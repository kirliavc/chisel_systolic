package systolic

import chisel3._
import chisel3.iotesters.{PeekPokeTester, Driver}
import scala.collection.mutable.ArrayBuffer
import scala.math.log10
import chisel3.util._
import chisel3.experimental._
class ram_sdp_1024x256 extends Module{
  val io = IO(new Bundle(){
    // val wr_clk = Input(Clock())
    // val rd_clk = Input(Clock())
    val wr_en = Input(Bool())
    val wr_addr = Input(UInt(10.W))
    val wr_data = Input(UInt(256.W))
    
    val rd_en = Input(Bool())
    val rd_addr = Input(UInt(10.W))
    val rd_data = Output(UInt(256.W))
  })
  val buf = SyncReadMem(1024, UInt(256.W))
  when(io.wr_en){
    buf.write(io.wr_addr, io.wr_data)
  }
  io.rd_data := buf.read(io.rd_addr)
}
class ram_sdp_1024x16 extends Module{
  val io = IO(new Bundle(){
    // val wr_clk = Input(Clock())
    // val rd_clk = Input(Clock())
    val wr_en = Input(Bool())
    val wr_addr = Input(UInt(10.W))
    val wr_data = Input(UInt(16.W))
    
    val rd_en = Input(UInt(1.W))
    val rd_addr = Input(UInt(10.W))
    val rd_data = Output(UInt(16.W))
  })
  val buf = SyncReadMem(1024, UInt(16.W))
  when(io.wr_en){
    buf.write(io.wr_addr, io.wr_data)
  }
  io.rd_data := buf.read(io.rd_addr)
}
class ConvConfig extends Bundle{
  val in_w = UInt(10.W)
  val ks = UInt(10.W)
  val out_w = UInt(10.W)
  val stride = UInt(10.W)
  //val buf_rep = UInt(10.W)
  //val input_cycle = UInt(10.W)
  //val c_tile_num = UInt(10.W)
}
// inst: in_input 
class RoCCInstruction extends Bundle {
  val funct = UInt(7.W)
  val rs2 = UInt(5.W)
  val rs1 = UInt(5.W)
  val xd = Bool()
  val xs1 = Bool()
  val xs2 = Bool()
  val rd = UInt(5.W)
  val opcode = UInt(7.W)
}
class BufIDInst extends Bundle{
  val id = UInt(5.W)
}
class ConvInst extends Bundle{
  val input_id = UInt(5.W)
  val filter_id = UInt(5.W)
  val output_id = UInt(5.W)
  val free = UInt(3.W)
}
class InstDispatcher extends Module{
  val io = IO(new Bundle{
    val inst = DeqIO(new RoCCInstruction())
    val wr_input = EnqIO(new BufIDInst())
    val wr_filter = EnqIO(new BufIDInst())
    val conv_exec = EnqIO(new ConvInst())
    //val rd_input = EnqIO(new BufIDInst())
    //val rd_filter = EnqIO(new BufIDInst())
    //val wr_output = EnqIO(new BufIDInst())
    val rd_output = EnqIO(new BufIDInst())
    val config = Output(new ConvConfig())
  })
  val config_ks =   RegInit(0.U(10.W))
  val config_pad =  RegInit(0.U(10.W))
  val config_out_w =  RegInit(1.U(10.W))
  val wr_input_q =  Module(new Queue(new BufIDInst(), 10)).io
  val wr_filter_q = Module(new Queue(new BufIDInst(), 10)).io
  val conv_q =  Module(new Queue(new ConvInst(), 10)).io
  val rd_output_q = Module(new Queue(new BufIDInst(), 10)).io
  val input_en = RegInit(VecInit(Seq.fill(32)(false.B)))
  val filter_en = RegInit(VecInit(Seq.fill(32)(false.B)))
  val out_en = RegInit(VecInit(Seq.fill(32)(false.B)))
  val conv_pre_input_id = RegInit(0.U(10.W))
  val conv_pre_filter_id = RegInit(0.U(10.W))
  io.config.stride := 1.U
  io.config.in_w := config_out_w + config_ks - 1.U
  io.config.ks := config_ks
  io.config.out_w := config_out_w
  io.inst.ready := wr_input_q.enq.ready && wr_filter_q.enq.ready && conv_q.enq.ready && rd_output_q.enq.ready
  // val wr_input =  DeqIO(new BufIDInst())
  // val wr_filter = DeqIO(new BufIDInst())
  // val rd_input =  DeqIO(new BufIDInst())
  // val rd_filter = DeqIO(new BufIDInst())
  // val wr_output = DeqIO(new BufIDInst())
  // val rd_output = DeqIO(new BufIDInst())
  
  // deq ops
  // printf("write buf: %d %d\n",io.wr_input.valid, io.wr_filter.valid)
  // printf("input_en: %d %d %d %d\n",input_en(0), input_en(1), input_en(2), filter_en(1))
  // printf("compute: %d %d %d\n", io.rd_input.valid, io.rd_filter.valid, io.wr_output.valid)
  // printf("rd_output: %d %d %d\n",io.rd_output.ready, io.rd_output.valid, io.rd_output.bits.id)

  //以下是从队列中读取指令，并发送给加速器
  // (1) 指令队列中有指令，且input_en满足条件
  io.wr_input.valid := wr_input_q.deq.valid && (!input_en(wr_input_q.deq.bits.id))
  io.wr_input.bits := wr_input_q.deq.bits
  wr_input_q.deq.ready := io.wr_input.ready
  //printf("io.wr_input.ready=%d\n",io.wr_input.ready)
  when(io.wr_input.ready){
    input_en(io.wr_input.bits.id):=true.B
  }

  io.wr_filter.valid := wr_filter_q.deq.valid && (!filter_en(wr_filter_q.deq.bits.id))
  io.wr_filter.bits := wr_filter_q.deq.bits
  wr_filter_q.deq.ready := io.wr_filter.ready
  when(io.wr_filter.ready){
    filter_en(io.wr_filter.bits.id):=true.B
  }
  // input_en
  // out_en为false时，才可以写output
  io.conv_exec.valid := 
    conv_q.deq.valid && 
    (!conv_q.deq.bits.free) &&
    (input_en(conv_q.deq.bits.input_id + io.config.ks - 1.U)) && 
    (filter_en(conv_q.deq.bits.filter_id)) && 
    (!out_en(conv_q.deq.bits.output_id))
  io.conv_exec.bits := conv_q.deq.bits
  // conv完成，或者free操作时，直接完成指令，设置ready
  conv_q.deq.ready := io.conv_exec.ready || (conv_q.deq.bits.free=/=0.U)

  // printf("CONV_Q FREE:%d %d %d %d\n",conv_q.deq.bits.free, conv_q.deq.bits.free(0), conv_q.deq.bits.free(1), conv_q.deq.bits.free(2))
  // 用free的3个bit来说明具体是哪个buffer的free指令
  when(conv_q.deq.bits.free(0)){

    out_en(conv_q.deq.bits.input_id):=true.B
  }
  when(conv_q.deq.bits.free(1)){
    filter_en(conv_q.deq.bits.filter_id):=false.B
  }
  when(conv_q.deq.bits.free(2)){
    input_en(conv_q.deq.bits.output_id):=false.B
  }

  // out_en 为true时，才可以输出
  printf("IDS RD Output Valid: %d\n", io.rd_output.valid)
  io.rd_output.valid := rd_output_q.deq.valid && (out_en(rd_output_q.deq.bits.id))
  io.rd_output.bits := rd_output_q.deq.bits
  rd_output_q.deq.ready := io.rd_output.ready
  when(io.rd_output.ready){  //free指令开始时
    //printf("_____FREE_____%d\n",wr_output_q.deq.bits.id)
    out_en(io.rd_output.bits.id):=false.B
  }
  // io.wr_filter <> wr_filter_q
  // io.rd_input <> rd_input_q
  // io.rd_filter <> rd_filter_q
  // io.wr_output <> wr_output_q
  // io.rd_output <> rd_output_q

  // push into task queues
  when(io.inst.valid&&io.inst.ready){
    val funct = io.inst.bits.funct
    val rs1 = io.inst.bits.rs1
    val rs2 = io.inst.bits.rs2
    val rd = io.inst.bits.rd
    //printf("inst in: %d %d %d %d \n", funct, rs1, rs2, rd)
    // load input
    when(funct===1.U){
      wr_input_q.enq.valid := true.B
      wr_input_q.enq.bits.id := rs1
    }.otherwise{
      wr_input_q.enq.valid := false.B
      wr_input_q.enq.bits.id := 0.U
    }
    // load filter
    when(funct===2.U){
      wr_filter_q.enq.valid := true.B
      wr_filter_q.enq.bits.id := rs1
    }.otherwise{
      wr_filter_q.enq.valid := false.B
      wr_filter_q.enq.bits.id := 0.U
    }
    when(funct===3.U){
      conv_pre_input_id := rs1
      conv_pre_filter_id := rs2
    }
    // compute, and mark flag (4,5,6,7)
    when(funct===4.U){
      conv_q.enq.valid := true.B
      conv_q.enq.bits.input_id := conv_pre_input_id
      conv_q.enq.bits.filter_id := conv_pre_filter_id
      conv_q.enq.bits.output_id := rs1
      conv_q.enq.bits.free := 0.U
    }.otherwise{
      printf("free instruction, %d %d\n",funct, rs1)
      conv_q.enq.valid := (funct>=5.U)&&(funct<=7.U)
      conv_q.enq.bits.input_id := Mux(funct===5.U, rs1, 0.U)
      conv_q.enq.bits.filter_id := Mux(funct===6.U, rs1, 0.U)
      conv_q.enq.bits.output_id := Mux(funct===7.U, rs1, 0.U)
      // 5->4, 6->2, 7->1
      conv_q.enq.bits.free := Mux(funct===5.U, 4.U, Mux(funct===6.U, 2.U, Mux(funct===7.U, 1.U, 0.U)))
    }
    /*
      free (rs1, rs2)
      x=0 --- input buf
      x=1 --- filter buf
      x=2 --- output buf
    */
    // store
    when(funct===8.U){
      rd_output_q.enq.valid := true.B
      rd_output_q.enq.bits.id := rs1
    }.otherwise{
      rd_output_q.enq.valid := false.B
      rd_output_q.enq.bits.id := 0.U
    }
    // config h, w
    when(funct===9.U){
      printf("funct=9")
      config_out_w := rs1
      config_ks := rs2
    }
  }.otherwise{
    wr_input_q.enq.valid := false.B
    wr_input_q.enq.bits.id := 0.U
    wr_filter_q.enq.valid := false.B
    wr_filter_q.enq.bits.id := 0.U
    conv_q.enq.valid := false.B
    conv_q.enq.bits.input_id := 0.U
    conv_q.enq.bits.filter_id := 0.U
    conv_q.enq.bits.output_id := 0.U
    conv_q.enq.bits.free := 0.U
    rd_output_q.enq.valid := false.B
    rd_output_q.enq.bits.id := 0.U
  }
  
  // printf("%d %d\n",io.wr_input.valid, io.wr_input.bits.id)
  // printf("%d %d\n",io.wr_filter.valid, io.wr_filter.bits.id)
  // printf("%d %d\n",io.wr_output.valid, io.wr_output.bits.id)
  // printf("%d %d\n",io.rd_input.valid, io.rd_input.bits.id)
  // printf("%d %d\n",io.rd_filter.valid, io.rd_filter.bits.id)
  //printf("out_inst queue: %d %d %d\n",rd_output_q.deq.ready, rd_output_q.deq.valid, rd_output_q.deq.bits.id)
  //extract inst from queues
}
// Layout = NHWC
class WSSysIn_Input(pe_num: Int, slot_num: Int, slot_size: Int, cycle_read: Int, width: Int) extends Module{


  val io=IO(new Bundle{
    val in_inst = DeqIO(new BufIDInst())
    val out_inst = DeqIO(new BufIDInst())
    val config = Input(new ConvConfig())
    // TODO: crossbar input cycle_read -> pe_num
    val data_in = DeqIO(Vec(pe_num, Valid(UInt(width.W))))
    val data_out = EnqIO(Vec(pe_num, Valid(UInt(width.W))))
  })
  // input: 1024 * 256
  val buf_bank = for(i <- 0 until pe_num) yield{
      Module(new ram_sdp_1024x256()).io
  }
  // for(i <- 0 until pe_num){
  //   buf_bank(i).wr_clk = Clock()
  //   buf_bank(i).rd_clk = Clock()
  // }
  //val buf = SyncReadMem(max_ks*input_size, UInt(width.W))
  
  val in_addr = RegInit(VecInit(Seq.fill(pe_num)(0.U(10.W))))  //每行中的写地址
  val out_addr = RegInit(VecInit(Seq.fill(pe_num)(0.U(10.W)))) 
  val can_out = RegInit(VecInit(Seq.fill(pe_num+1)(false.B))) 
  //输出的条件是：有这条指令，并且systolic array允许输入数据
  
  val out_kh = RegInit(VecInit(Seq.fill(pe_num)(0.U(4.W))))
  val out_kw = RegInit(VecInit(Seq.fill(pe_num)(0.U(4.W))))
  val out_slot = RegInit(VecInit(Seq.fill(pe_num)(0.U(4.W))))
  // 暂时不用ready来控制buffer输出，全部由整体的控制器来控制
  // 但依然要控制buffer输入，ready表示输入完毕
  //io.in_inst.ready := true.B
  can_out(0) := io.out_inst.valid && io.data_out.ready
  out_slot(0) := io.out_inst.bits.id
  io.out_inst.ready := true.B

  // val in_inst_ready = RegInit(true.B)
  // val out_inst_ready = RegInit(true.B)
  // when(io.in_inst.valid && in_inst_ready){
  //   in_inst_ready := false.B
  // }
  // when(io.out_inst.valid && out_inst_ready){
  //   out_inst_ready := false.B
  // }
  for(i <- 0 until pe_num){
    can_out(i+1) := can_out(i)
  }
  for(i <- 1 until pe_num){
    out_kh(i):=out_kh(i-1)
    out_kw(i):=out_kw(i-1)
    out_addr(i):=out_addr(i-1)
    out_slot(i):=out_slot(i-1)
  }
  io.data_in.ready := io.in_inst.valid
  for(i <- 0 until pe_num){
    buf_bank(i).wr_addr := io.in_inst.bits.id * (slot_size).asUInt + in_addr(i)
    buf_bank(i).wr_en := io.data_in.valid && io.data_in.ready
    buf_bank(i).wr_data := io.data_in.bits(i).bits
    buf_bank(i).rd_en := 1.U
    buf_bank(i).rd_addr := (out_slot(i) + out_kh(i)) * (slot_size).asUInt + out_addr(i)+out_kw(i)
    io.data_out.bits(i).bits := buf_bank(i).rd_data
  }
  
  when(io.data_in.valid && io.data_in.ready){
    for(i <- 0 until pe_num){
      when(in_addr(i)+io.data_in.bits(i).valid<io.config.in_w){
        in_addr(i):=in_addr(i)+io.data_in.bits(i).valid
      }.otherwise{
        in_addr(i):=0.U
      }
    }

    //bank输入完毕，则该条指令完成
    when(in_addr(pe_num-1)===io.config.in_w - 1.U && io.data_in.bits(pe_num-1).valid){
      io.in_inst.ready := true.B
    }.otherwise{
      io.in_inst.ready := false.B
    }
  }.otherwise{
    io.in_inst.ready := false.B
  }
  for(i <- 0 until pe_num){
    io.data_out.bits(i).valid:=can_out(i+1)
  }
  when(can_out(0)){
    when(out_addr(0)+1.U<io.config.out_w){
      out_addr(0):=out_addr(0)+1.U
    }.otherwise{
      out_addr(0):=0.U
      when(out_kw(0)+1.U<io.config.ks){
        out_kw(0):=out_kw(0)+1.U
      }.otherwise{
        out_kw(0) := 0.U
        when(out_kh(0)+1.U<io.config.ks){
          out_kh(0):=out_kh(0)+1.U
        }.otherwise{
          out_kh(0):=0.U
        }
      }
    }
  }
  io.data_out.valid := true.B

}
// kernel的input buf， bank=output channel
class WSSysIn_Kernel(in_channel: Int, out_channel: Int, slot_num: Int, slot_size: Int, cycle_read: Int, width: Int) extends Module{

  val io=IO(new Bundle{
    val in_inst = DeqIO(new BufIDInst())
    val out_inst = DeqIO(new BufIDInst())
    val config = Input(new ConvConfig())
    val data_in = DeqIO(Vec(out_channel, Valid(UInt(width.W))))
    val data_out = EnqIO(Vec(out_channel, Valid(UInt(width.W))))
  })
  val buf_bank = for(i <- 0 until out_channel) yield{
      Module(new ram_sdp_1024x16()).io
      //SyncReadMem(slot_num * slot_size, UInt(width.W))
  }

  val in_addr = RegInit(VecInit(Seq.fill(out_channel)(0.U(10.W))))  //每行中的写地址
  val out_addr = RegInit(VecInit(Seq.fill(out_channel)(0.U(10.W)))) 
  val can_out = RegInit(VecInit(Seq.fill(out_channel+1)(false.B))) 
  can_out(0) := io.out_inst.valid && io.data_out.ready
  val out_kh = RegInit(VecInit(Seq.fill(out_channel)(0.U(4.W))))
  val out_kw = RegInit(VecInit(Seq.fill(out_channel)(0.U(4.W))))
  val out_slot = RegInit(VecInit(Seq.fill(out_channel)(0.U(4.W))))
  //io.in_inst.ready := true.B
  io.out_inst.ready := true.B
  out_slot(0) := io.out_inst.bits.id
  // val in_inst_ready = RegInit(true.B)
  // val out_inst_ready = RegInit(true.B)
  
  // when(io.in_inst.valid && in_inst_ready){
  //   in_inst_ready := false.B
  // }
  // when(io.out_inst.valid && out_inst_ready){
  //   out_inst_ready := false.B
  // }

  for(i <- 0 until out_channel){
    can_out(i+1) := can_out(i)
  }
  for(i <- 1 until out_channel){
    out_kh(i):=out_kh(i-1)
    out_kw(i):=out_kw(i-1)
    out_addr(i):=out_addr(i-1)
    out_slot(i):=out_slot(i-1)


  }
  io.data_in.ready := io.in_inst.valid
  when(io.data_in.valid && io.data_in.ready){
    for(i <- 0 until out_channel){
      when(in_addr(i)+io.data_in.bits(i).valid<in_channel.asUInt * io.config.ks * io.config.ks){
        in_addr(i):=in_addr(i)+io.data_in.bits(i).valid
      }.otherwise{
        in_addr(i):=0.U
      }
      
    }
    // 最后一个bank输入完毕，则该条指令完成，共输入c*ks*ks个
    when(in_addr(out_channel-1)===in_channel.asUInt * io.config.ks * io.config.ks-1.U && io.data_in.bits(out_channel-1).valid){
      io.in_inst.ready := true.B
    }.otherwise{
      io.in_inst.ready := false.B
    }
  }.otherwise{
    io.in_inst.ready := false.B
  }
  for(i <- 0 until out_channel){
    // 地址的layout： ks * ks * C
    io.data_out.bits(i).valid:=can_out(i+1)
    buf_bank(i).wr_addr := io.in_inst.bits.id * (slot_size).asUInt + in_addr(i)
    buf_bank(i).wr_en := io.data_in.valid && io.data_in.ready
    buf_bank(i).wr_data := io.data_in.bits(i).bits
    buf_bank(i).rd_en := 1.U
    buf_bank(i).rd_addr := out_slot(i) * (slot_size).asUInt + (out_kh(i)*io.config.ks+out_kw(i))*in_channel.asUInt + in_channel.asUInt - 1.U - out_addr(i)
    io.data_out.bits(i).bits := buf_bank(i).rd_data
  }
  when(can_out(0)){
    when(out_addr(0)+1.U<in_channel.asUInt){
      out_addr(0):=out_addr(0)+1.U
    }.otherwise{
      out_addr(0):=0.U
      when(out_kw(0)+1.U<io.config.ks){
        out_kw(0):=out_kw(0)+1.U
      }.otherwise{
        out_kw(0):=0.U
        when(out_kh(0)+1.U<io.config.ks){
          out_kh(0):=out_kh(0)+1.U
        }.otherwise{
          out_kh(0):=0.U
        }
      }
    }
  }
  io.data_out.valid := true.B

  // out_fin 表示一条指令做完了
  //io.out_inst.ready := out_addr(0)===0.U && out_kw(0)===0.U && out_kh(0)===0.U && io.data_out.ready
}

class Update_Result(out_channel: Int, slot_num: Int, slot_size: Int, cycle_write: Int, width: Int) extends Module{
  val io=IO(new Bundle{
    val in_inst = DeqIO(new BufIDInst())
    val out_inst = DeqIO(new BufIDInst())
    val config = Input(new ConvConfig())
    val data_in = Input(Vec(out_channel, Valid(UInt(width.W))))
    val data_out = Output(Valid(Vec(out_channel,UInt(width.W))))
  })
  // val buf_bank = for(i <- 0 until out_channel) yield{
  //     SyncReadMem(slot_num * slot_size, UInt(width.W))
  // }
  val buf_reg = for(i <- 0 until out_channel) yield{
    RegInit(VecInit(Seq.fill(slot_num * slot_size)(0.U(width.W))))
    //SyncReadMem(slot_num * slot_size, UInt(width.W))
  }

  val in_addr = RegInit(VecInit(Seq.fill(out_channel)(0.U(10.W))))  //每行中的写地址
  val out_addr = RegInit(0.U(10.W))
  val update_data = RegInit(VecInit(Seq.fill(out_channel)(0.U(width.W))))
  val read_data = RegInit(VecInit(Seq.fill(out_channel)(0.U(width.W))))
  val update_valid = RegInit(VecInit(Seq.fill(out_channel)(false.B)))
  val update_valid2 = RegInit(VecInit(Seq.fill(out_channel)(false.B)))
  val update_addr = RegInit(VecInit(Seq.fill(out_channel)(0.U(10.W))))
  val write_addr = RegInit(VecInit(Seq.fill(out_channel)(0.U(10.W))))
  val in_data = RegInit(VecInit(Seq.fill(out_channel)(0.U(width.W))))
  val update_slot = RegInit(VecInit(Seq.fill(out_channel*2)(0.U(10.W))))

  val output_valid = RegInit(VecInit(Seq.fill(out_channel)(false.B)))
  val output_id = RegInit(VecInit(Seq.fill(out_channel)(0.U(10.W))))


  //val buf_reg = RegInit(VecInit(Seq.fill(out_channel * slot_size)(0.U(width.W))))

  printf("Output Buf Data, out_addr=%d\n", out_addr)
  for(i <- 0 until 8){
    for(j <- 0 until 12){
      printf("%d ",buf_reg(i)(j.asUInt))
    }
    printf("\n")
  }
  
  printf("\n")

  
  io.in_inst.ready := true.B
  output_valid(0) := io.out_inst.valid
  output_id(0) := io.out_inst.bits.id
  // out_inst传进来的时候，其实还没算完，等out_channel个cycle算完了再开始输出
  for(i <- 1 until out_channel){
    output_valid(i) := output_valid(i-1)
    output_id(i) := output_id(i-1)
  }
  update_slot(0) := io.in_inst.bits.id 
  for(i <- 1 until out_channel*2){
    update_slot(i) := update_slot(i-1)
  }
  // PE to buffer
  printf("Update_Result: in_data=%d, read_data=%d, read_addr=%d, update_data=%d, update_addr=%d Out Inst Valid: %d\n",in_data(0),read_data(0), in_addr(0), update_data(0), update_addr(0), io.out_inst.valid)
  for(i <- 0 until out_channel){
    in_data(i) := io.data_in(i).bits
    read_data(i) := buf_reg(i)(update_slot(out_channel-1+i) * slot_size.asUInt + in_addr(i))
    
    //read_data(i) := buf_bank(i).read(io.in_inst.bits.id * slot_size.asUInt + in_addr(i))
    update_addr(i) := update_slot(out_channel-1+i) * slot_size.asUInt + in_addr(i)
    write_addr(i) := update_addr(i)
    update_valid(i) := io.data_in(i).valid
    update_valid2(i) := update_valid(i)
    when(io.data_in(i).valid){
      in_addr(i) := (in_addr(i) + 1.U) % (io.config.out_w)
    }
    when(update_valid(i)){
      update_data(i) := read_data(i) + in_data(i)
    }
    when(update_valid2(i)){
      buf_reg(i)(write_addr(i)):=update_data(i)
      //buf_bank(i).write(update_addr(i), update_data(i))
    }
  }

  // buffer to MEM

  for(i <- 0 until out_channel){
    io.data_out.bits(i) := buf_reg(i)(io.out_inst.bits.id * slot_size.asUInt + out_addr)
    out_addr := (out_addr + io.out_inst.valid)%(io.config.out_w)
    //io.data_out.bits(i).valid := io.out_inst.valid
  }
  io.data_out.valid := io.out_inst.valid
  io.out_inst.ready := (out_addr===io.config.out_w - 1.U)
}
// output contains multiple tiles, each tile k(s)*h(x)
// result buffer has three dim: k * h * w
// class Update_Result(x: Int, s: Int, num_tile_h: Int, num_tile_w: Int, ipc: Int, width: Int)extends Module{
//   val io=IO(new Bundle{
//     val in_inst = DeqIO(new BufIDInst())
//     val out_inst = DeqIO(new BufIDInst())
//     val config = Input(new ConvConfig())
//     val data_in = Input(Vec(x, Valid(UInt(width.W))))
//     val data_out = Output(Valid(Vec(ipc,UInt(width.W))))
//   })
//   // printf("in output\n")
//   // for(i <- 0 until x){
//   //   printf("(%d,%d) ", io.data_in(i).bits, io.data_in(i).valid)
//   // }
//   // printf("\n")
//   //mem layout: each tile (s*x), tile (rep_h * rep_w) *(s*x)
//   //input order: c * h * tile_w * (s*x)
//   val mem = SyncReadMem(num_tile_h*num_tile_w*x*s,UInt(width.W) )
//   val tile_id = RegInit(VecInit(Seq.fill(x)(0.U(10.W))))
//   val tile_k_id = RegInit(VecInit(Seq.fill(x)(0.U(10.W))))
//   val h_id = RegInit(VecInit(Seq.fill(x)(0.U(10.W))))
//   val tile_w_id = RegInit(VecInit(Seq.fill(x)(0.U(10.W))))
//   val inner_k = RegInit(VecInit(Seq.fill(x)(0.U(10.W))))
//   val num_tiles = RegInit(100.U(10.W))
//   val outer_h = RegInit(100.U(10.W))
//   val update_data = RegInit(VecInit(Seq.fill(x)(0.U(width.W))))
//   val read_data = RegInit(VecInit(Seq.fill(x)(0.U(width.W))))
//   val update_addr = RegInit(VecInit(Seq.fill(x)(0.U(10.W))))
//   val read_addr = RegInit(VecInit(Seq.fill(x)(0.U(10.W))))
//   val update_valid = RegInit(VecInit(Seq.fill(x)(false.B)))
//   val out_addr = RegInit(0.U(10.W))
//   val out_h = RegInit(0.U(10.W))
//   val out_w = RegInit(0.U(10.W))
//   val out_valid = RegInit(false.B)

//   num_tiles :=(io.config.in_w + io.config.pad + io.config.pad - io.config.ks)/(s.asUInt)+1.U
//   tile_id(0) := io.in_inst.bits.id

//   for(i <- 1 until x){
//     tile_id(i) := tile_id(i-1)
//   }
//   io.in_inst.ready := (inner_k(0)===(s-1).asUInt) && (tile_w_id(0) === num_tiles - 1.U)
//   out_valid := io.out_inst.valid
//   //printf("output inst: %d\n",io.out_inst.valid)
//   // printf("input status\n")
//   // printf("[%d %d %d %d %d %d] ",io.data_in(0).bits, io.data_in(0).valid, tile_w_id(0),h_id(0), update_addr(0),update_data(0))
//   // printf("\n")
//   // printf("output:[%d %d %d]\n", out_h, out_w, out_addr)
//   // printf("out mem status\n")
//   // for(i <- 0 until num_tile_w){
//   //   for(j <- 0 until x*s){
//   //     printf("%d ", mem.read((i*x*s+j).asUInt))
//   //   }
//   //   printf("\n")
//   // }
//   // printf("\n")
//   num_tiles :=(io.config.in_w + io.config.pad + io.config.pad - io.config.ks)/(s.asUInt)+1.U
//   outer_h := io.config.in_h + io.config.pad + io.config.pad - io.config.ks + 1.U
//   //printf("%d %d %d %d %d\n",outer_h, h_id(0), num_tile_w.asUInt, num_tiles,(num_tile_h*num_tile_w*x*s).asUInt)
//   for(i <- 0 until x){
//     update_valid(i):=io.data_in(i).valid
//     //read_addr(i) := h_id(i) * (num_tile_w*s*x).asUInt + tile_w_id(i) * (s*x).asUInt + (i + (s-1) * x).asUInt - inner_k(i) * x.asUInt
//     update_addr(i) := tile_id(i) * (num_tile_w*s*x).asUInt + tile_w_id(i) * (s*x).asUInt + (i + (s-1) * x).asUInt - inner_k(i) * x.asUInt
//     when(io.data_in(i).valid){
//       when(inner_k(i) === (s-1).asUInt){
//         inner_k(i) := 0.U
//         when(tile_w_id(i) ===num_tiles - 1.U){
//           tile_w_id(i) := 0.U
//           // when(h_id(i) === outer_h - 1.U){
//           //   h_id(i) := 0.U
//           //   when(tile_c_id(i) === io.config.c_tile_num - 1.U){
//           //     tile_c_id(i) := 0.U
//           //   }.otherwise{
//           //     tile_c_id(i) := tile_c_id(i) + 1.U 
//           //   }
//           // }.otherwise{
//           //   h_id(i) := h_id(i) + 1.U
//           // }
//         }.otherwise{
//           tile_w_id(i) := tile_w_id(i) + 1.U
//         }
//       }.otherwise{
//         inner_k(i) := inner_k(i) + 1.U
//       }
//       update_data(i) := read_data(i) + io.data_in(i).bits
//       // when(tile_c_id(i)=/=0.U){
//       //   update_data(i) := read_data(i) + io.data_in(i).bits
//       // }.otherwise{
//       //   update_data(i) := io.data_in(i).bits
//       // }
//     }
//     read_data(i) := mem.read(tile_id(i) * (num_tile_w*s*x).asUInt + tile_w_id(i) * (s*x).asUInt + (i + (s-1) * x).asUInt - inner_k(i) * x.asUInt)
//     when(update_valid(i)){
//       mem.write(update_addr(i), update_data(i))
      
//     }
//   }
//   // one buffer is ready to output
//   // when(inner_k(0) === (s-1).asUInt && tile_c_id(0) === io.config.c_tile_num - 1.U){
//   //   in_fin := true.B
//   // }.otherwise{
//   //   in_fin := false.B
//   // }
//   when(io.out_inst.valid){
//     when(out_addr > (s*x-ipc).asUInt){
//       out_addr := 0.U
//       when(out_w > num_tiles - 1.U){
//         out_w := 0.U
//         io.out_inst.ready := true.B
//       //   when(out_h > outer_h - 1.U){
//       //     out_h := 0.U
//       //   }.otherwise{
//       //     out_h := out_h + 1.U
//       //   }
//       }.otherwise{
//         out_w := out_w + 1.U
//         io.out_inst.ready := false.B
//       }
//     }.otherwise{
//       out_addr := out_addr + ipc.asUInt
//       io.out_inst.ready := false.B
//     }
//   }.otherwise{
//     io.out_inst.ready := false.B
//   }
//   io.data_out.valid := out_valid
//   val last_addr = RegInit(0.U(10.W))
//   val last_out = RegInit(false.B)
//   last_addr := io.out_inst.bits.id* (num_tile_w * s*x).asUInt + out_w * (s*x).asUInt + out_addr
//   last_out := io.data_out.valid
//   for(i <- 0 until ipc){
//     io.data_out.bits(i) := mem.read(io.out_inst.bits.id* (num_tile_w * s*x).asUInt + out_w * (s*x).asUInt + out_addr + (i).asUInt)
//     when(last_out){
//       mem.write(last_addr + i.asUInt, 0.U)
//     }
//   }
// }