package com.colofabrix.scala.fpgeneric

case class Writer[A, W]( value: A, log: W )

object Writer {
  /**
    * Extending the base Writer to add Monad methods.
    * It's an incorporation of the below implicits into the Writer ADT
    */
  implicit class WriterOps[A, W: Monoid]( w: Writer[A, W] ) {
    type WriterW[J] = Writer[J, W]

    def map[B]( f: A => B )( implicit mw: Monad[WriterW] ): WriterW[B] = {
      mw.fmap( w )( f )
    }

    def flatMap[B]( f: A => WriterW[B] )( implicit mw: Monad[WriterW] ): WriterW[B] = {
      mw.bind( w )( f )
    }
  }

  /**
    * Implicit type converter for Monad[Writer]
    * Implements the real Monad behaviour for Writer
    */
  implicit def writerMonad[W: Monoid] = new Monad[({type WriterW[J] = Writer[J, W]})#WriterW]() {
    override def unit[A]( a: A ): Writer[A, W] = Writer( a, implicitly[Monoid[W]].mempty )

    override def bind[A, B]( ma: Writer[A, W] )( f: A => Writer[B, W] ): Writer[B, W] = {
      val newWriter = f( ma.value )
      Writer( newWriter.value, implicitly[Monoid[W]].mappend(ma.log, newWriter.log) )
    }
  }
}
