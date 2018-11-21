package com.colofabrix.scala.fpgeneric

case class Writer[+A, B: Monoid]( x: A, ys: B )

class WriterW[W: Monoid] {

  type WriterW[A] = Writer[A, W]

  /**
    * Extending the base WriterW to add Monad methods.
    * It's an incorporation of the below implicits into the Writer ADT
    */
  implicit class WriterWOps[A]( w: WriterW[A] ) {
    def map[B]( f: A => B )( implicit mw: Monad[WriterW] ): WriterW[B] = {
      mw.fmap( w )( f )
    }
    def flatMap[B]( f: A => WriterW[B] )( implicit mw: Monad[WriterW] ): WriterW[B] = {
      mw.bind( w )( f )
    }
  }

  /**
    * Implicit type converter for Monad[WriterW]
    * Implements the real Monad behaviour for WriterW
    */
  implicit val writerMonad: Monad[WriterW] = new Monad[WriterW]() {
    override def unit[A]( a: A ): WriterW[A] = Writer( a, implicitly[Monoid[W]].mempty )
    override def bind[A, B]( ma: WriterW[A] )( f: A => WriterW[B] ): WriterW[B] = {
      val newWriter = f( ma.x )
      Writer( newWriter.x, implicitly[Monoid[W]].mappend(ma.ys, newWriter.ys) )
    }
  }

}
