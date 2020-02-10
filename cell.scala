package systolic

import chisel3._
import chisel3.util._
//import chisel3.Driver
import chisel3.iotesters.{PeekPokeTester, Driver}

class My_fmac extends BlackBox{
  val io = IO(new Bundle{
    val aclk = Input(Clock())
    val s_axis_a_tvalid = Input(Bool())
    val s_axis_a_tdata = Input(UInt(32.W))
    val s_axis_b_tvalid = Input(Bool())
    val s_axis_b_tdata = Input(UInt(32.W))
    val s_axis_c_tvalid = Input(Bool())
    val s_axis_c_tdata = Input(UInt(32.W))
    val m_axis_result_tvalid = Output(Bool())
    val m_axis_result_tdata = Output(UInt(32.W))
  })
}
class My_fadd extends BlackBox{
  val io = IO(new Bundle{
    val aclk = Input(Clock())
    val s_axis_a_tvalid = Input(Bool())
    val s_axis_a_tdata = Input(UInt(32.W))
    val s_axis_b_tvalid = Input(Bool())
    val s_axis_b_tdata = Input(UInt(32.W))
    val m_axis_result_tvalid = Output(Bool())
    val m_axis_result_tdata = Output(UInt(32.W))
  })
}
class My_fmul extends BlackBox{
  val io = IO(new Bundle{
    val aclk = Input(Clock())
    val s_axis_a_tvalid = Input(Bool())
    val s_axis_a_tdata = Input(UInt(32.W))
    val s_axis_b_tvalid = Input(Bool())
    val s_axis_b_tdata = Input(UInt(32.W))
    val m_axis_result_tvalid = Output(Bool())
    val m_axis_result_tdata = Output(UInt(32.W))
  })
}
class ComputeCellF(m: Int, n: Int, width: Int) extends Module {
    val io = IO(new Bundle {
        val in_a = Input(UInt((m*width).W))
        val in_b = Input(UInt((n*width).W))
        val in_c = Input(UInt((m*n*width).W))
        val out_c = Output(UInt((m*n*width).W))
    })
    val vec_a = Wire(Vec(m, UInt(width.W)))
    val vec_b = Wire(Vec(n, UInt(width.W)))
    val vec_c_in = Wire(Vec(m*n, UInt(width.W)))
    val vec_c_out = Wire(Vec(m*n, UInt(width.W)))
    val fmac = for(i <- 0 until m*n) yield Module(new My_fmac).io
    // val fadd = for(i <- 0 until m*n) yield Module(new My_fadd).io
    // val fmul = for(i <- 0 until m*n) yield Module(new My_fmul).io
    for(i <- 0 until m){
      vec_a(i):=io.in_a(i*width+width-1, i*width)
    }
    for(i <- 0 until n){
      vec_b(i):=io.in_b(i*width+width-1, i*width)
    }
    for(i <- 0 until m*n){
      vec_c_in(i):=io.in_c(i*width+width-1, i*width)
    }
    for(i <- 0 until m){
      for(j <- 0 until n){
        // fmul.clk = clock
        // fmul.s_axis_a_tvalid:=true.B
        // fmul.s_axis_b_tvalid:=true.B
        // fmul.s_axis_a_tdata:=vec_a(i)
        // fmul.s_axis_b_tdata:=vec_b(j)
        // fadd.s_axis_a_tdata:=fmul.m_axis_result_tdata
        // fadd.s_axis_a_tvalid:=fmul.m_axis_result_tvalid
        // fadd.s_axis_b_tdata:=vec()
        fmac(i*n+j).aclk := clock
        fmac(i*n+j).s_axis_a_tvalid:=true.B
        fmac(i*n+j).s_axis_b_tvalid:=true.B
        fmac(i*n+j).s_axis_a_tdata:=vec_a(i)
        fmac(i*n+j).s_axis_b_tdata:=vec_b(j)
        fmac(i*n+j).s_axis_c_tvalid:=true.B
        fmac(i*n+j).s_axis_c_tdata:=vec_c_in(i*n+j)
        vec_c_out(i*n+j):=fmac(i*n+j).m_axis_result_tdata
      }
    }
    io.out_c := vec_c_out.asUInt
    //printf("%x %x %x %x %x %x\n", io.in_b, vec_b(0), vec_b(1), vec_c_out(0), vec_c_out(1), io.out_c)
}

class ComputeCell(m: Int, n: Int, width: Int) extends Module {
    val io = IO(new Bundle {
        val in_a = Input(UInt((m*width).W))
        val in_b = Input(UInt((n*width).W))
        val in_c = Input(UInt((m*n*width).W))
        val out_c = Output(UInt((m*n*width).W))
    })
    val vec_a = Wire(Vec(m, UInt(width.W)))
    val vec_b = Wire(Vec(n, UInt(width.W)))
    val vec_c_in = Wire(Vec(m*n, UInt(width.W)))
    val vec_c_out = Wire(Vec(m*n, UInt(width.W)))
    for(i <- 0 until m){
      vec_a(i):=io.in_a(i*width+width-1, i*width)
    }
    for(i <- 0 until n){
      vec_b(i):=io.in_b(i*width+width-1, i*width)
    }
    for(i <- 0 until m*n){
      vec_c_in(i):=io.in_c(i*width+width-1, i*width)
    }
    for(i <- 0 until m){
      for(j <- 0 until n){
        vec_c_out(i*n+j):=vec_a(i)*vec_b(j)+vec_c_in(i*n+j)
      }
    }
    io.out_c := vec_c_out.asUInt
    //printf("%x %x %x %x %x %x\n", io.in_b, vec_b(0), vec_b(1), vec_c_out(0), vec_c_out(1), io.out_c)
}


