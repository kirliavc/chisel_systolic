package systolic

import chisel3._
import chisel3.util._
//import chisel3.Driver
import chisel3.iotesters.{PeekPokeTester, Driver}

// A = weight B = input C = output
// class StatIn(id: Int, dim: Int, m: Int, n: Int, width: Int) extends Module{
//   val a_width = m*width
//   val io = IO(new Bundle{
//     val stage_cycle = Input(UInt(24.W))
//     val in_valid = Input(Bool())
//     val in_b_ready = Output(Bool())
//     val in_a_ready = Output(Bool())
//     val from_io = Input(Valid(UInt(a_width.W)))
//     val to_io = Output(Valid(UInt(a_width.W)))
//     val to_cell = Output(Valid(UInt(a_width.W)))
//   })
//   val in_valid = RegInit(false.B)
//   in_valid := io.in_valid
//   val trans_A = RegInit(0.U.asTypeOf(Valid(UInt((a_width).W))))
//   val stat_A = RegInit(0.U.asTypeOf(Valid(UInt((a_width).W))))
//   val cycle = RegInit(0.U(10.W))
//   val skip_cycle = RegInit(0.U(10.W))
//   val buf_rem = RegInit(0.U(2.W))
//   val buf_produce = Wire(UInt(2.W))
//   val buf_consume = Wire(UInt(2.W))
//   val input_ready = RegInit(false.B)
//   //printf("%d %d\n",stat_C, trans_C)
//   cycle:=Mux(cycle+in_valid===io.stage_cycle, 0.U, cycle+in_valid)
//   io.to_cell := stat_A
//   io.to_io:=trans_A
//   // 
//   when(cycle===0.U){
//     stat_A := trans_A
//     when(buf_rem > 0.U){  // data is valid in trans
//       buf_consume := 1.U
//       input_ready := true.B
//     }.otherwise{
//       input_ready := false.B
//     }
//   }
//   when(io.from_io.valid){
//     skip_cycle := Mux(skip_cycle===(dim-1).asUInt,0.U, skip_cycle+1.U)
//   }
//   when(skip_cycle<(dim-id).asUInt){
//     trans_A := io.from_io
//     when(skip_cycle===(dim-id-1).asUInt){
//       buf_produce := 1.U
//     }
//   }
//   buf_rem := buf_rem + buf_produce - buf_consume
//   // when filter stays in PE, input is ready to fill in PE
//   io.in_a_ready := (buf_rem===0.U)
//   io.in_b_ready := input_ready
// }
// class WSPE(id: Int, dim: Int, m: Int, n: Int, width: Int) extends Module{
//     val io = IO(new Bundle {
//       val in_stage_cycle = Input(UInt(24.W))
//       val out_stage_cycle = Output(UInt(24.W))
//       val in_a = DeqIO(UInt((m*width).W))
//       val in_b = DeqIO(UInt((n*width).W))
//       val in_c = Input(Valid(UInt((m*n*width).W)))
//       val out_c = Output(Valid(UInt((m*n*width).W)))
//       val out_a = EnqIO(UInt((m*width).W))
//       val out_b = EnqIO(UInt((n*width).W))
//     })
//     val stage_cycle = RegInit(100.U(20.W))  
//     val reg_b = RegInit(0.U.asTypeOf(Valid(UInt((n*width).W))))
//     val reg_c = RegInit(0.U.asTypeOf(Valid(UInt((m*n*width).W))))
//     val pe = Module(new ComputeCell(m, n, width)).io
//     val trans_a = RegInit(0.U.asTypeOf(Valid(UInt((m*width).W))))
//     val stat_a = RegInit(0.U.asTypeOf(Valid(UInt((m*width).W))))
//     val exec_cycle = RegInit(0.U(20.W))
//     val input_cycle = RegInit(0.U(20.W))
//     val buf_rem = RegInit(0.U(2.W))
//     val b_ready = RegInit(false.B)
//     val buf_produce = Wire(UInt(2.W))
//     val buf_consume = Wire(UInt(2.W))
//     //val stat = Module(new StatIn(id, dim, m, n, width)).io
//     stage_cycle := io.in_stage_cycle
//     io.out_stage_cycle := stage_cycle

//     exec_cycle:=Mux(exec_cycle+(io.in_b.valid& io.in_b.ready)===stage_cycle, 0.U, exec_cycle+(io.in_b.valid& io.in_b.ready))
    
//     when(io.in_b.ready){
//       reg_b.bits := io.in_b.bits
//       reg_b.valid := io.in_b.valid
//     }
//     when(io.in_a.ready){
//       trans_a.bits := io.in_a.bits
//       trans_a.valid := io.in_a.valid
//       when(io.in_a.valid){
//         input_cycle := Mux(input_cycle===(dim-1).asUInt,0.U, input_cycle+1.U)
//         when(input_cycle===(dim-id-1).asUInt){
//           buf_produce := 1.U
//         }.otherwise{
//           buf_produce := 0.U
//         }
//       }.otherwise{
//         buf_produce := 0.U
//       }
//     }.otherwise{
//       buf_produce := 0.U 
//     }
//     when(exec_cycle===0.U){
//       stat_a := trans_a
//       when(buf_rem > 0.U){  // data is valid in trans
//         buf_consume := 1.U
//         b_ready := true.B
//       }.otherwise{
//         buf_consume := 0.U
//         b_ready := false.B
//       }
//     }.otherwise{
//       buf_consume := 0.U
//     }
//     //io.out_a := stat.to_io
//     pe.in_a := stat_a.bits
//     when(reg_b.valid){
//       pe.in_b := reg_b.bits
//     }.otherwise{
//       pe.in_b := 0.U
//     }
//     reg_c.valid := io.in_b.valid
//     reg_c.bits := pe.out_c
//     io.out_c.bits := reg_c.bits
//     io.out_c.valid := reg_c.valid
//     pe.in_c := io.in_c.bits
//     io.out_b.bits := reg_b.bits
//     io.out_b.valid := reg_b.valid
//     buf_rem := buf_rem + buf_produce - buf_consume
//     io.in_a.ready := (buf_rem === 0.U) && (input_cycle<(dim-id).asUInt)
//     io.in_b.ready := b_ready
//     io.out_a.bits := trans_a.bits
//     io.out_a.valid := trans_a.valid
// }
class WSPE2(id: Int, dim: Int, m: Int, n: Int, width: Int) extends Module{
    val io = IO(new Bundle {
      val in_stage_cycle = Input(UInt(10.W))
      val out_stage_cycle = Output(UInt(10.W))
      val in_a = Input(Valid(UInt((m*width).W)))
      val in_b = Input(Valid(UInt((n*width).W)))
      val in_c = Input(Valid(UInt((m*n*width).W)))
      val out_c = Output(Valid(UInt((m*n*width).W)))
      val out_a = Output(Valid(UInt((m*width).W)))
      val out_b = Output(Valid(UInt((n*width).W)))
      val input_cycle = Output(UInt(10.W))
      val a_stat = Output(UInt((m*width).W))
      val exec_cycle = Output(UInt(10.W))
    })
    val stage_cycle = RegInit(1.U(10.W))  
    val reg_b = RegInit(0.U.asTypeOf(Valid(UInt((n*width).W))))
    val reg_c = RegInit(0.U.asTypeOf(Valid(UInt((m*n*width).W))))
    val pe = Module(new ComputeCell(m, n, width)).io
    val trans_a = RegInit(0.U.asTypeOf(Valid(UInt((m*width).W))))
    val stat_a = RegInit(0.U.asTypeOf(Valid(UInt((m*width).W))))
    val exec_cycle = RegInit(0.U(10.W))
    val input_cycle = RegInit(0.U(10.W))
    val input_a_valid = RegInit(false.B)
    input_a_valid := io.in_a.valid
    //val stat = Module(new StatIn(id, dim, m, n, width)).io
    stage_cycle := io.in_stage_cycle
    io.out_stage_cycle := stage_cycle

    exec_cycle:=Mux(exec_cycle+(io.in_b.valid)===stage_cycle, 0.U, exec_cycle+(io.in_b.valid))
    

    // 对于input，照单全收，由整体的controller控制
    reg_b.bits := io.in_b.bits
    reg_b.valid := io.in_b.valid

    // 对于filter，第0个PE收dim个，留最后一个，其余的传出去
    // 第dim-1个PE收1个，其余的不要
    when(input_cycle < (dim-id).asUInt && io.in_a.valid){
      trans_a.bits := io.in_a.bits
      trans_a.valid := io.in_a.valid
    }
    // 一直更新
    when(io.in_a.valid){
      input_cycle := Mux(input_cycle===(dim-1).asUInt,0.U, input_cycle+1.U)
    }

    when(exec_cycle===0.U){
      stat_a := trans_a
    }
    //io.out_a := stat.to_io
    pe.in_a := stat_a.bits
    when(reg_b.valid){
      pe.in_b := reg_b.bits
    }.otherwise{
      pe.in_b := 0.U
    }
    reg_c.valid := io.in_c.valid
    reg_c.bits := io.in_c.bits
    io.out_c.bits := pe.out_c
    io.out_c.valid := reg_b.valid
    pe.in_c := reg_c.bits
    io.out_b.bits := reg_b.bits
    io.out_b.valid := reg_b.valid
    io.out_a.bits := trans_a.bits
    io.out_a.valid := input_a_valid
    io.input_cycle := input_cycle
    io.a_stat := stat_a.bits
    io.exec_cycle := exec_cycle
}

class WSSystolic_Test(in_channel: Int, out_channel: Int, in_slot_num: Int, ker_slot_num: Int, cycle_read_kernel: Int, cycle_read_input: Int, cycle_out_res: Int, max_ks: Int, max_w: Int, batch: Int, width: Int) extends Module{
  val io = IO(new Bundle{
    val inst = DeqIO(new RoCCInstruction()) //指令输入
    val a_in = DeqIO(Vec(cycle_read_kernel, UInt((width).W))) //数据输入
    val b_in = DeqIO(Vec(cycle_read_input, UInt((batch*width).W)))
    val c_out = Output(Valid(Vec(cycle_out_res, UInt((batch*width).W))))
  })
  // val io = IO(new Bundle{
  //   val a_in = Vec(8, Valid(UInt(8.W)))
  //   val b_in = Vec(8, Valid(UInt(8.W)))
  //   val c_out = Vec(8, Valid(UInt(8.W)))
  // })
  // stage cycle=24, cur cycle=8
  val in_a_valid = RegInit(VecInit(Seq.fill(in_channel)(false.B)))
  val in_b_valid = RegInit(VecInit(Seq.fill(in_channel)(false.B)))
  val total_cycle = RegInit(1000.U(10.W))
  val exec_cycle = RegInit(0.U(10.W))
  val ids = Module(new InstDispatcher()).io
  val a_input = Module(new WSSysIn_Kernel(in_channel, out_channel, ker_slot_num, max_ks * max_ks * in_channel, in_channel, width))
  val b_input = Module(new WSSysIn_Input(in_channel, in_slot_num, max_w, cycle_read_input, batch * width))
  val c_output = Module(new Update_Result(out_channel, in_slot_num, max_w, out_channel, batch*width))
  // 一开始的in_channel个cycle，输入filter到PE中，接下来的ks*ks*out_w个cycle用于计算。
  printf("total cycle=%d, exec_cycle=%d, ks=%d, w=%d, conv_exec.valid=%d\n", total_cycle, exec_cycle, ids.config.ks, ids.config.out_w, ids.conv_exec.valid)
  printf("kernel buffer to PE\n")
  for(i <- 0 until cycle_read_kernel){
    printf("(%d, %d)",a_input.io.data_out.bits(i).bits,a_input.io.data_out.bits(i).valid)
  }
  printf("\n")
  printf("input buffer to PE\n")
  for(i <- 0 until cycle_read_input){
    printf("(%d, %d)",b_input.io.data_out.bits(i).bits,b_input.io.data_out.bits(i).valid)
  }
  printf("PE to output buffer\n")
  for(i <- 0 until cycle_read_input){
    printf("(%d, %d)",c_output.io.data_in(i).bits,c_output.io.data_in(i).valid)
  }
  printf("\n")
  total_cycle := ids.config.ks * ids.config.ks * ids.config.out_w + in_channel.asUInt
  ids.inst <> io.inst


  a_input.io.in_inst <> ids.wr_filter
  a_input.io.out_inst.bits.id := ids.conv_exec.bits.filter_id
  a_input.io.out_inst.valid := ids.conv_exec.valid
  a_input.io.data_in.valid := io.a_in.valid
  for(i <- 0 until cycle_read_kernel){
    a_input.io.data_in.bits(i).bits := io.a_in.bits(i)
    a_input.io.data_in.bits(i).valid := io.a_in.valid
  }
  b_input.io.in_inst <> ids.wr_input
  b_input.io.out_inst.bits.id := ids.conv_exec.bits.input_id
  b_input.io.out_inst.valid := ids.conv_exec.valid
  b_input.io.data_in.valid := io.b_in.valid
  for(i <- 0 until cycle_read_input){
    b_input.io.data_in.bits(i).bits := io.b_in.bits(i)
    b_input.io.data_in.bits(i).valid := io.b_in.valid
  }
  c_output.io.in_inst.bits.id := ids.conv_exec.bits.output_id
  c_output.io.in_inst.valid := ids.conv_exec.valid
  c_output.io.out_inst <> ids.rd_output
  io.c_out.valid := c_output.io.data_out.valid
  ids.conv_exec.ready := (exec_cycle===total_cycle-1.U) //最后一个cycle，conv指令结束，ready置为真，允许下一条指令进来
  io.a_in.ready := a_input.io.data_in.ready
  io.b_in.ready := b_input.io.data_in.ready
  // filter的输入：每out_w个cycle，前in_channel个cycle输入，共输入ks*ks次
  a_input.io.data_out.ready:=(exec_cycle%ids.config.out_w < in_channel.asUInt && exec_cycle < total_cycle - in_channel.asUInt)
  // input的输入：前in_channel个cycle不输入，之后一直输入
  b_input.io.data_out.ready:=(exec_cycle >= in_channel.asUInt)
  a_input.io.config:=ids.config
  b_input.io.config:=ids.config
  c_output.io.config:=ids.config
  a_input.io.data_in.valid:=io.a_in.valid
  b_input.io.data_in.valid:=io.b_in.valid
  io.c_out:=c_output.io.data_out

  //如果input buffer的输出指令和filter buffer的输出指令均有效，则视为开始计算
  //exec_cycle对应的是0号PE的cycle情况
  exec_cycle := (exec_cycle + ids.conv_exec.valid)%total_cycle
  for(i <- 1 until in_channel){
    in_a_valid(i) := in_a_valid(i-1)
    in_b_valid(i) := in_b_valid(i-1)
  }
  val pes = for(i <- 0 until in_channel) yield{
    for(j <- 0 until out_channel) yield{
      Module(new WSPE2(j, in_channel, 1, batch, width)).io
    }
  }
  for(i <- 0 until in_channel){
    for(j <- 1 until out_channel){
      pes(i)(j).in_a <> pes(i)(j-1).out_a
      pes(i)(j).in_c <> pes(i)(j-1).out_c
      pes(i)(j).in_stage_cycle := pes(i)(j-1).out_stage_cycle
    }
  }
  for(i <- 1 until in_channel){
    for(j <- 0 until out_channel){
      pes(i)(j).in_b <> pes(i-1)(j).out_b
    }
  }
  for(i <- 0 until in_channel){
    pes(i)(0).in_stage_cycle := ids.config.out_w
    pes(i)(0).in_c.bits := 0.U
    pes(i)(0).in_c.valid := true.B
    pes(i)(0).in_a.bits := a_input.io.data_out.bits(i).bits
    pes(i)(0).in_a.valid := a_input.io.data_out.bits(i).valid
    c_output.io.data_in(i).bits := pes(i)(in_channel-1).out_c.bits
    c_output.io.data_in(i).valid := pes(i)(in_channel-1).out_c.valid
  }
  for(i <- 0 until in_channel){
    pes(0)(i).in_b.bits := b_input.io.data_out.bits(i).bits
    pes(0)(i).in_b.valid := b_input.io.data_out.bits(i).valid
  }
  for(i <- 0 until in_channel){
    for(j <- 0 until in_channel){
      printf("(%d %d %d) ", pes(i)(j).out_a.bits, pes(i)(j).out_b.bits, pes(i)(j).out_c.bits)
      //printf("(a:%d, %d b:%d, %d c:%d, %d) ", pes(i)(j).out_a.bits, pes(i)(j).out_a.valid, pes(i)(j).out_b.bits,pes(i)(j).out_b.valid, pes(i)(j).out_c.bits, pes(i)(j).out_c.valid)
    }
    printf("\n")
    
  }
  printf("\n")
}

// class WSSystolic(in_channel: Int, out_channel: Int, in_slot_num: Int, ker_slot_num: Int, cycle_read_kernel: Int, cycle_read_input: Int, cycle_out_res: Int, max_ks: Int, max_w: Int, batch: Int, width: Int) extends Module{
//   val io = IO(new Bundle{
//     val inst = DeqIO(new RoCCInstruction()) //指令输入
//     val a_in = DeqIO(Vec(cycle_read_kernel, UInt((width).W))) //数据输入
//     val b_in = DeqIO(Vec(cycle_read_input, UInt((batch*width).W)))
//     val c_out = Output(Valid(Vec(cycle_out_res, UInt((batch*width).W))))
//   })
//   // val io = IO(new Bundle{
//   //   val a_in = Vec(8, Valid(UInt(8.W)))
//   //   val b_in = Vec(8, Valid(UInt(8.W)))
//   //   val c_out = Vec(8, Valid(UInt(8.W)))
//   // })
//   // stage cycle=24, cur cycle=8
//   val in_a_valid = RegInit(VecInit(Seq.fill(8)(false.B)))
//   val in_b_valid = RegInit(VecInit(Seq.fill(8)(false.B)))
//   val exec_cycle = RegInit(0.U(10.W))
//   val ids = Module(new InstDispatcher()).io
//   val a_input = Module(new WSSysIn_Kernel(in_channel: in_channel, out_channel: out_channel, slot_num: ker_slot_num, slot_size: max_ks * max_ks * in_channel, cycle_read: Int, width: Int))
//   val b_input = Module(new WSSysIn_Input(pe_num: in_channel, slot_num: in_slot_num, slot_size: max_w * batch, cycle_read: cycle_read_input, width: Int))
//   val c_output = Module(new Update_Result(x, s, max_input_h, max_input_w/s, cycle_out_res, m*n*width))
//   val b_wait_cycle = RegInit(0.U(10.W))
//   when(ids.rd_input.ready && ids.rd_input.valid){
//     b_wait_cycle := in_channel.asUInt - 1.U
//   }.otherwise{
//     b_wait_cycle := Mux(b_wait_cycle===0.U, 0.U, b_wait_cycle - ids.rd_input.valid)
//   }
//   val a_input_cycle = RegInit(0.U(10.W))
//   when(ids.rd_filter.ready && ids.rd_filter.valid){
//     a_input_cycle := 0.U
//   }.otherwise{
//     a_input_cycle := Mux(a_input_cycle + ids.rd_filter.valid===ids.config.out_w, 0.U, a_input_cycle + ids.rd_filter.valid)
//   }
//   a_input.io.in_inst <> ids.wr_filter
//   a_input.io.out_inst <> ids.rd_filter
//   b_input.io.in_inst <> ids.wr_input
//   b_input.io.out_inst <> ids.rd_input
//   c_output.io.in_inst <> ids.wr_output
//   c_output.io.out_inst <> ids.rd_output
//   io.a_in.ready := a_input.io.data_in.ready
//   io.b_in.ready := b_input.io.data_in.ready
//   a_input.io.data_out.ready:=(a_input_cycle < in_channel)
//   b_input.io.data_out.ready:=(b_wait_cycle===0.U)
//   a_input.io.config:=ids.config
//   b_input.io.config:=ids.config
//   c_output.io.config:=ids.config
//   a_input.io.data_in.valid:=io.a_in.valid
//   b_input.io.data_in.valid:=io.b_in.valid
//   io.c_out:=c_output.io.data_out

//   //如果input buffer的输出指令和filter buffer的输出指令均有效，则视为开始计算
//   //exec_cycle对应的是0号PE的cycle情况
//   exec_cycle := (exec_cycle + ids.rd_input.valid && ids.rd_filter.valid)%ids.config.out_w
//   when(exec_cycle < input_){
//     a_input.io.data_out.ready
//   }.otherwise{
//     in_a_valid(0) := 0.U
//   }
//   for(i <- 1 until 8){
//     in_a_valid(i) := in_a_valid(i-1)
//     in_b_valid(i) := in_b_valid(i-1)
//   }
//   val pes = for(i <- 0 until 8) yield{
//     for(j <- 0 until 8) yield{
//       Module(new WSPE2(j, 16, 1, 1, 16)).io
//     }
//   }
//   for(i <- 0 until 8){
//     for(j <- 1 until 8){
//       pes(i)(j).in_a <> pes(i)(j-1).out_a
//       pes(i)(j).in_c <> pes(i)(j-1).out_c
//       pes(i)(j).in_stage_cycle := pes(i)(j-1).out_stage_cycle
//     }
//   }
//   for(i <- 1 until 8){
//     for(j <- 0 until 8){
//       pes(i)(j).in_b <> pes(i-1)(j).out_b
//     }
//   }
//   for(i <- 0 until 8){
//     pes(i)(0).in_stage_cycle := 24.U
//     pes(i)(0).in_c.bits := 0.U
//     pes(i)(0).in_c.valid := true.B
//     pes(i)(0).in_a.bits := cur_data
//     pes(i)(0).in_a.valid := in_a_valid(i)
//   }
//   for(i <- 0 until 8){
//     pes(0)(i).in_b.bits := 1.U
//     pes(0)(i).in_b.valid := in_b_valid(i)
//   }
//   for(i <- 0 until 8){
//     for(j <- 0 until 8){
//       printf("(%d %d %d) ", pes(i)(j).out_a.bits, pes(i)(j).out_b.bits, pes(i)(j).out_c.bits)
//       //printf("(a:%d, %d b:%d, %d c:%d, %d) ", pes(i)(j).out_a.bits, pes(i)(j).out_a.valid, pes(i)(j).out_b.bits,pes(i)(j).out_b.valid, pes(i)(j).out_c.bits, pes(i)(j).out_c.valid)
//     }
//     printf("\n")
    
//   }
//   printf("\n")
// }

// class WSPE_BitFusion(id: Int, dim: Int, m: Int, n: Int, width: Int) extends Module{
//     val io = IO(new Bundle {
//       val in_stage_cycle = Input(UInt(10.W))
//       val out_stage_cycle = Output(UInt(10.W))
//       val in_a = Input(Valid(UInt((m*width).W)))
//       val in_b = Input(Valid(UInt((n*width).W)))
//       val in_c = Input(Valid(UInt(128.W)))
//       val out_c = Output(Valid(UInt(128.W)))
//       val out_a = Output(Valid(UInt((m*width).W)))
//       val out_b = Output(Valid(UInt((n*width).W)))
//     })
//     val stage_cycle = RegInit(1.U(10.W))  
//     val reg_b = RegInit(0.U.asTypeOf(Valid(UInt((n*width).W))))
//     val reg_c = RegInit(0.U.asTypeOf(Valid(UInt((128).W))))
//     val pe = Module(new DynamicPE_WS()).io
    

//     val trans_a = RegInit(0.U.asTypeOf(Valid(UInt((m*width).W))))
//     val stat_a = RegInit(0.U.asTypeOf(Valid(UInt((m*width).W))))
//     val exec_cycle = RegInit(0.U(10.W))
//     val input_cycle = RegInit(0.U(10.W))

//     pe.ctrl := 4.U
//     pe.sgn := 1.U
//     pe.statC_in := io.in_c.bits
//     reg_c.bits := pe.statC_out
//     pe.in_row := stat_a.bits
//     pe.in_column := Mux(reg_b.valid, reg_b.bits, 0.U)
//     //val stat = Module(new StatIn(id, dim, m, n, width)).io
//     stage_cycle := io.in_stage_cycle
//     io.out_stage_cycle := stage_cycle

//     exec_cycle:=Mux(exec_cycle+(io.in_b.valid)===stage_cycle, 0.U, exec_cycle+(io.in_b.valid))
    

//     // 对于input，照单全收，由整体的controller控制
//     reg_b.bits := io.in_b.bits
//     reg_b.valid := io.in_b.valid
//     trans_a.bits := io.in_a.bits
//     trans_a.valid := io.in_a.valid
//     when(io.in_a.valid){
//       input_cycle := Mux(input_cycle===(dim-1).asUInt,0.U, input_cycle+1.U)
//     }

//     when(exec_cycle===0.U){
//       stat_a := trans_a
//     }
//     //io.out_a := stat.to_io
//     // pe.in_a := stat_a.bits
//     // when(reg_b.valid){
//     //   pe.in_b := reg_b.bits
//     // }.otherwise{
//     //   pe.in_b := 0.U
//     // }
//     reg_c.valid := io.in_b.valid
//     io.out_c.bits := reg_c.bits
//     io.out_c.valid := reg_c.valid
//     //pe.in_c := io.in_c.bits
//     io.out_b.bits := reg_b.bits
//     io.out_b.valid := reg_b.valid
//     io.out_a.bits := trans_a.bits
//     io.out_a.valid := trans_a.valid
// }
// class WSSystolic(s: Int, x: Int, max_input_w: Int, max_input_h: Int, max_c: Int, max_ks: Int, cycle_read_input: Int, cycle_read_kernel: Int, cycle_out_res: Int, m: Int, n: Int, width: Int) extends Module{
//   val io = IO(new Bundle{
//     val inst = DeqIO(new RoCCInstruction()) //指令输入
//     //val config = Input(new ConvConfig())  //后续会合并到inst里
//     val a_in = DeqIO(Vec(cycle_read_kernel, UInt((n*width).W))) //数据输入
//     val b_in = DeqIO(Vec(cycle_read_input, UInt((m*width).W)))
//     val c_out = Output(Valid(Vec(cycle_out_res, UInt((m*n*width).W))))
//   })
  
//   //assert(s*x/t==1)
  
//   val ids = Module(new InstDispatcher()).io
//   printf("config: %d %d %d %d\n",ids.config.c, ids.config.ks, ids.config.in_w, ids.config.in_h)
//   val stage_cycle = RegInit(333333.U(20.W))
//   stage_cycle := ids.config.in_w

//   //ids.config := io.config
//   val pes = for(i <- 0 until s) yield{
//     for(j <- 0 until x) yield{
//       Module(new WSPE(i, s, m, n, width))
//     }
//   }
//   // printf("PE status\n")
//   //   for(j <- 0 until x){
//   //     printf("(%d, %d, %d, %d)",pes(0)(j).io.in_b.bits,pes(0)(j).io.in_b.valid, pes(0)(j).io.cur_dt, pes(0)(j).io.cur_cycle)
//   //   }
//   // printf("\n")
//   val a_input = Module(new DFSysIn_Kernel(x, s, max_c*max_ks*max_ks, 3,cycle_read_kernel, n*width))
//   val b_input = Module(new DFSysIn_Input(x, max_input_w, max_c, max_ks, cycle_read_input, m*width))
//   val c_output = Module(new Update_Result(x, s, max_input_h, max_input_w/s, cycle_out_res, m*n*width))
//   //val transC = Module(new DCStatOut(t, s, x, m*n*width))
//   a_input.io.in_inst <> ids.wr_filter
//   a_input.io.out_inst <> ids.rd_filter
//   b_input.io.in_inst <> ids.wr_input
//   b_input.io.out_inst <> ids.rd_input
//   c_output.io.in_inst <> ids.wr_output
//   c_output.io.out_inst <> ids.rd_output
//   io.a_in.ready := a_input.io.data_in.ready
//   io.b_in.ready := b_input.io.data_in.ready
//   a_input.io.data_out.ready:=true.B
//   b_input.io.data_out.ready:=true.B
//   a_input.io.config:=ids.config
//   b_input.io.config:=ids.config
//   c_output.io.config:=ids.config
//   a_input.io.data_in.valid:=io.a_in.valid
//   b_input.io.data_in.valid:=io.b_in.valid
//   io.c_out:=c_output.io.data_out
//   for(i <- 0 until cycle_read_kernel){
//     a_input.io.data_in.bits(i):=io.a_in.bits(i).asUInt
//   }
//   for(i <- 0 until cycle_read_input){
//     b_input.io.data_in.bits(i):=io.b_in.bits(i).asUInt
//   }
//   for(i <- 0 until s){
//     pes(i)(0).io.in_stage_cycle:=reduce_cycle
//     pes(i)(0).io.in_a:=a_input.io.data_out.bits(i)
//   }
//   for(i<- 0 until s){
//     for(j<- 1 until x){
//       pes(i)(j).io.in_stage_cycle:=pes(i)(j-1).io.out_stage_cycle
//       pes(i)(j).io.in_a:=pes(i)(j-1).io.out_a
//     }
//   }
//   for(i <- 0 until x){
//     pes(0)(i).io.in_b:=b_input.io.data_out.bits(i)
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
//     c_output.io.data_in(i):=pes(s-1)(i).io.out_c
//   }
//   //   //io.c_out(i).valid:=pes(s-1)(i).io.res_out.valid
//   // }
//   // for(i <- 0 until s*x/t){
//   //   io.c_out(i).valid:=transC.io.data_out.valid
//   //   io.c_out(i).bits:=transC.io.data_out.bits(i)
//   // }
//   // transC.io.data_out.ready:=true.B
// }