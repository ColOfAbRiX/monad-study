package com.colofabrix.scala.fpgeneric

case class Writer[+A, B: Monoid]( x: A, ys: B )

class WriterW[W: Monoid] {

  type WriterW[A] = Writer[A, W]

  implicit class WriterWOps[A]( w: WriterW[A] ) {
    def map[B]( f: A => B )( implicit mw: Monad[WriterW] ): WriterW[B] = {
      mw.fmap( w )( f )
    }
    def flatMap[B]( f: A => WriterW[B] )( implicit mw: Monad[WriterW] ): WriterW[B] = {
      mw.bind( w )( f )
    }
  }

}
