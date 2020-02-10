package systolic

import chisel3._
import chisel3.iotesters.{PeekPokeTester, Driver}
import scala.collection.mutable.ArrayBuffer
import scala.math.log10
import chisel3.util._
//总共b*t*col个cycle，写入s*t*col个数，因此每个cycle读s/b个数。目前s/b=1
class SYIBuffer(t: Int, s: Int, b: Int, col: Int, id: Int, width: Int) extends Module{
  val io = IO(new Bundle{
    // 每个buf，进入id*line，写给自己line，输出id*line-line，总共t次循环，写满t*line的buf

    // data_in:上一个buf传来的数据
    // data_next: 传给下一个buf
    // data_out: 传给PE
    val data_in = Input(Valid(Vec(s/b, UInt(width.W))))
    val data_next = Output(Valid(Vec(s/b, UInt(width.W))))
    val data_out = Output(Valid(UInt(width.W)))
  })
  val inp_data = RegInit(0.U.asTypeOf(Valid(Vec(s/b, UInt(width.W)))))
  val mem = SyncReadMem(2*col*t, UInt(width.W))
  val out_valid = RegInit(VecInit(Seq.fill(2)(false.B)))
  val in_mem = RegInit(0.U(1.W))
  val in_off = RegInit(0.U(16.W))
  val out_off = RegInit(0.U(16.W))
  val out_mem = RegInit(0.U(1.W))
  val in_addr = RegInit(0.U(16.W))
  val num_items = RegInit(0.U(16.W))
  val out_addr = RegInit(0.U(16.W))
  val row_num = RegInit(0.U(16.W))
  val out_wait_cycle = RegInit((id.asUInt)(16.W))
  val real_out_valid = RegInit(false.B)
  //val real_out_bits = RegInit(0.U(width.W))
  val out_addr_add = Wire(Bool())
  val inp_addr_add = Wire(Bool())

  //直接把上一个buf传来的数据，延迟一个cycle之后传给下一个buf
  inp_data := io.data_in
  io.data_next:=inp_data

  //num_items记录当前的buf总共来了多少data。每s个data，当前的buf取一个，把其他的data传给下一个buf
  //num_items = (num_items + inp_data.valid)%s
  num_items:=Mux(num_items===(s-1).asUInt && inp_data.valid, 0.U,num_items+inp_data.valid)

  //每来s个data，第一个buf保存最后一个data，第二个buf保存倒数第二个，以此类推，最后一个buf保存第一个data
  //id代表是第几个buf
  //inp_addr_add表示在当前cycle是否保存data
  //如果保存，则in_addr=in_addr+s/b
  /*
    cycle 0
    | | | | |
    cycle 1
    |4| | | |
    cycle 2
    |3|4| | |
    cycle 3
    |2|3|4| |
    cycle 4
    |1|2|3|4|
    从整体来看，在cycle4的时候，所有的buf都写入data。但每个buf是分别控制的
  */
  
  inp_addr_add := Mux(inp_data.valid &(num_items===(s-id-1).asUInt), (s/b).asUInt, 0.U)
  in_addr := Mux(in_addr === (col*t-s/b).asUInt && inp_addr_add, 0.U, in_addr + inp_addr_add)

  when(inp_addr_add){
    for(i <- 0 until s/b){
      mem.write(in_off+in_addr+i.asUInt, inp_data.bits(i))
    }
    //double buffer，每个buf的大小是col*t，如果写完了当前的buf，就切到另一个buf去写，同时设置该buf为可读
    when(in_addr===(col*t-s/b).asUInt){
      out_valid(in_mem):=true.B
      in_off:=(col*t).asUInt-in_off
      in_mem:=(!in_mem)
    }
  }
  //从整体上看，所有buf的数据都会在同一cycle更新。但是systolic array要求不同buf的输出形成流水线，、因此除了第一个PE以外，别的PE都要等待id个cycle才能输出。用out_wait_cycle表示。但由于每个buf输出的cycle数是一样的，所以只需要在第一次输出的时候延迟id个cycle。流水线开始工作以后，out_valid会一直是true直到输入结束
  when(!out_valid(out_mem)){
    out_wait_cycle := id.asUInt
  }.otherwise{
    out_wait_cycle:=Mux(out_wait_cycle===0.U, out_wait_cycle, out_wait_cycle-1.U)
  }
  //在valid且wait_cycle=0时输出数据，更新out_addr。输出会reuse b次。输出reuse的次数用row_num表示。
  when(row_num===(b-1).asUInt && out_addr===(col*t-1).asUInt){
    out_valid(out_mem):=false.B
    out_mem:=(!out_mem)
    out_off:=(col*t).asUInt-out_off
    row_num:=0.U
  }.otherwise{
    row_num:=row_num + (out_addr===(col*t-1).asUInt)  // reuse b times, rownum+1=reuse
  }
  out_addr_add := Mux(out_valid(out_mem) && out_wait_cycle===0.U, (s/b).asUInt, 0.U)
  out_addr := Mux(out_addr === (col*t-s/b).asUInt && out_addr_add, 0.U, out_addr + out_addr_add)

  // 因为读取mem需要一个cycle的延迟，所以valid信号也要延迟1个cycle输出。用real_out_valid表示。
  real_out_valid := out_valid(out_mem) && out_wait_cycle===0.U
  
  io.data_out.valid:=real_out_valid
  io.data_out.bits:=mem.read(out_off+out_addr)//real_out_bits
}
/*
  b = tensor在buffer中reuse的次数
  col = reuse buffer的大小，每算出一组C的结果记为1次
  例如，运算顺序是A1*B1,A1*B2,A1*B3,A2*B1,A2*B2,A2*B3
  对A来说b=3,col=1
  对B来说b=2,col=3
*/
class DFSysIn(t: Int, s: Int, b: Int, col: Int, width: Int) extends Module{
  val io=IO(new Bundle{
    val data_in = Input(Valid(Vec(s/b, UInt(width.W))))
    val data_out = Output(Vec(s, Valid(UInt(width.W))))
  })
  //printf("sysin.out: %d %d %d %d\n", io.data_out(0).bits, io.data_out(1).bits, io.data_out(2).bits, io.data_out(3).bits)
  //printf("sysin.valid: %d %d %d %d\n", io.data_out(0).valid, io.data_out(1).valid, io.data_out(2).valid, io.data_out(3).valid)
  val buffers = for(i <- 0 until s) yield{
    Module(new SYIBuffer(t, s, b, col, i, width))
  }
  for(i <- 0 until s-1){
    buffers(i+1).io.data_in<>buffers(i).io.data_next
  }
  buffers(0).io.data_in<>io.data_in
  for(i <- 0 until s){
    io.data_out(i).bits:=buffers(i).io.data_out.bits
    io.data_out(i).valid:=buffers(i).io.data_out.valid
  }
}
//总共s*x个PE，每t个cycle产生一组结果，在t个cycle内输出，每个cycle输出s*x/t个data。
/*
假设s=3,x=3
cycle 1
|1| | |   output=1
cycle 2
|2|4| |   output=2
cycle 3
|3|4,5|7| output=3
cycle 4
|4|5,6|7,8| output=4
cycle 5
|5|6|7,8,9| output=5
cycle 6
|6|7|8,9| output=6
注意到，4在cycle2时就在buffer2产生，但是cycle3停留在buffer2，cycle4才传回buffer1
7在cycle3时产生，等待2cycle后才向buffer2传。
除了buffer1以外，后面的buffer都要等前面的buffer把所有数据全部输出之后再输出。
等待的cycle数是id*(s-2)。因为第id个buffer数据的生成晚了id个cycle，传到第一个buffer输出又需要id个cycle。
*/


class STOBuffer(t: Int, s: Int, x: Int, id: Int, width: Int) extends Module{
  val io = IO(new Bundle{
    // 每个buf，进入id*line，写给自己line，输出id*line-line，总共t次循环，写满t*line的buf
    val data_in = DeqIO(UInt(width.W))  //connect to array
    val data_out = Decoupled(Vec(s*x/t, UInt(width.W))) //connect to prev buf
    val data_next = Flipped(Decoupled((Vec(s*x/t, UInt(width.W)))))  //connect to next buf
  })

  io.data_in.ready:=true.B
  io.data_next.ready:=true.B
  val out_valid = RegInit(VecInit(Seq.fill(2)(false.B)))  // (t/s) slots
  val mem = SyncReadMem(2*s, UInt(width.W))
  val in_mem = RegInit(0.U(1.W))
  val in_off = RegInit(0.U(16.W))
  val out_off = RegInit(0.U(16.W))
  val out_mem = RegInit(0.U(1.W))
  val in_addr = RegInit(0.U(16.W))
  val out_addr = RegInit(0.U(16.W))
  val from_next_bits = RegInit(VecInit(Seq.fill(s*x/t)(0.U(width.W))))
  val from_next_valid = RegInit(false.B)
  val out_wait_cycle = RegInit((id*(s-2)).asUInt(16.W))
  val read_mem_data = RegInit(VecInit(Seq.fill(s*x/t)(0.U(width.W))))
  val read_mem_valid = RegInit(false.B)
  val read_mem_valid_next = RegInit(false.B)
  //从PE中读s*x/t个数，写到mem中。没考虑不整除的情况

  when(io.data_in.valid){
    for(i <- 0 until s*x/t)
      mem.write(in_off+in_addr+i.asUInt, io.data_in.bits)
    in_addr:=(in_addr+(s*x/t).U)%(s).asUInt
    when(in_addr===(s-s*x/t).asUInt){
      out_valid(in_mem):=true.B
      in_off:=s.asUInt-in_off
      in_mem:=(!in_mem)
    }
  }

  //从下一个buf中读数据
  from_next_bits:=io.data_next.bits
  from_next_valid:=io.data_next.valid
  //输出的既可能是当前mem中的data，也可能是下一个buf传过来的
  
  //同样，valid信号延迟1cycle
  for(i <- 0 until s*x/t)
    read_mem_data(i):=mem.read(out_off+out_addr+i.asUInt)
  read_mem_valid := out_valid(out_mem)&&out_wait_cycle===0.U
  read_mem_valid_next :=read_mem_valid

  when(read_mem_valid_next){
    io.data_out.bits:=read_mem_data
  }.otherwise{
    io.data_out.bits:=from_next_bits
  }
  io.data_out.valid:=read_mem_valid_next|from_next_valid

  when(out_valid(out_mem)&& out_wait_cycle===0.U){
    // %操作可以用Mux重写，应该对减少计算复杂度有帮助
    out_addr:=(out_addr+(s*x/t).asUInt)%(s.asUInt)
    when(out_addr===(s-s*x/t).asUInt){
      out_valid(out_mem):=false.B
      out_mem:=(!out_mem)
      out_off:=s.asUInt-out_off
    }
  }
  when(!out_valid(out_mem)){
    out_wait_cycle := (id*(s-2)).asUInt
  }.otherwise{
    out_wait_cycle:=Mux(out_wait_cycle===0.U, out_wait_cycle, out_wait_cycle-1.U)
  }


  
}

class DCStatOut(t: Int, s: Int, x: Int, width: Int) extends Module{
  val io=IO(new Bundle{
    val data_out = EnqIO(Vec(s*x/t, UInt(width.W)))
    val data_in = Vec(x, DeqIO(UInt(width.W)))
  })

  val buffers = for(i <- 0 until x) yield{
    Module(new STOBuffer(t, s, x, i, width))
  }
  for(i <- 0 until x-1){
    buffers(i).io.data_next<>buffers(i+1).io.data_out
  }
  buffers(x-1).io.data_next.valid:=false.B
  buffers(x-1).io.data_next.bits:=VecInit(Seq.fill(s*x/t)(0.U(width.W)))
  io.data_out<>buffers(0).io.data_out
  for(i <- 0 until x){
    buffers(i).io.data_in<>io.data_in(i)
  }
}
/*
class STIBuffer(t: Int, s: Int, b: Int, col: Int, id: Int, width: Int) extends Module{
  val io = IO(new Bundle{
    val data_in = Input(Valid(UInt(width.W)))
    val data_next = Output(Valid(UInt(width.W)))
    val data_out = Output(Valid(UInt(width.W)))
  })
  val mem = SyncReadMem(2*col*s, UInt(width.W))
  val out_valid = RegInit(VecInit(Seq.fill(2)(false.B)))
  val in_valid = RegInit(VecInit(Seq.fill(2)(true.B)))
  val in_mem = RegInit(0.U(1.W))
  val in_off = RegInit(0.U(width.W))
  val out_off = RegInit(0.U(width.W))
  val out_mem = RegInit(0.U(1.W))
  val in_addr = RegInit(0.U(width.W))
  val num_items = RegInit(0.U(width.W))
  val row_num = RegInit(0.U(width.W))
  val to_next_bits = RegInit((0.U(width.W)))
  val to_next_valid = RegInit(false.B)
  val out_wait_cycle = RegInit(VecInit(Seq.fill(2)(0.U(width.W))))
  val real_out_valid = RegInit(false.B)
  val real_out_bits = RegInit(0.U(width.W))
  val out_cyclepert = RegInit(0.U(width.W))
  val out_percol = RegInit(0.U(width.W))
  real_out_valid := out_valid(out_mem) && out_wait_cycle(out_mem)===0.U &&(out_cyclepert < s.asUInt)
  io.data_out.valid:=real_out_valid
  real_out_bits:=mem.read(out_off+out_percol+Mux(out_cyclepert>=s.asUInt,s.asUInt,out_cyclepert))
  io.data_out.bits:=real_out_bits
  when(out_valid(out_mem) && out_wait_cycle(out_mem)===0.U){
    out_percol:=(out_percol+Mux(out_cyclepert===(t-1).asUInt, t.asUInt, 0.U))%(col*t).asUInt
    out_cyclepert:=(out_cyclepert+1.U)%t.asUInt
  }
  when(row_num===(b-1).asUInt && out_percol===(col*t-t).asUInt && out_cyclepert===(t-1).asUInt){
    in_valid(out_mem):=true.B
    out_valid(out_mem):=false.B
    out_mem:=(!out_mem)
    out_off:=(col*t).asUInt-out_off
    row_num:=0.U
  }.otherwise{
    row_num:=row_num + (out_percol===(col*t-t).asUInt && out_cyclepert===(t-1).asUInt)  
  }
  to_next_bits:=io.data_in.bits
  to_next_valid:=io.data_in.valid & (num_items=/=(id-1).asUInt) 
  io.data_next.valid:=to_next_valid
  io.data_next.bits:=to_next_bits
  num_items:=Mux(num_items===(id-1).asUInt && io.data_in.valid, 0.U,num_items+io.data_in.valid)

  for (i <- 0 until 2) out_wait_cycle(i):=Mux(out_wait_cycle(i)===0.U, 0.U, out_wait_cycle(i)-1.U)
  when(io.data_in.valid &(num_items===(id-1).asUInt) & in_valid(in_mem)){

    mem.write(in_off+in_addr, io.data_in.bits)
    in_addr:=(in_addr+1.U)%(col*t).asUInt
    when(in_addr===(col*t-1).asUInt){
      out_valid(in_mem):=true.B
      in_valid(in_mem):=false.B
      out_wait_cycle(in_mem):=(s-id).asUInt
      in_off:=(col*t).asUInt-in_off
      in_mem:=(!in_mem)
    }
  }
}
class DFStatIn(t: Int, s: Int, b: Int, col: Int, width: Int) extends Module{
  val io=IO(new Bundle{
    val data_in = Input(Valid(UInt(width.W)))
    val data_out = Output(Vec(s, Valid(UInt(width.W))))
  })
  val buffers = for(i <- 0 until s) yield{
    Module(new STIBuffer(t, s, b, col, s-i, width))
  }
  for(i <- 0 until s-1){
    buffers(i+1).io.data_in<>buffers(i).io.data_next
  }
  buffers(0).io.data_in<>io.data_in
  for(i <- 0 until s){
    io.data_out(i).bits:=buffers(i).io.data_out.bits
    io.data_out(i).valid:=buffers(i).io.data_out.valid
  }
}

*/

