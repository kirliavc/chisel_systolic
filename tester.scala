package systolic

import chisel3._
import chisel3.iotesters.{PeekPokeTester, Driver}
import scala.collection.mutable.ArrayBuffer
import scala.math.log10
import chisel3.util._
// k, c, ks, ks = x*4*3*3
// w*c*(ks-1)/cycle_read
// in_w = 24, c=4, 96 items, 24 cycles
class TestInst(c: Systolic_Rect) extends PeekPokeTester(c){
  val cycle_read_input = 4
  val cycle_read_kernel = 9
  val cycle_out_res = 4
  // poke(c.io.config.in_h, 5)
  // poke(c.io.config.in_w, 12)
  // poke(c.io.config.c, 4)
  // poke(c.io.config.ks, 3)
  // poke(c.io.config.pad, 0)
  poke(c.io.a_in.valid, false)
  poke(c.io.b_in.valid, false)
  // poke(c.io.config.stride, 1)
  // poke(c.io.config.c_tile_num, 1)
  // push config instruction

  poke(c.io.inst.valid, 1)
  poke(c.io.inst.bits.funct, 8)
  poke(c.io.inst.bits.rs1, 5)
  poke(c.io.inst.bits.rs2, 12)
  step(1)
  poke(c.io.inst.valid, 1)
  poke(c.io.inst.bits.funct, 9)
  poke(c.io.inst.bits.rs1, 4)
  poke(c.io.inst.bits.rs2, 3)
  step(1)
  // push three input instruction
  poke(c.io.inst.valid, 1)
  poke(c.io.inst.bits.funct, 1)
  poke(c.io.inst.bits.rs1, 0)
  step(1)
  poke(c.io.inst.bits.funct, 1)
  poke(c.io.inst.bits.rs1, 1)
  step(1)
  poke(c.io.inst.bits.funct, 1)
  poke(c.io.inst.bits.rs1, 2)
  step(1)
  // push filter load inst
  poke(c.io.inst.bits.funct, 2)
  poke(c.io.inst.bits.rs1, 1)
  step(1)
  // push compute inst
  poke(c.io.inst.bits.funct, 3)
  poke(c.io.inst.bits.rs1, 0)
  poke(c.io.inst.bits.rs2, 1)
  poke(c.io.inst.bits.rd, 0)
  step(1)

  // push free-output inst
  poke(c.io.inst.bits.funct, 6)
  poke(c.io.inst.bits.rs1, 0)
  poke(c.io.inst.bits.rs2, 1)
  poke(c.io.inst.bits.rd, 0)
  step(1)

  // push output inst
  poke(c.io.inst.bits.funct, 7)
  poke(c.io.inst.bits.rs1, 0)
  step(1)
  poke(c.io.inst.valid, 0)


  val > = Array
  val mat_in = Array.ofDim[Int](5, 4, 12)
  val mat_pad = Array.fill(7, 4, 14)(0)
  val mat_fil = Array.fill(20, 4, 9)(0)
  val mat_out = Array.ofDim[Int](5, 8, 12)
  val rand = scala.util.Random
  // input: 2*4*12
  // filter: 8*4*9
  Console.printf("mat_in\n")
  for(i <- 0 until 7){
    for(j <- 0 until 4){
      for(k <- 0 until 12){
        mat_pad(i)(j)(k)=rand.nextInt(3)//k%3
        Console.printf("%d ",mat_pad(i)(j)(k))
      }
      Console.printf("\n")
    }
    Console.printf("\n")
  }
  // Console.printf("mat_pad\n")
  // for(i <- 0 until 5){
  //   for(j <- 0 until 4){
  //     for(k <- 0 until 12){
  //       mat_pad(i+1)(j)(k+1)=mat_in(i)(j)(k)
  //     }
  //   }
  // }
  // for(i <- 0 until 4){
  //   for(j <- 0 until 7){
  //     for(k <- 0 until 14){
  //       Console.printf("%d ",mat_pad(j)(i)(k))
  //     }
  //     Console.printf("\n")
  //   }
  //   Console.printf("\n")
  // }
  Console.printf("mat_filter\n")
  for(i <- 0 until 8){
    for(j <- 0 until 4){
      for(k <- 0 until 9){
        mat_fil(i)(j)(k)=rand.nextInt(3)
        Console.printf("%d ",mat_fil(i)(j)(k))
      }
      Console.printf("\n")
    }
    Console.printf("\n")
  }
  Console.printf("mat_out\n")
  for(i <- 0 until 5){
    for(j <- 0 until 8){
      for(k <- 0 until 10){
        mat_out(i)(j)(k)=0
        for(l <- 0 until 4){
          for(m <- 0 until 3){
            for(n <- 0 until 3){
              mat_out(i)(j)(k)=mat_out(i)(j)(k)+mat_pad(i+m)(l)(k+n)*mat_fil(j)(l)(m*3+n)
            }
          }
        }
        Console.printf("%d ",mat_out(i)(j)(k))
      }
      Console.printf("\n")
    }
    Console.printf("\n")
  }
  var idx_input = 0
  var idx_filter = 0
  for(i <- 0 until 84){

    if(peek(c.io.b_in.ready).intValue==1){
      for(k <- 0 until cycle_read_input){
        // h c w
        poke(c.io.b_in.bits(k),mat_pad(idx_input/48)((idx_input%48)/12)(idx_input%12))
        idx_input=idx_input+1
      }
    }
    if(peek(c.io.a_in.ready).intValue==1){
      for(k <- 0 until cycle_read_kernel){
        poke(c.io.a_in.bits(k),mat_fil(idx_filter/36)((idx_filter%36)/9)(idx_filter%9))
        idx_filter = idx_filter + 1
      }
    }
    poke(c.io.a_in.valid, true)
    poke(c.io.b_in.valid, true)
    step(1)
    print(peek(c.io.c_out.bits),peek(c.io.c_out.valid))
    println()
  }
  for(i <- 0 until 240){
    for(k <- 0 until cycle_read_input){
      poke(c.io.b_in.bits(k),0)
    }
    for(k <- 0 until cycle_read_kernel){
      poke(c.io.a_in.bits(k),0)
    }
    poke(c.io.a_in.valid, false)
    poke(c.io.b_in.valid, false)
    step(1)
    print(peek(c.io.c_out.bits),peek(c.io.c_out.valid))
    println()
  }
}
// class Test_Input(c: DFSysIn_Input) extends PeekPokeTester(c){
//   poke(c.io.config.in_h, 5)
//   poke(c.io.config.in_w, 24)
//   poke(c.io.config.c, 4)
//   poke(c.io.config.ks, 3)
//   poke(c.io.config.pad, 1)
//   poke(c.io.config.stride, 1)
//   poke(c.io.config.c_tile_num, 1)
//   // case: C*ks*ks cycle, read a line=C*(W+ks-1)
//   // buffer: (ks+1)*C*(W+ks-1)=4*2*18
//   // output: 
//   val cycle_read_num = 2
//   var num_s =0
//   for(j <- 0 until 24){
//     for(i <- 0 until 12){
      
//       while(peek(c.io.data_in.ready).intValue==0){
//         println(num_s.toString)
//         num_s = num_s + 1
//         step(1)
//         for(k <- 0 until 16){
//           print(peek(c.io.data_out.bits(k).bits)," ")
//         }
//       }
        
//       //print(peek(c.io.data_in.valid),j*12+i)
//       poke(c.io.data_in.bits(0), i*2+1)
//       poke(c.io.data_in.bits(1), i*2+2)
//       poke(c.io.data_in.valid, 1)
//       println(num_s.toString)
//       num_s = num_s + 1
//       step(1)
//       for(k <- 0 until 16){
//         print(peek(c.io.data_out.bits(k).bits)," ")
//       }
//       println()
//     }
//   }
//   // output: C(1, 4), C(2, 4), C(3, 4), C(4, 4), C(1, 3), C(2, 3), C(3, 3), C(4, 3)...
// }

// class Test_InKernel(c: DFSysIn_Kernel) extends PeekPokeTester(c){
//   poke(c.io.config.in_h, 5)
//   poke(c.io.config.in_w, 24)
//   poke(c.io.config.c, 4)
//   poke(c.io.config.ks, 3)
//   poke(c.io.config.pad, 1)
//   poke(c.io.config.stride, 1)
//   // case: C*ks*ks cycle, read a line=C*(W+ks-1)
//   // buffer: (ks+1)*C*(W+ks-1)=4*2*18
//   // output: 
//   val cycle_read_num = 8
//   var num_s =0
//   poke(c.io.data_out.ready, 1)
//   for(j <- 0 until 30){
//     for(i <- 0 until 9){
      
//       while(peek(c.io.data_in.ready).intValue==0){
//         println(num_s.toString)
//         num_s = num_s + 1
//         step(1)
//         for(k <- 0 until 16){
//           print(peek(c.io.data_out.bits(k).bits),peek(c.io.data_out.bits(k).valid))
//         }
//       }
        
//       //print(peek(c.io.data_in.valid),j*12+i)
//       for(k <- 0 until cycle_read_num)
//         poke(c.io.data_in.bits(k), i*8+k)
//       poke(c.io.data_in.valid, 1)
//       println(num_s.toString)
//       num_s = num_s + 1
//       step(1)
//       for(k <- 0 until 16){
//         print(peek(c.io.data_out.bits(k).bits),peek(c.io.data_out.bits(k).valid))
//       }
//       println()
//     }
//   }
//   // output: C(1, 4), C(2, 4), C(3, 4), C(4, 4), C(1, 3), C(2, 3), C(3, 3), C(4, 3)...
// }
//class Systolic_Rect(s: Int, x: Int, max_input_len: Int, max_c: Int, max_ks: Int, cycle_read_input: Int, cycle_read_kernel: Int, m: Int, n: Int, width: Int)
//16, 16, 256, 8, 3, 2, 8, 1, 1, 8
// 
// class Test_Systolic_ks3p1(c: Systolic_Rect) extends PeekPokeTester(c){
//   val > = Array
//   val mat_in = Array.ofDim[Int](5, 4, 12)
//   val mat_pad = Array.fill(7, 4, 14)(0)
//   val mat_fil = Array.fill(20, 4, 9)(0)
//   val mat_out = Array.ofDim[Int](5, 8, 12)
//   val rand = scala.util.Random
//   // input: 2*4*12
//   // filter: 8*4*9
//   Console.printf("mat_in\n")
//   for(i <- 0 until 5){
//     for(j <- 0 until 4){
//       for(k <- 0 until 12){
//         mat_in(i)(j)(k)=rand.nextInt(3)//k%3
//         Console.printf("%d ",mat_in(i)(j)(k))
//       }
//       Console.printf("\n")
//     }
//     Console.printf("\n")
//   }
//   Console.printf("mat_pad\n")
//   for(i <- 0 until 5){
//     for(j <- 0 until 4){
//       for(k <- 0 until 12){
//         mat_pad(i+1)(j)(k+1)=mat_in(i)(j)(k)
//       }
//     }
//   }
//   for(i <- 0 until 4){
//     for(j <- 0 until 7){
//       for(k <- 0 until 14){
//         Console.printf("%d ",mat_pad(j)(i)(k))
//       }
//       Console.printf("\n")
//     }
//     Console.printf("\n")
//   }
//   Console.printf("mat_filter\n")
//   for(i <- 0 until 8){
//     for(j <- 0 until 4){
//       for(k <- 0 until 9){
//         mat_fil(i)(j)(k)=rand.nextInt(3)
//         Console.printf("%d ",mat_fil(i)(j)(k))
//       }
//       Console.printf("\n")
//     }
//     Console.printf("\n")
//   }
//   Console.printf("mat_out\n")
//   for(i <- 0 until 5){
//     for(j <- 0 until 8){
//       for(k <- 0 until 12){
//         mat_out(i)(j)(k)=0
//         for(l <- 0 until 4){
//           for(m <- 0 until 3){
//             for(n <- 0 until 3){
//               mat_out(i)(j)(k)=mat_out(i)(j)(k)+mat_pad(i+m)(l)(k+n)*mat_fil(j)(l)(m*3+n)
//             }
//           }
//         }
//         Console.printf("%d ",mat_out(i)(j)(k))
//       }
//       Console.printf("\n")
//     }
//     Console.printf("\n")
//   }
//   poke(c.io.config.in_h, 5)
//   poke(c.io.config.in_w, 12)
//   poke(c.io.config.c, 4)
//   poke(c.io.config.ks, 3)
//   poke(c.io.config.pad, 1)
//   poke(c.io.config.stride, 1)
//   poke(c.io.config.buf_rep, 100)  //always repeat filter
//   poke(c.io.config.input_cycle, 50)
//   poke(c.io.config.c_tile_num, 2)
//   val cycle_read_input = 4
//   val cycle_read_kernel = 9
//   val cycle_out_res = 4
//   var idx_input = 0
//   var idx_filter = 0
//   for(i <- 0 until 70){
//     //Console.printf("input: %d \n",peek(c.io.b_in.ready).intValue)
//     if(peek(c.io.b_in.ready).intValue==1){
//       for(k <- 0 until cycle_read_input){
        
//         //Console.printf("%d ",mat_in(idx_input/48)((idx_input%48)/12)(idx_input%12))
//         poke(c.io.b_in.bits(k),mat_in(idx_input/48)((idx_input%48)/12)(idx_input%12))
//         idx_input=idx_input+1
//       }
//       //Console.printf("\n")
//     }
//     if(peek(c.io.a_in.ready).intValue==1){
//       for(k <- 0 until cycle_read_kernel){
        
//         poke(c.io.a_in.bits(k),mat_fil(idx_filter/36)((idx_filter%36)/9)(idx_filter%9))
//         idx_filter = idx_filter + 1
//       }
      
//     }
    
//     poke(c.io.a_in.valid, true)
//     poke(c.io.b_in.valid, true)
//     step(1)
//     print(peek(c.io.c_out.bits),peek(c.io.c_out.valid))
//     println()
//   }
//   for(i <- 0 until 120){
//     for(k <- 0 until cycle_read_input){
//       poke(c.io.b_in.bits(k),0)
//     }
//     for(k <- 0 until cycle_read_kernel){
//       poke(c.io.a_in.bits(k),0)
//     }
//     poke(c.io.a_in.valid, false)
//     poke(c.io.b_in.valid, false)
//     step(1)
//     print(peek(c.io.c_out.bits),peek(c.io.c_out.valid))
//     println()
//   }
// }
//Systolic_Rect(s: Int, x: Int, max_input_w: Int, max_input_h: Int, max_c: Int, max_ks: Int, cycle_read_input: Int, cycle_read_kernel: Int, cycle_out_res: Int, m: Int, n: Int, width: Int)
object Test2 extends App {
  //Driver(() => new Systolic_Rect(s=4,x=4,max_input_w=16, max_input_h=16, max_c=4, max_ks=5, cycle_read_input=4, cycle_read_kernel=9, cycle_out_res=4, m=1, n=1, width=8))(c => new TestInst(c))
  //Driver(() => new Systolic_Rect(4, 4, 16, 4, 4, 1, 1, 16))(c => new Test_Res1(c))
  //Driver(() => new DFSysIn_Input(16, 256, 64, 5, 2, 8))(c => new Test_Input(c))
  //Driver(() => new DFSysIn_Kernel(16, 64, 1, 8, 8))(c => new Test_InKernel(c))
  chisel3.Driver.execute(args, () => new Systolic_Rect(s=16,x=16,max_input_w=32, max_input_h=16, max_c=4, max_ks=5, cycle_read_input=4, cycle_read_kernel=9, cycle_out_res=4, m=8, n=8, width=8))
  //chisel3.Driver.execute(args, () => new Systolic_Rect(8,8,16,16,4, 5, 4, 9, 4, 1, 1, 8))
  //chisel3.Driver.execute(args, () => new Systolic_Rect(16, 8, 128, 16, 8, 1, 8, 32))
  //chisel3.Driver.execute(args, () => new NonSystolic(16, 16, 128, 16, 1, 8, 16))
}