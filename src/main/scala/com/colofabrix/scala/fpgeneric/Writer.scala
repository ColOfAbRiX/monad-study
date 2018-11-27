package com.colofabrix.scala.fpgeneric

case class Writer[A, W]( x: A, mw: W )

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

  ///**
  //  * Implicit type converter for Monad[Writer]
  //  * Implements the real Monad behaviour for Writer
  //  */
  //type WriterW[T] = Writer[T, W]
  //
  //implicit val writerMonad: Monad[Writer] = new Monad[Writer]() {
  //  override def unit[A]( a: A ): Writer[A] = Writer( a, implicitly[Monoid[W]].mempty )
  //  override def bind[A, B]( ma: Writer[A] )( f: A => Writer[B] ): Writer[B] = {
  //    val newWriter = f( ma.x )
  //    Writer( newWriter.x, implicitly[Monoid[W]].mappend(ma.ys, newWriter.ys) )
  //  }
  //}
}
