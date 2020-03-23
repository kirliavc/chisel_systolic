package systolic

import chisel3._
import chisel3.util._
//import chisel3.Driver
import chisel3.iotesters.{PeekPokeTester, Driver}

class BFStatOut(id: Int, dim: Int, m: Int, n: Int, width: Int) extends Module{
  val c_width = m*n*width
  val io = IO(new Bundle{
    val reduce_cycle = Input(UInt(24.W))
    val in_valid = Input(Bool())
    val from_io = Input(Valid(UInt(c_width.W)))
    val to_io = Output(Valid(UInt(c_width.W)))
    val from_cell = Input(UInt(c_width.W))
    val to_cell = Output(UInt(c_width.W))
  })
  val out_valid = RegInit(false.B)
  out_valid := io.in_valid
  val trans_C = RegInit(0.U(c_width.W))
  val trans_C_valid = RegInit(false.B)
  //val stat_C = Module(new RegIO(m*n,width))
  val stat_C = RegInit(0.U(c_width.W))
  val stat_C_valid = RegInit(false.B)
  val cycle = RegInit(0.U(10.W))
  val out_cycle = RegInit(0.U(10.W))
  //printf("%d %d\n",stat_C, trans_C)
  cycle:=Mux(cycle===io.reduce_cycle, 0.U, cycle) + out_valid
  io.to_cell:=Mux(cycle===io.reduce_cycle | cycle===0.asUInt, 0.U, stat_C)
  io.to_io.bits:=trans_C
  io.to_io.valid:=trans_C_valid
  stat_C:=io.from_cell
  stat_C_valid:=out_valid
  when(cycle===io.reduce_cycle){
    out_cycle:=(dim-id-1).asUInt
    trans_C:=stat_C
    trans_C_valid:=stat_C_valid
  }.elsewhen(out_cycle>0.U){
    out_cycle:=out_cycle-1.U
  }.otherwise{
    out_cycle:=0.U
    trans_C:=io.from_io.bits
    trans_C_valid:=io.from_io.valid
  }
}
class SystolicPE(id: Int, dim: Int, m: Int, n: Int, width: Int) extends Module{
    val io = IO(new Bundle {
        val in_red_cycle = Input(UInt(24.W))
        val out_red_cycle = Output(UInt(24.W))
        val in_a = Input(Valid(UInt((m*width).W)))
        val in_b = Input(Valid(UInt((n*width).W)))
        val in_c = Input(Valid(UInt((m*n*width).W)))
        val out_c = Output(Valid(UInt((m*n*width).W)))
        val out_a = Output(Valid(UInt((m*width).W)))
        val out_b = Output(Valid(UInt((n*width).W)))
    })
    val reduce_cycle = RegInit(100.U(20.W))
    val reg_a = RegInit(0.U.asTypeOf(Valid(UInt((m*width).W))))
    val reg_b = RegInit(0.U.asTypeOf(Valid(UInt((n*width).W))))
    reduce_cycle := io.in_red_cycle
    io.out_red_cycle := reduce_cycle
    reg_a := io.in_a
    reg_b := io.in_b
    io.out_a := reg_a
    io.out_b := reg_b

    val pe = Module(new ComputeCell(m, n, width)).io
    val stat = Module(new BFStatOut(id, dim, m, n, width)).io
    pe.in_a := reg_a.bits
    pe.in_b := reg_b.bits
    stat.reduce_cycle:=reduce_cycle
    stat.in_valid:=io.in_a.valid & io.in_b.valid
    stat.from_io := io.in_c
    stat.from_cell := pe.out_c
    io.out_c := stat.to_io
    pe.in_c := stat.to_cell
}

class BFSystolicPE(id: Int, dim: Int, m: Int, n: Int, width: Int) extends Module{
    val io = IO(new Bundle {
        val ctrl = Input(UInt(3.W))
        val sgn = Input(UInt(1.W))
        val out_ctrl = Output(UInt(3.W))
        val out_sgn = Output(UInt(1.W))
        val in_red_cycle = Input(UInt(24.W))
        val out_red_cycle = Output(UInt(24.W))
        val in_a = Input(Valid(UInt((m*width).W)))
        val in_b = Input(Valid(UInt((n*width).W)))
        val in_c = Input(Valid(UInt((m*n*width).W)))
        val out_c = Output(Valid(UInt((m*n*width).W)))
        val out_a = Output(Valid(UInt((m*width).W)))
        val out_b = Output(Valid(UInt((n*width).W)))
    })
    val reg_ctrl = RegInit(0.U(3.W))
    val reg_sgn = RegInit(0.U(3.W))
    reg_ctrl := io.ctrl
    reg_sgn := io.sgn
    io.out_ctrl := reg_ctrl
    io.out_sgn := reg_sgn
    val reduce_cycle = RegInit(100.U(20.W))
    val reg_a = RegInit(0.U.asTypeOf(Valid(UInt((m*width).W))))
    val reg_b = RegInit(0.U.asTypeOf(Valid(UInt((n*width).W))))
    reduce_cycle := io.in_red_cycle
    io.out_red_cycle := reduce_cycle
    reg_a := io.in_a
    reg_b := io.in_b
    io.out_a := reg_a
    io.out_b := reg_b

    val pe = Module(new ComputeCell(m, n, width)).io
    val stat = Module(new BFStatOut(id, dim, m, n, width)).io
    pe.in_a := reg_a.bits
    pe.in_b := reg_b.bits
    stat.reduce_cycle:=reduce_cycle
    stat.in_valid:=io.in_a.valid & io.in_b.valid
    stat.from_io := io.in_c
    stat.from_cell := pe.out_c
    io.out_c := stat.to_io
    pe.in_c := stat.to_cell
}
//tester: s=4, t=16, b=4
//output: s=8, t=64, b=8

// class Systolic(s: Int, x: Int, t: Int, b: Int, m: Int, n: Int, width: Int) extends Module{
//   val io = IO(new Bundle{
//     val a_in = Input(Valid(Vec(s/b, UInt((m*width).W))))
//     val b_in = Input(Valid(Vec(x/b, UInt((n*width).W))))
//     val c_out = Output(Vec(s*x/t,Valid(UInt((m*n*width).W))))
//   })
//   //assert(s*x/t==1)
//   val pes = for(i <- 0 until s) yield{
//     for(j <- 0 until x) yield{
//       Module(new SystolicPE(t, i, s, m, n, width))
//     }
//   }
//   val a_input = Module(new DFSysIn_Input(t, s, b, 1, m*width))
//   val b_input = Module(new DFSysIn_Kernel(t, x, b, b, n*width))
//   val transC = Module(new DCStatOut(t, s, x, m*n*width))
//   a_input.io.data_in.valid:=io.a_in.valid
//   b_input.io.data_in.valid:=io.b_in.valid
//   for(i <- 0 until s/b){
//     a_input.io.data_in.bits(i):=io.a_in.bits(i).asUInt
//   }
//   for(i <- 0 until x/b){
//     b_input.io.data_in.bits(i):=io.b_in.bits(i).asUInt
//   }
//   for(i <- 0 until s){
//     pes(i)(0).io.in_a:=a_input.io.data_out(i)
//   }
//   for(i<- 0 until s){
//     for(j<- 1 until x){
//       pes(i)(j).io.in_a:=pes(i)(j-1).io.out_a
//     }
//   }
//   for(i <- 0 until x){
//     pes(0)(i).io.in_b:=b_input.io.data_out(i)
//   }
//   for(i<- 0 until x){
//     for(j<- 1 until s){
//       pes(j)(i).io.in_b:=pes(j-1)(i).io.out_b
//     }
//   }
//   for(i <- 0 until x){
//     pes(0)(i).io.in_c.bits:=0.U
//     pes(0)(i).io.in_c.valid:=false.B
//   }
//   for(i <- 1 until s){
//     for(j <- 0 until x){
//       pes(i)(j).io.in_c:=pes(i-1)(j).io.out_c
//     }
//   }
//   for(i <- 0 until x){
//     transC.io.data_in(i).valid:=pes(s-1)(i).io.out_c.valid
//     transC.io.data_in(i).bits:=pes(s-1)(i).io.out_c.bits
    
//     //io.c_out(i).valid:=pes(s-1)(i).io.res_out.valid
//   }
//   for(i <- 0 until s*x/t){
//     io.c_out(i).valid:=transC.io.data_out.valid
//     io.c_out(i).bits:=transC.io.data_out.bits(i)
//   }
//   transC.io.data_out.ready:=true.B
// }
/*
 tensor A 从左向右(filter)，tensor B从上向下(input)
 
 tensor C从下方输出(output)，每个cycle输出x个。
systolic array的PEsize是s*x
 s = PE行数: 输出channel数
 x = PE列数:每次输出的一行的元素个数
 max_input_w = 输入feature map最大的宽度
 max_c = 最大的输入channel数
 max_ks =最大的filter size+1
 cycle_read_input = input每个cycle读入的元素个数
 cycle_read_filter = filter每个cycle读入的元素个数
 m = 每个PE计算的submatrix A
 n = 每个PE计算的submatrix B
 width = data bitwidth，unsigned
 输入数据Layout：NHCW 每一行不需要padding，W可以大于x，但必须小于max_input_w
 每个cycle输入max_input_w个数。c<max_c
 filter layout: K*C*ks*ks，K=s，ks<max_ks
 输入config:
 in_h, in_w, c, ks, pad, stride(暂时没有支持不同stride), buf_rep
 buf_rep指filter buffer被reuse的次数(算出一组结果算1次)
 input_cycle指输入filter和input所需要的cycle数。
 目前测试的case是
 in_h = 24, in_w 随意, c=4, ks=3, pad=1, buf_rep=1
 cycle_read_input=4, cycle_read_filter=8
 对于Input feature map，需输入(ks-pad)行之后才可以计算，共需要(ks-pad)*c*in_h/cycle_read_input个cycle
 输入filter需要k*c*ks*ks/cycle_read_filter个cycle
 由于input和filter大小不同，所以它们的输入时间也不同。第一次input之前需要填充buffer。计算单元再input_cycle之后开始工作。因此，将input_cycle设置成比最长的输入时间还要长。这个例子中，filter输入72 cycle，input 96cycle，将input_cycle设成100.
 为保证计算单元一直运行，计算的速度比输入速度慢。buffer的大小有限。如果输入的数据将buffer填满了，则(input/filter)的ready信号会变成false。此时再输入数据则不会被读取。
 output stationary模式，每c*ks*ks个cycle算出一组output。一组output的大小是out_channel*in_column，也就是"KCHW"Layout中的K*W

 input采用line buffer，每次输入一行的数据，与之前的ks-1行数据合并，作为本次的输入
 在c*ks*ks个cycle中，input共输入c*(x+ks-1)个数。因此每个cycle输入cell(x+ks-1/(ks*ks))
 filter共输入s*c*ks*ks个数。每组filter在buffer中reuse bs次。因此每个cycle输入s/bs。filter采用tiling的reuse策略。
 在计算开始前的准备阶段，input要输入ks*(x+ks-1)*c，需要ks*ks*ks*c个cycle
 filter输入s*c*ks*ks，需要c*ks*ks*bs个cycle。假设bs=ks，也就是每个filter reuse的次数正好是ks次。

c*ks*ks cycle
输入一行，长度为in_w*c
*/
// config: in_h, in_w, c, ks, pad, stride

class Systolic_Rect(s: Int, x: Int, max_input_w: Int, max_input_h: Int, max_c: Int, max_ks: Int, cycle_read_input: Int, cycle_read_kernel: Int, cycle_out_res: Int, m: Int, n: Int, width: Int) extends Module{
  val io = IO(new Bundle{
    val config = Input(new ConvConfig())
    val a_in = DeqIO(Vec(cycle_read_kernel, UInt((n*width).W)))
    val b_in = DeqIO(Vec(cycle_read_input, UInt((m*width).W)))
    //val c_out = Output(Vec(x,Valid(UInt((m*n*width).W))))
    val c_out = Output(Valid(Vec(cycle_out_res, UInt((m*n*width).W))))
  })
  val wait_data_cycle = RegInit(100.U(20.W))
  //wait_data_cycle := Mux(wait_data_cycle>io.config.input_cycle, io.config.input_cycle,Mux(wait_data_cycle=/=0.U, wait_data_cycle-1.U, 0.U))
  wait_data_cycle := Mux(wait_data_cycle=/=0.U, wait_data_cycle-1.U, 0.U)
  val reduce_cycle = RegInit(333333.U(20.W))
  reduce_cycle := io.config.c * io.config.ks * io.config.ks
  //assert(s*x/t==1)
  val pes = for(i <- 0 until s) yield{
    for(j <- 0 until x) yield{
      Module(new SystolicPE(i, s, m, n, width))
    }
  }
  // printf("PE status\n")
  //   for(j <- 0 until x){
  //     printf("(%d, %d, %d, %d)",pes(0)(j).io.in_b.bits,pes(0)(j).io.in_b.valid, pes(0)(j).io.cur_dt, pes(0)(j).io.cur_cycle)
  //   }
  // printf("\n")
  val a_input = Module(new DFSysIn_Kernel(s, max_c*max_ks*max_ks, 1,cycle_read_kernel, n*width))
  val b_input = Module(new DFSysIn_Input(x, max_input_w, max_c, max_ks, cycle_read_input, m*width))
  val c_output = Module(new Update_Result(x, s, max_input_h, max_input_w/s, cycle_out_res, m*n*width))
  //val transC = Module(new DCStatOut(t, s, x, m*n*width))
  io.a_in.ready := a_input.io.data_in.ready
  io.b_in.ready := b_input.io.data_in.ready
  a_input.io.data_out.ready:=(wait_data_cycle===0.U)
  b_input.io.data_out.ready:=(wait_data_cycle===0.U)
  a_input.io.config:=io.config
  b_input.io.config:=io.config
  c_output.io.config:=io.config
  a_input.io.data_in.valid:=io.a_in.valid
  b_input.io.data_in.valid:=io.b_in.valid
  io.c_out:=c_output.io.data_out
  for(i <- 0 until cycle_read_kernel){
    a_input.io.data_in.bits(i):=io.a_in.bits(i).asUInt
  }
  for(i <- 0 until cycle_read_input){
    b_input.io.data_in.bits(i):=io.b_in.bits(i).asUInt
  }
  for(i <- 0 until s){
    pes(i)(0).io.in_red_cycle:=reduce_cycle
    pes(i)(0).io.in_a:=a_input.io.data_out.bits(i)
  }
  for(i<- 0 until s){
    for(j<- 1 until x){
      pes(i)(j).io.in_red_cycle:=pes(i)(j-1).io.out_red_cycle
      pes(i)(j).io.in_a:=pes(i)(j-1).io.out_a
    }
  }
  for(i <- 0 until x){
    pes(0)(i).io.in_b:=b_input.io.data_out.bits(i)
  }
  for(i<- 0 until x){
    for(j<- 1 until s){
      pes(j)(i).io.in_b:=pes(j-1)(i).io.out_b
    }
  }
  for(i <- 0 until x){
    pes(0)(i).io.in_c.bits:=0.U
    pes(0)(i).io.in_c.valid:=false.B
  }
  for(i <- 1 until s){
    for(j <- 0 until x){
      pes(i)(j).io.in_c:=pes(i-1)(j).io.out_c
    }
  }
  for(i <- 0 until x){
    c_output.io.data_in(i):=pes(s-1)(i).io.out_c
  }
  //   //io.c_out(i).valid:=pes(s-1)(i).io.res_out.valid
  // }
  // for(i <- 0 until s*x/t){
  //   io.c_out(i).valid:=transC.io.data_out.valid
  //   io.c_out(i).bits:=transC.io.data_out.bits(i)
  // }
  // transC.io.data_out.ready:=true.B
}

class BFSystolic(s: Int, x: Int, max_input_w: Int, max_input_h: Int, max_c: Int, max_ks: Int, cycle_read_input: Int, cycle_read_kernel: Int, cycle_out_res: Int, m: Int, n: Int, width: Int) extends Module{
  val io = IO(new Bundle{
    val ctrl = Input(UInt(3.W))
    val sgn = Input(UInt(1.W))
    val config = Input(new ConvConfig())
    val a_in = DeqIO(Vec(cycle_read_kernel, UInt((n*width).W)))
    val b_in = DeqIO(Vec(cycle_read_input, UInt((m*width).W)))
    //val c_out = Output(Vec(x,Valid(UInt((m*n*width).W))))
    val c_out = Output(Valid(Vec(cycle_out_res, UInt((m*n*width).W))))
  })
  val wait_data_cycle = RegInit(100.U(20.W))
  //wait_data_cycle := Mux(wait_data_cycle>io.config.input_cycle, io.config.input_cycle,Mux(wait_data_cycle=/=0.U, wait_data_cycle-1.U, 0.U))
  wait_data_cycle := Mux(wait_data_cycle=/=0.U, wait_data_cycle-1.U, 0.U)
  val reduce_cycle = RegInit(333333.U(20.W))
  reduce_cycle := io.config.c * io.config.ks * io.config.ks
  //assert(s*x/t==1)
  val pes = for(i <- 0 until s) yield{
    for(j <- 0 until x) yield{
      Module(new BFSystolicPE(i, s, m, n, width))
    }
  }
  // printf("PE status\n")
  //   for(j <- 0 until x){
  //     printf("(%d, %d, %d, %d)",pes(0)(j).io.in_b.bits,pes(0)(j).io.in_b.valid, pes(0)(j).io.cur_dt, pes(0)(j).io.cur_cycle)
  //   }
  // printf("\n")
  val a_input = Module(new DFSysIn_Kernel(s, max_c*max_ks*max_ks, 1,cycle_read_kernel, n*width))
  val b_input = Module(new DFSysIn_Input(x, max_input_w, max_c, max_ks, cycle_read_input, m*width))
  val c_output = Module(new Update_Result(x, s, max_input_h, max_input_w/s, cycle_out_res, m*n*width))
  //val transC = Module(new DCStatOut(t, s, x, m*n*width))
  io.a_in.ready := a_input.io.data_in.ready
  io.b_in.ready := b_input.io.data_in.ready
  a_input.io.data_out.ready:=(wait_data_cycle===0.U)
  b_input.io.data_out.ready:=(wait_data_cycle===0.U)
  a_input.io.config:=io.config
  b_input.io.config:=io.config
  c_output.io.config:=io.config
  a_input.io.data_in.valid:=io.a_in.valid
  b_input.io.data_in.valid:=io.b_in.valid
  io.c_out:=c_output.io.data_out
  for(i <- 0 until cycle_read_kernel){
    a_input.io.data_in.bits(i):=io.a_in.bits(i).asUInt
  }
  for(i <- 0 until cycle_read_input){
    b_input.io.data_in.bits(i):=io.b_in.bits(i).asUInt
  }
  for(i <- 0 until s){
    pes(i)(0).io.ctrl:=io.ctrl
    pes(i)(0).io.sgn:=io.sgn
    pes(i)(0).io.in_red_cycle:=reduce_cycle
    pes(i)(0).io.in_a:=a_input.io.data_out.bits(i)
  }
  for(i<- 0 until s){
    for(j<- 1 until x){
      pes(i)(j).io.ctrl:=pes(i)(j-1).io.out_ctrl
      pes(i)(j).io.sgn:=pes(i)(j-1).io.out_sgn
      pes(i)(j).io.in_red_cycle:=pes(i)(j-1).io.out_red_cycle
      pes(i)(j).io.in_a:=pes(i)(j-1).io.out_a
    }
  }
  for(i <- 0 until x){
    pes(0)(i).io.in_b:=b_input.io.data_out.bits(i)
  }
  for(i<- 0 until x){
    for(j<- 1 until s){
      pes(j)(i).io.in_b:=pes(j-1)(i).io.out_b
    }
  }
  for(i <- 0 until x){
    pes(0)(i).io.in_c.bits:=0.U
    pes(0)(i).io.in_c.valid:=false.B
  }
  for(i <- 1 until s){
    for(j <- 0 until x){
      pes(i)(j).io.in_c:=pes(i-1)(j).io.out_c
    }
  }
  for(i <- 0 until x){
    c_output.io.data_in(i):=pes(s-1)(i).io.out_c
  }
  //   //io.c_out(i).valid:=pes(s-1)(i).io.res_out.valid
  // }
  // for(i <- 0 until s*x/t){
  //   io.c_out(i).valid:=transC.io.data_out.valid
  //   io.c_out(i).bits:=transC.io.data_out.bits(i)
  // }
  // transC.io.data_out.ready:=true.B
}