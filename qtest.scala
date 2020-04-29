package systolic

import chisel3._
import chisel3.iotesters.{PeekPokeTester, Driver}
import scala.collection.mutable.ArrayBuffer
import scala.math.log10
import chisel3.util._

class QSample extends Module{
    val io = IO(new Bundle{
        val in = DeqIO(UInt(4.W))
        val out = EnqIO(UInt(4.W))
    })
    io.in.ready := true.B
    val q = Queue(io.in, 8)
    printf("%d\n", q.bits)
    io.out<>q
}
class Test_Q(c: QSample) extends PeekPokeTester(c){
    for(i <- 0 until 8){
        println("cycle")
        poke(c.io.in.bits, i)
        poke(c.io.in.valid, 1)
        poke(c.io.out.ready, i%2)
        print(peek(c.io.out.bits), peek(c.io.out.valid))
        println()
        step(1)
    }
}
class Mem
// object Test3 extends App {
//     Driver(() => new QSample())(c => new Test_Q(c))
// }