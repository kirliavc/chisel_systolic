package systolic

import chisel3._
import chisel3.util._
//import chisel3.Driver
import chisel3.iotesters.{PeekPokeTester, Driver}

class MicArch extends Module {
  val io = IO(new Bundle {
    val in_row = Input(UInt(2.W))
    val in_column = Input(UInt(2.W))
    val p = Output(UInt(4.W))
  })
  val lo = Wire(UInt(4.W))
  val hi = Wire(UInt(4.W))
  when(io.in_column(0).toBool){
    lo := io.in_row
  }.otherwise {
    lo := 0.U
  }
  when(io.in_column(1).toBool) {
    hi := io.in_row << 1
  }.otherwise {
    hi := 0.U
  }
  io.p := lo + hi
}
class Addition(row_width:Int,col_width:Int,out_p_width:Int) extends Module{
    val io = IO(new Bundle{
      val row = Input(UInt(row_width.W))
      val column = Input(UInt(col_width.W))
      val sgn = Input(UInt(1.W))
      val out = Output(UInt(out_p_width.W))
    })
    val row_extend_sgn = Wire(UInt(1.W))
    val col_extend_sgn = Wire(UInt(1.W))
    row_extend_sgn := io.sgn & io.row(row_width-1)
    col_extend_sgn := io.sgn & io.column(col_width-1)
    val row_offset = Wire(UInt(out_p_width.W))
    val col_offset = Wire(UInt(out_p_width.W))
    row_offset := Mux(col_extend_sgn.toBool, io.row << col_width, 0.U)
    col_offset := Mux(row_extend_sgn.toBool, io.column << row_width, 0.U)
    io.out := ~(row_offset + col_offset) + 1.U
}
object Addition{
  def apply(row:UInt,column:UInt,sgn:UInt,row_width:Int,col_width:Int,out_p_width:Int) = {
    val m = Module(new Addition(row_width,col_width,out_p_width))
    m.io.row := row
    m.io.column := column
    m.io.sgn := sgn
    m.io.out
  }
}
class Unit4 extends Module {
  val io = IO(new Bundle {
          val in_row = Input(UInt(4.W))
          val in_column = Input(UInt(4.W))
          val sgn = Input(UInt(1.W))
          val out_p = Output(UInt(8.W))
          val res_in = Input(UInt(8.W))
          val res_out = Output(UInt(8.W))
  })
   def inject_data(in_row:UInt,in_column:UInt,sgn:UInt) {
     io.in_row := in_row
     io.in_column := in_column
     io.sgn := sgn
   }
  val row = Wire(UInt(4.W))
  val column = Wire(UInt(4.W))
  val micArch = VecInit(Seq.fill(4){Module(new MicArch()).io})
  row := io.in_row
  column := io.in_column
  val addition = Wire(UInt(8.W))
  addition := Addition(row,column,io.sgn,4,4,8)
  for (i <- 0 until 2)
      for (j <- 0 until 2) {
          micArch(i*2+j).in_row := row(3-2*i,2-2*i)
          micArch(i*2+j).in_column := column(3-2*j,2-2*j)
      }
  val p_sub = Wire(Vec(4,UInt(4.W)))
  val p_total = Wire(UInt(8.W))
  for (i <- 0 until 4)
      p_sub(i) := micArch(i).p
  p_total := (p_sub(0)<<4) + (p_sub(1)<<2) + (p_sub(2)<<2) + p_sub(3)
  io.res_out := io.res_in + p_total + addition
  io.out_p := p_total
}
class Bridge(in_p_width:Int,row_width:Int,col_width:Int,p_shift:Int)/*[]-[]*/ extends Module {
    val out_p_width = row_width + col_width
    val res_width = if (out_p_width < 16) 16 else 32
    val io = IO(new Bundle{
        val pl = Input(UInt(in_p_width.W))
        val pr = Input(UInt(in_p_width.W))
        val in_row = Input(UInt(row_width.W))
        val in_column = Input(UInt(col_width.W))
        val sgn = Input(UInt(1.W))
        val out_p = Output(UInt(out_p_width.W))
        val res_in = Input(UInt(res_width.W))
        val res_out = Output(UInt(res_width.W))
    })
    def inject_data(pl:UInt,pr:UInt,in_row:UInt,in_column:UInt,sgn:UInt) {
      io.pl := pl
      io.pr := pr
      io.in_row := in_row
      io.in_column := in_column
      io.sgn := sgn
    }
    val pl = RegInit(0.U(in_p_width.W))
    val pr = RegInit(0.U(in_p_width.W))
    val row = Wire(UInt(row_width.W))
    val column = Wire(UInt(col_width.W))
    pl := io.pl
    pr := io.pr
    row := io.in_row
    column := io.in_column
    val addition = Wire(UInt(out_p_width.W))
    addition := Addition(row,column,io.sgn,row_width,col_width,out_p_width)

    val p_total = Wire(UInt(out_p_width.W))
    p_total := (pl<<p_shift) + pr

    val sum = Wire(UInt(out_p_width.W))
    sum := p_total + addition
    if(out_p_width == res_width) {
        io.res_out := io.res_in + sum
    }else {
        val extend = Wire(UInt(res_width.W))
        extend := Cat(Fill(res_width-out_p_width,sum(out_p_width-1)&io.sgn),sum).asUInt
        io.res_out := io.res_in + extend
    }
    io.out_p := p_total
}

class DynamicPE extends Module {
    val io = IO(new Bundle {
        val ctrl = Input(UInt(3.W))
        val in_row = Input(UInt(16.W))
        val in_column = Input(UInt(16.W))
        val sgn = Input(UInt(1.W))
        val out_row = Output(UInt(16.W))
        val out_column = Output(UInt(16.W))
        val statC_in = Input(UInt(128.W))
        val statC_out = Output(UInt(128.W))
    })
    val rows = RegInit(VecInit(Seq.fill(5)(0.U(16.W))))
    val columns = RegInit(VecInit(Seq.fill(5)(0.U(16.W))))
    for (i <- 0 until 5) {
        if (i == 0) {
            rows(i) := io.in_row
            columns(i) := io.in_column
        } else {
            rows(i) := rows(i-1)
            columns(i) := columns(i-1)
        }
    }
    val Unit4 = for(i <- 0 until 4) yield {
      for(j <- 0 until 4) yield
        Module(new Unit4())
    }
    for (i <- 0 until 4)
    for (j <- 0 until 4)
      Unit4(i)(j).inject_data(rows(0)(15-i*4,12-i*4), columns(0)(15-j*4,12-j*4), io.sgn)

    val Bridge2 = for(i <- 0 until 4) yield {
      for(j <- 0 until 2) yield
        Module(new Bridge(8,4,8,4))
    }
    for(i <- 0 until 4)
    for(j <- 0 until 2)
      Bridge2(i)(j).inject_data(Unit4(i)(j*2).io.out_p,Unit4(i)(j*2+1).io.out_p,rows(1)(15-i*4,12-i*4),columns(1)(15-j*8,8-j*8),io.sgn)

    val Bridge4_row = for(i <- 0 until 4) yield
      Module(new Bridge(12,4,16,8))
    for(i <- 0 until 4)
      Bridge4_row(i).inject_data(Bridge2(i)(0).io.out_p,Bridge2(i)(1).io.out_p,rows(2)(15-i*4,12-i*4),columns(2),io.sgn)
    val Bridge4_col = for(i <- 0 until 2) yield
      for(j <- 0 until 2) yield
          Module(new Bridge(12,8,8,4))
    for(i <- 0 until 2)
    for(j <- 0 until 2)
      Bridge4_col(i)(j).inject_data(Bridge2(2*i)(j).io.out_p,Bridge2(2*i+1)(j).io.out_p,rows(2)(15-i*8,8-i*8),columns(2)(15-j*8,8-j*8),io.sgn)

    val Bridge8 = for(i <- 0 until 2) yield
      Module(new Bridge(16,8,16,8))
    for(i <- 0 until 2) yield
      Bridge8(i).inject_data(Bridge4_col(i)(0).io.out_p, Bridge4_col(i)(1).io.out_p, rows(3)(15-i*8,8-i*8), columns(3), io.sgn)
    val Bridge16 = Module(new Bridge(24,16,16,8))
    Bridge16.inject_data(Bridge8(0).io.out_p,Bridge8(1).io.out_p,rows(4),columns(4),io.sgn)         
    io.out_row := rows(0)
    io.out_column := columns(0)
    for(i <- 0 until 4)
    for(j <- 0 until 4) {
        val tmp = 8 * (4 * i + j)
        Unit4(i)(j).io.res_in := io.statC_in(7+tmp,tmp)
    }
    for(i <- 0 until 4)
    for(j <- 0 until 2) {
        val tmp = 16 * (2 * i + j)
        Bridge2(i)(j).io.res_in := io.statC_in(15+tmp,tmp)
    }   //1 cycle
    for(i <- 0 until 4) {
        val tmp = 32 * i
        Bridge4_row(i).io.res_in := io.statC_in(31+tmp,tmp)
    }
    for(i <- 0 until 2)
    for(j <- 0 until 2) {
        val tmp = 32 * (2* i + j)
        Bridge4_col(i)(j).io.res_in := io.statC_in(31+tmp,tmp)
    }   //2 cycle
    for(i <- 0 until 2) {
        val tmp = 32 * i
        Bridge8(i).io.res_in := io.statC_in(31+tmp,tmp)
    }   //3 cycle
    Bridge16.io.res_in := io.statC_in(31,0)
    //  4 cycle
    var res404_out = Wire(Vec(16,UInt(8.W)))
    var res408_out = Wire(Vec(8,UInt(16.W)))
    var res4016_out = Wire(Vec(4,UInt(32.W)))
    var res808_out = Wire(Vec(4,UInt(32.W)))
    var res8016_out = Wire(Vec(2,UInt(32.W)))
    var res16016_out = Wire(UInt(32.W))
    for(i <- 0 until 4)
        for(j <- 0 until 4)
    res404_out(i*4+j) := Unit4(i)(j).io.res_out
    for(i <- 0 until 4)
        for(j <- 0 until 2)
    res408_out(i*2+j) := Bridge2(i)(j).io.res_out
    for(i <- 0 until 4)
    res4016_out(i) := Bridge4_row(i).io.res_out
    for(i <- 0 until 2)
        for(j <- 0 until 2)
    res808_out(i*2+j) := Bridge4_col(i)(j).io.res_out
    for(i <- 0 until 2)
    res8016_out(i) := Bridge8(i).io.res_out
    res16016_out := Bridge16.io.res_out
    io.statC_out := MuxLookup(io.ctrl, 0.U, Array(1.U -> res404_out.asUInt, 2.U -> res408_out.asUInt, 3.U -> res4016_out.asUInt, 4.U -> res808_out.asUInt, 5.U -> res8016_out.asUInt, 6.U -> res16016_out.asUInt))
}

class TestClosure extends Module{
    val io = IO(new Bundle {
        val ctrl = Input(UInt(3.W))
        val in_row = Input(UInt(16.W))
        val in_column = Input(UInt(16.W))
        val sgn = Input(UInt(1.W))
        val result = Output(UInt(128.W))
    })
    val PEio = Module(new DynamicPE).io
    val statC = RegInit(0.U(128.W))
    PEio.ctrl := io.ctrl
    PEio.in_row := io.in_row
    PEio.in_column := io.in_column
    PEio.sgn := io.sgn
    PEio.statC_in := statC
    statC := PEio.statC_out
    io.result := statC
}
