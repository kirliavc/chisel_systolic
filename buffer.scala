package systolic

import chisel3._
import chisel3.iotesters.{PeekPokeTester, Driver}
import scala.collection.mutable.ArrayBuffer
import scala.math.log10
import chisel3.util._

class ConvConfig extends Bundle{
  val in_h = UInt(24.W)
  val in_w = UInt(24.W)
  val c = UInt(24.W)
  val ks = UInt(24.W)
  val pad = UInt(24.W)
  val stride = UInt(24.W)
  val buf_rep = UInt(24.W)
  val input_cycle = UInt(24.W)
  val c_tile_num = UInt(24.W)
}
// inst: in_input 
// class Inst extends Bundle{
//   val opcode =  
// }
class DFSysIn_Input(s: Int, line_len: Int, max_c: Int, max_ks: Int, cycle_read: Int, width: Int) extends Module{
  // case: C*ks*ks cycle, read a line=C*(W+ks-1)
  // output:
  // 出现输入一行长度不整除cycle_read_num，则末尾补0
  val input_size = max_c * line_len //
  // s = ((H+ks-1)*(W+ks-1))

  // t = C
  // input cycle: c*(io.in_w / cycle_read_num)
  // output cycle: ks*ks*c
  // tile output order: row * tile
  val io=IO(new Bundle{
    val config = Input(new ConvConfig())
    val data_in = DeqIO(Vec(cycle_read, UInt(width.W)))
    val data_out = EnqIO(Vec(s, Valid(UInt(width.W))))
  })
  val num_tiles = RegInit(0.U(24.W))
  num_tiles :=(io.config.in_w + io.config.pad + io.config.pad - io.config.ks)/(s.asUInt)+1.U
  val buf = SyncReadMem(max_ks*input_size, UInt(width.W))
  val in_addr = RegInit(0.U(24.W))  //每行中的写地址
  val in_channel = RegInit(0.U(24.W))
  val in_line = RegInit(0.U(24.W))//RegInit(io.config.pad * input_size.asUInt(24.W))  //在写第几行，从第x+1行开始输出
  val in_line_cnt = RegInit(0.U(24.W))
  val out_row = RegInit(VecInit(Seq.fill(s)(0.U(24.W))))
  val out_col = RegInit(VecInit(Seq.fill(s)(0.U(24.W))))
  val out_tile = RegInit(VecInit(Seq.fill(s)(0.U(24.W))))
  val out_line = RegInit(VecInit(Seq.fill(s)(0.U(24.W))))
  val out_line_cnt = RegInit(VecInit(Seq.fill(s)(0.U(24.W))))
  //输出的第一行
  val can_out = RegInit(VecInit(Seq.fill(s+1)(false.B)))
  val out_kw = RegInit(VecInit(Seq.fill(s)(0.U(24.W))))
  //当前输出列
  val out_kh = RegInit(VecInit(Seq.fill(s)(0.U(24.W))))//当前输出行
  val out_c = RegInit(VecInit(Seq.fill(s)(0.U(24.W))))
  //val fin_line = Wire(Vec(s, UInt(24.W)))//第i个buffer的最后一行输出
  val buf_rem = RegInit(0.U(24.W))
  val input_fin = Wire(UInt(4.W))
  val out_fin = Wire(UInt(4.W))
  val out_ready = RegInit(false.B)
  // printf("input mem\n")
  // for(i <- 0 until max_ks){
  //   for(j <- 0 until max_c){
  //     for(k <- 0 until line_len){
  //       printf("%d ",buf(i*max_c*line_len+j*line_len+k))
  //     }
  //     printf("\n")
  //   }
  //   printf("\n")
  // }
  out_ready := io.data_out.ready
  buf_rem := buf_rem + input_fin - out_fin
  io.data_in.ready := (buf_rem <= io.config.ks) && (in_line_cnt >= io.config.pad)
  can_out(0) := (buf_rem >= io.config.ks) && io.data_out.ready

  for(i <- 1 until s){
    can_out(i):=can_out(i-1)
  }
  can_out(s):=can_out(s-1)
  when(io.data_in.valid&&io.data_in.ready){
    for(i <- 0 until cycle_read){
      when(in_addr+i.asUInt<io.config.in_w){
        // write buffer with padding
        buf.write(in_line + in_channel + in_addr + io.config.pad+i.asUInt, io.data_in.bits(i))
      }
    }
    
    when(in_addr < io.config.in_w-cycle_read.asUInt){
      in_addr:=in_addr + cycle_read.asUInt
      input_fin:=0.U
    }.otherwise{
      in_addr:=0.U
      
      when(in_channel===(io.config.c-1.U)*line_len.asUInt){
        when(in_line_cnt === io.config.in_h - 1.U){ // finish one image
          in_line_cnt := 0.U
          // add zero lines below
          // for(i <- 0 until io.config.pad){
          //   buf.write((in_line + max_c * line_len * i.asUInt)%(max_ks * line_len * max_c).asUInt, 0.U((line*len*max_c).W))
          // }
        }.otherwise{
          in_line_cnt := in_line_cnt + 1.U
        }
        
        in_channel:=0.U
        input_fin:=1.U
        when(in_line===((max_ks-1)*max_c*line_len).asUInt){
          in_line := 0.U
        }.otherwise{
          in_line := in_line + (max_c * line_len).asUInt
        }
      }.otherwise{
        in_channel:=in_channel+line_len.asUInt
        input_fin := 0.U
      }
      
    }
  }.elsewhen(in_line_cnt < io.config.pad){
    input_fin := 1.U
    in_line_cnt := in_line_cnt + 1.U
    when(in_line===((max_ks-1)*max_c*line_len).asUInt){
      in_line := 0.U
    }.otherwise{
      in_line := in_line + (max_c * line_len).asUInt
    }
    // write a line of zeros
    for(i <- 0 until line_len * max_c)
      buf.write(in_line+i.asUInt, 0.U)
  }.otherwise{
    input_fin := 0.U
  }
  // for(i <- 0 until ks+1){
  //   for(j <- 0 until c){
  //     for(k <- 0 until s+ks-1)
  //       printf("%d ",buf(i*c*(s+ks-1)+j*(s+ks-1)+k))
  //     printf("\n")
  //   }
  //   printf("\n")
  // }
  //输出顺序：tile_num*C*kh*kw
  //printf("input, out: %d tile=%d line=%d c=%d kh=%d kw=%d addr=%d bit=%d valid=%d\n",can_out(0), out_tile(0), out_line(0), out_c(0), out_kh(0)/input_size.asUInt, out_kw(0), out_c(0)*line_len.asUInt+out_kw(0)+out_kh(0)+out_tile(0)*s.asUInt,io.data_out.bits(0).bits,io.data_out.bits(0).valid)
  for(i <- 0 until s){
    when(can_out(i)&&io.data_out.ready){
      when(out_kw(i)===io.config.ks-1.U){
        out_kw(i):=0.U
        when(out_col(i)===io.config.ks-1.U){
          out_col(i):=0.U
          
          when(out_c(i)===io.config.c-1.U){
            out_c(i):=0.U
            when(out_tile(i)===num_tiles-1.U){
              out_tile(i):=0.U
              // last line of image?
              when(out_line_cnt(i) =/=io.config.in_h + io.config.pad + io.config.pad - io.config.ks){
                out_line_cnt(i) := 0.U
                out_line(i):=(out_line(i)+io.config.ks-io.config.pad)%(max_ks).asUInt
                out_kh(i):=(out_line(i)+io.config.ks-io.config.pad)%(max_ks).asUInt*input_size.asUInt
              }.otherwise{
                out_line_cnt(i) := out_line_cnt(i) + 1.U
                out_line(i):=Mux(out_line(i)===(max_ks-1).asUInt, 0.U, out_line(i)+1.U)
                out_kh(i):=Mux(out_line(i)===(max_ks-1).asUInt, 0.U, out_line(i)+1.U)*input_size.asUInt
              }
                
            }.otherwise{
              out_tile(i):=out_tile(i)+1.U
              out_kh(i) := out_line(i)*input_size.asUInt
            }
          }.otherwise{
            out_c(i):=out_c(i)+1.U
            out_kh(i) := out_line(i)*input_size.asUInt
          }
        }.otherwise{
          out_col(i):=out_col(i)+1.U
          out_kh(i) := Mux(out_kh(i)=/=((max_ks-1)*input_size).asUInt, out_kh(i)+input_size.asUInt, 0.U)
        }
      }.otherwise{
        out_kw(i):=out_kw(i)+1.U
      }
    }
    io.data_out.bits(i).bits:=buf.read(out_c(i)*line_len.asUInt+i.asUInt+out_kw(i)+out_kh(i)+out_tile(i)*s.asUInt)
    io.data_out.bits(i).valid:=can_out(i+1)
  }
  io.data_out.valid:=true.B
  when((out_kw(0)===io.config.ks-1.U)&&(out_col(0)===io.config.ks-1.U)&&(out_c(0)===io.config.c-1.U)&&(out_tile(0)===num_tiles-1.U)){
    when(out_line_cnt(0)===io.config.in_h + io.config.pad + io.config.pad - io.config.ks){
      // output last line
      out_fin := io.config.ks//1.U//io.config.ks - io.config.pad
    }.otherwise{
      out_fin := 1.U
    }
  }.otherwise{
    out_fin := 0.U
  }
  //out_fin := (out_kw(0)===io.config.ks-1.U)&&(out_col(0)===io.config.ks-1.U)&&(out_c(0)===io.config.c-1.U)&&(out_tile(0)===num_tiles-1.U)
}
class DFSysIn_Kernel(k: Int, max_cks: Int, buf_size: Int, cycle_read: Int, width: Int) extends Module{
  val io=IO(new Bundle{
    val config = Input(new ConvConfig())
    val data_in = DeqIO(Vec(cycle_read, UInt(width.W)))
    val data_out = EnqIO(Vec(k, Valid(UInt(width.W))))
  })
  val buf = SyncReadMem(2*buf_size*k*max_cks,UInt(width.W) )
  val line_len = RegInit(10000.U(24.W))
  val out_line_len = RegInit(10000.U(24.W))
  val in_addr = RegInit(0.U(24.W))
  val in_bufid = RegInit(0.U(24.W))
  val in_outid = RegInit(0.U(24.W))
  val out_addr = RegInit(VecInit(Seq.fill(k)(0.U(24.W))))
  val out_repid = RegInit(VecInit(Seq.fill(k)(0.U(24.W))))
  val out_bufid = RegInit(VecInit(Seq.fill(k)(0.U(24.W))))
  val out_outid = RegInit(VecInit(Seq.fill(k)(0.U(24.W))))
  val can_out = RegInit(VecInit(Seq.fill(k+1)(false.B)))
  val buf_rem = RegInit(0.U(24.W))
  val in_fin = Wire(Bool())
  val out_fin = Wire(Bool())
  val in_max_line = k*max_cks
  val out_ready = RegInit(false.B)
  //printf("filter, out: addr=%d bit=%d valid=%d\n",out_addr(0),io.data_out.bits(0).bits,io.data_out.bits(0).valid)
  out_ready := io.data_out.ready
  line_len := k.asUInt * io.config.c * io.config.ks * io.config.ks
  out_line_len := io.config.c * io.config.ks * io.config.ks
  buf_rem := buf_rem + in_fin - out_fin
  io.data_in.ready := (buf_rem < 2.U)
  can_out(0) := (buf_rem >= 1.U) && io.data_out.ready
  for(i <- 1 until k){
    can_out(i):=can_out(i-1)
    out_addr(i):=out_addr(i-1)
    out_repid(i):=out_repid(i-1)
    out_bufid(i):=out_bufid(i-1)
    out_outid(i):=out_outid(i-1)
  }
  can_out(k):=can_out(k-1)
  when(io.data_in.valid&&io.data_in.ready){
    for(i <- 0 until cycle_read){
      when(in_addr+i.asUInt<line_len){
        // write buffer with padding
        buf.write(in_outid + in_bufid + in_addr + i.asUInt, io.data_in.bits(i))
      }
    }
    when(in_addr + cycle_read.asUInt < line_len){
      in_addr := in_addr + cycle_read.asUInt
      in_fin:=false.B
    }.otherwise{
      in_addr := 0.U
      when(in_bufid =/= (buf_size*in_max_line-in_max_line).asUInt){
        in_bufid := in_bufid + in_max_line.asUInt
        in_fin := false.B
      }.otherwise{
        in_bufid := 0.U
        in_outid := (buf_size * in_max_line).asUInt - in_outid
        in_fin := true.B
      }
    }
    
  }.otherwise{
    in_fin:=false.B
  }
  for(i <- 0 until k){
    when(can_out(i)&&io.data_out.ready){
      when(out_addr(i)===out_line_len-1.U){
        out_addr(i):=0.U
        when(out_bufid(i)===buf_size.asUInt-1.U){
          out_bufid(i):=0.U
          when(out_repid(i)===io.config.buf_rep-1.U){
            out_repid(i):=0.U
            out_outid(i):= (buf_size * k *max_cks).asUInt - out_outid(i)
          }.otherwise{
            out_repid(i):=out_repid(i)+1.U
          }
        }.otherwise{
          out_bufid(i):=out_bufid(i)+1.U
        }
      }.otherwise{
        out_addr(i):=out_addr(i)+1.U
      }
    }.otherwise{
    }
    out_fin:=can_out(0)&&io.data_out.ready&&(out_addr(0)===out_line_len-1.U)&&(out_bufid(0)===buf_size.asUInt-1.U)&&(out_repid(0)===io.config.buf_rep-1.U)
    io.data_out.valid:=true.B
    io.data_out.bits(i).bits:=buf.read(out_outid(i) + out_bufid(i)*(k*max_cks).asUInt+out_line_len*i.asUInt+out_addr(i))
    io.data_out.bits(i).valid:=can_out(i+1)&&out_ready
  }
}
// output contains multiple tiles, each tile k(s)*h(x)
// result buffer has three dim: k * h * w
class Update_Result(x: Int, s: Int, num_tile_h: Int, num_tile_w: Int, ipc: Int, width: Int)extends Module{
  val io=IO(new Bundle{
    val config = Input(new ConvConfig())
    val data_in = Input(Vec(x, Valid(UInt(width.W))))
    val data_out = Output(Valid(Vec(ipc,UInt(width.W))))
  })
  //mem layout: each tile (s*x), tile (rep_h * rep_w) *(s*x)
  //input order: c * h * tile_w * (s*x)
  val mem = SyncReadMem(num_tile_h*num_tile_w*x*s,UInt(width.W) )
  val tile_c_id = RegInit(VecInit(Seq.fill(x)(0.U(24.W))))
  val tile_k_id = RegInit(VecInit(Seq.fill(x)(0.U(24.W))))
  val h_id = RegInit(VecInit(Seq.fill(x)(0.U(24.W))))
  val tile_w_id = RegInit(VecInit(Seq.fill(x)(0.U(24.W))))
  val inner_k = RegInit(VecInit(Seq.fill(x)(0.U(24.W))))
  val num_tiles = RegInit(100.U(24.W))
  val outer_h = RegInit(100.U(24.W))
  val update_data = RegInit(VecInit(Seq.fill(x)(0.U(width.W))))
  val read_data = RegInit(VecInit(Seq.fill(x)(0.U(width.W))))
  val update_addr = RegInit(VecInit(Seq.fill(x)(0.U(24.W))))
  val read_addr = RegInit(VecInit(Seq.fill(x)(0.U(24.W))))
  val update_valid = RegInit(VecInit(Seq.fill(x)(false.B)))
  val out_addr = RegInit(0.U(24.W))
  val out_h = RegInit(0.U(24.W))
  val out_w = RegInit(0.U(24.W))
  val buf_rem = RegInit(0.U(24.W))
  val in_fin = Wire(Bool())
  val out_fin = Wire(Bool())
  val out_valid = RegInit(false.B)
  // printf("input status\n")
  // printf("[%d %d %d %d %d %d %d] ",io.data_in(0).bits, io.data_in(0).valid, tile_w_id(0),h_id(0), tile_c_id(0), update_addr(0),update_data(0))
  // printf("\n")
  // printf("output:[%d %d %d]\n", out_h, out_w, out_addr)
  // printf("mem status\n")
  // for(i <- 0 until x){
  //   for(j <- 0 until s){
  //     printf("%d ", mem.read((i*s+j).asUInt))
  //   }
  //   printf("\n")
  // }
  // printf("\n")
  out_valid := (buf_rem > 0.U)
  buf_rem := buf_rem + in_fin - out_fin
  num_tiles :=(io.config.in_w + io.config.pad + io.config.pad - io.config.ks)/(s.asUInt)+1.U
  outer_h := io.config.in_h + io.config.pad + io.config.pad - io.config.ks + 1.U
  //printf("%d %d %d %d %d\n",outer_h, h_id(0), num_tile_w.asUInt, num_tiles,(num_tile_h*num_tile_w*x*s).asUInt)
  for(i <- 0 until x){
    update_valid(i):=io.data_in(i).valid
    //read_addr(i) := h_id(i) * (num_tile_w*s*x).asUInt + tile_w_id(i) * (s*x).asUInt + (i + (s-1) * x).asUInt - inner_k(i) * x.asUInt
    update_addr(i) := h_id(i) * (num_tile_w*s*x).asUInt + tile_w_id(i) * (s*x).asUInt + (i + (s-1) * x).asUInt - inner_k(i) * x.asUInt
    when(io.data_in(i).valid){
      when(inner_k(i) === (s-1).asUInt){
        inner_k(i) := 0.U
        when(tile_w_id(i) ===num_tiles - 1.U){
          tile_w_id(i) := 0.U
          when(h_id(i) === outer_h - 1.U){
            h_id(i) := 0.U
            when(tile_c_id(i) === io.config.c_tile_num - 1.U){
              tile_c_id(i) := 0.U
            }.otherwise{
              tile_c_id(i) := tile_c_id(i) + 1.U 
            }
          }.otherwise{
            h_id(i) := h_id(i) + 1.U
          }
        }.otherwise{
          tile_w_id(i) := tile_w_id(i) + 1.U
        }
      }.otherwise{
        inner_k(i) := inner_k(i) + 1.U
      }
      when(tile_c_id(i)=/=0.U){
        update_data(i) := read_data(i) + io.data_in(i).bits
      }.otherwise{
        update_data(i) := io.data_in(i).bits
      }
    }
    read_data(i) := mem.read(h_id(i) * (num_tile_w*s*x).asUInt + tile_w_id(i) * (s*x).asUInt + (i + (s-1) * x).asUInt - inner_k(i) * x.asUInt)
    when(update_valid(i)){
      mem.write(update_addr(i), update_data(i))
      
    }
  }
  // one buffer is ready to output
  when(inner_k(0) === (s-1).asUInt && tile_c_id(0) === io.config.c_tile_num - 1.U){
    in_fin := true.B
  }.otherwise{
    in_fin := false.B
  }
  when(buf_rem > 0.U){
    when(out_addr > (s*x-ipc).asUInt){
      out_addr := 0.U
      when(out_w > num_tiles - 1.U){
        out_w := 0.U
        when(out_h > outer_h - 1.U){
          out_h := 0.U
        }.otherwise{
          out_h := out_h + 1.U
        }
      }.otherwise{
        out_w := out_w + 1.U
      }
    }.otherwise{
      out_addr := out_addr + ipc.asUInt
    }
  }
  io.data_out.valid := out_valid
  for(i <- 0 until ipc){
    io.data_out.bits(i) := mem.read(out_h* (num_tile_w*s*x).asUInt + out_w* (s*x).asUInt + out_addr + (ipc+i).asUInt)
  }
  out_fin := out_addr >(s*x-ipc).asUInt
}