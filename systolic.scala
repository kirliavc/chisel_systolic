package systolic

import chisel3._
import chisel3.util._
//import chisel3.Driver
import chisel3.iotesters.{PeekPokeTester, Driver}

class BFStatOut(t: Int, id: Int, dim: Int, m: Int, n: Int, width: Int) extends Module{
  val c_width = m*n*width
  val io = IO(new Bundle{
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
  cycle:=Mux(cycle===t.asUInt, 0.U, cycle) + out_valid
  io.to_cell:=Mux(cycle===t.asUInt | cycle===0.asUInt, 0.U, stat_C)
  io.to_io.bits:=trans_C
  io.to_io.valid:=trans_C_valid
  stat_C:=io.from_cell
  stat_C_valid:=out_valid
  when(cycle===t.asUInt){
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
class SystolicPE(t: Int, id: Int, dim: Int, m: Int, n: Int, width: Int) extends Module{
    val io = IO(new Bundle {
        val in_a = Input(Valid(UInt((m*width).W)))
        val in_b = Input(Valid(UInt((n*width).W)))
        val in_c = Input(Valid(UInt((m*n*width).W)))
        val out_c = Output(Valid(UInt((m*n*width).W)))
        val out_a = Output(Valid(UInt((m*width).W)))
        val out_b = Output(Valid(UInt((n*width).W)))
    })
    val reg_a = RegInit(0.U.asTypeOf(Valid(UInt((m*width).W))))
    val reg_b = RegInit(0.U.asTypeOf(Valid(UInt((n*width).W))))
    reg_a := io.in_a
    reg_b := io.in_b
    io.out_a := reg_a
    io.out_b := reg_b

    val pe = Module(new ComputeCell(m, n, width)).io
    val stat = Module(new BFStatOut(t, id, dim, m, n, width)).io
    pe.in_a := reg_a.bits
    pe.in_b := reg_b.bits
    io.out_a := reg_a
    io.out_b := reg_b
    stat.in_valid:=io.in_a.valid & io.in_b.valid
    stat.from_io := io.in_c
    stat.from_cell := pe.out_c
    io.out_c := stat.to_io
    pe.in_c := stat.to_cell
}
//tester: s=4, t=16, b=4
//output: s=8, t=64, b=8

class Systolic(s: Int, x: Int, t: Int, b: Int, m: Int, n: Int, width: Int) extends Module{
  val io = IO(new Bundle{
    val a_in = Input(Valid(Vec(s/b, UInt((m*width).W))))
    val b_in = Input(Valid(Vec(x/b, UInt((n*width).W))))
    val c_out = Output(Vec(s*x/t,Valid(UInt((m*n*width).W))))
  })
  //assert(s*x/t==1)
  val pes = for(i <- 0 until s) yield{
    for(j <- 0 until x) yield{
      Module(new SystolicPE(t, i, s, m, n, width))
    }
  }
  val a_input = Module(new DFSysIn(t, s, b, 1, m*width))
  val b_input = Module(new DFSysIn(t, x, b, b, n*width))
  val transC = Module(new DCStatOut(t, s, x, m*n*width))
  a_input.io.data_in.valid:=io.a_in.valid
  b_input.io.data_in.valid:=io.b_in.valid
  for(i <- 0 until s/b){
    a_input.io.data_in.bits(i):=io.a_in.bits(i).asUInt
  }
  for(i <- 0 until x/b){
    b_input.io.data_in.bits(i):=io.b_in.bits(i).asUInt
  }
  for(i <- 0 until s){
    pes(i)(0).io.in_a:=a_input.io.data_out(i)
  }
  for(i<- 0 until s){
    for(j<- 1 until x){
      pes(i)(j).io.in_a:=pes(i)(j-1).io.out_a
    }
  }
  for(i <- 0 until x){
    pes(0)(i).io.in_b:=b_input.io.data_out(i)
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
    transC.io.data_in(i).valid:=pes(s-1)(i).io.out_c.valid
    transC.io.data_in(i).bits:=pes(s-1)(i).io.out_c.bits
    
    //io.c_out(i).valid:=pes(s-1)(i).io.res_out.valid
  }
  for(i <- 0 until s*x/t){
    io.c_out(i).valid:=transC.io.data_out.valid
    io.c_out(i).bits:=transC.io.data_out.bits(i)
  }
  transC.io.data_out.ready:=true.B
}
/*
 tensor A 从左向右，tensor B从上向下
 s = PE行数
 x = PE列数
 t = 在PE内部累加的次数
 bs = tensor A在buffer中reuse的次数
 bx = tensor B在buffer中reuse的次数
 m = 每个PE计算的submatrix A
 n = 每个PE计算的submatrix B
 width = data bitwidth，unsigned
*/
class Systolic_Rect(s: Int, x: Int, t: Int, bs: Int, bx: Int, m: Int, n: Int, width: Int) extends Module{
  val io = IO(new Bundle{
    val a_in = Input(Valid(Vec(s/bs, UInt((m*width).W))))
    val b_in = Input(Valid(Vec(x/bx, UInt((n*width).W))))
    val c_out = Output(Vec(s*x/t,Valid(UInt((m*n*width).W))))
  })
  //assert(s*x/t==1)
  val pes = for(i <- 0 until s) yield{
    for(j <- 0 until x) yield{
      Module(new SystolicPE(t, i, s, m, n, width))
    }
  }
  val a_input = Module(new DFSysIn(t, s, bs, 1, m*width))
  val b_input = Module(new DFSysIn(t, x, bx, bs, n*width))
  val transC = Module(new DCStatOut(t, s, x, m*n*width))
  a_input.io.data_in.valid:=io.a_in.valid
  b_input.io.data_in.valid:=io.b_in.valid
  for(i <- 0 until s/bs){
    a_input.io.data_in.bits(i):=io.a_in.bits(i).asUInt
  }
  for(i <- 0 until x/bx){
    b_input.io.data_in.bits(i):=io.b_in.bits(i).asUInt
  }
  for(i <- 0 until s){
    pes(i)(0).io.in_a:=a_input.io.data_out(i)
  }
  for(i<- 0 until s){
    for(j<- 1 until x){
      pes(i)(j).io.in_a:=pes(i)(j-1).io.out_a
    }
  }
  for(i <- 0 until x){
    pes(0)(i).io.in_b:=b_input.io.data_out(i)
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
    transC.io.data_in(i).valid:=pes(s-1)(i).io.out_c.valid
    transC.io.data_in(i).bits:=pes(s-1)(i).io.out_c.bits
    
    //io.c_out(i).valid:=pes(s-1)(i).io.res_out.valid
  }
  for(i <- 0 until s*x/t){
    io.c_out(i).valid:=transC.io.data_out.valid
    io.c_out(i).bits:=transC.io.data_out.bits(i)
  }
  transC.io.data_out.ready:=true.B
}

class Test_Res1(c: Systolic_Rect) extends PeekPokeTester(c){
  val s=4
  val tt=16
  val b=4
  var steps=0
  val > = Array
  val r = scala.util.Random
  val matA = Array.tabulate(16, 4)((x, y)=>r.nextInt(10))
  val matB = Array.tabulate(2, 16, 4)((x, y, z)=>r.nextInt(10))
  var matC = Array.tabulate(2, 4, 4)((x, y, z)=> 0)
  for(i <- 0 until 16){
    println(matA(i).mkString(" "))
  }
  println()
  for(j <- 0 until 2)
  for(i <- 0 until 16){
    println(matB(j)(i).mkString(" "))
  }
  for(i <- 0 until 4){
    for(j <- 0 until 4){
      for(k <- 0 until 16){
        matC(0)(i)(j)=matC(0)(i)(j)+matA(k)(i)*matB(0)(k)(j)
        matC(1)(i)(j)=matC(1)(i)(j)+matA(k)(i)*matB(1)(k)(j)
      }
        
    }
  }
  for(i <- 0 until 2){
    for(j <- 0 until 4)
    println(matC(i)(j).mkString(" "))
    println()
  }

  for(j <- 0 until 2){
    for(i <- 0 until 64){
      poke(c.io.a_in.bits(0), matA(i/4)(i%4))
      poke(c.io.b_in.bits(0), matB(j)(i/4)(i%4))
      poke(c.io.a_in.valid, 1)
      poke(c.io.b_in.valid, 1)
      step(1)
      steps=steps+1
    }
  }
  for(j <- 0 until 4){
    for(i <- 0 until 64){
      poke(c.io.a_in.bits(0), matA(i/4)(i%4))
      poke(c.io.b_in.bits(0), 0)
      poke(c.io.a_in.valid, 1)
      poke(c.io.b_in.valid, 1)
      step(1)
      steps=steps+1
      print(peek(c.io.c_out(0).bits))
      println()
      print("step",steps)
      println()
    }
  }
  // output: C(1, 4), C(2, 4), C(3, 4), C(4, 4), C(1, 3), C(2, 3), C(3, 3), C(4, 3)...
}
object Test2 extends App {
  Driver(() => new Systolic_Rect(4, 4, 16, 4, 4, 1, 1, 16))(c => new Test_Res1(c))
  //chisel3.Driver.execute(args, () => new Systolic(16, 16, 256, 16, 1, 8, 32))
  //chisel3.Driver.execute(args, () => new Systolic_Rect(16, 10, 160, 16, 10, 1, 8, 32))
  //chisel3.Driver.execute(args, () => new Systolic_Rect(16, 8, 128, 16, 8, 1, 8, 32))
  //chisel3.Driver.execute(args, () => new NonSystolic(16, 16, 128, 16, 1, 8, 16))
}