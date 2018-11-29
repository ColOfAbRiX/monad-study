package com.colofabrix.scala.monads

/**
  * Writer Data Type
  */
case class Writer[A, W]( value: A, log: W )

object Writer {

  /**
    * Extending the base Writer to add Monad methods.
    * It's an incorporation of the below implicits into the Writer ADT
    */
  implicit class WriterOps[A, W: Monoid]( w: Writer[A, W] ) {
    type WriterW[J] = Writer[J, W]

    def map[B]( f: A => B )( implicit fw: Functor[WriterW] ): WriterW[B] = {
      fw.fmap( w )( f )
    }

    def flatMap[B]( f: A => WriterW[B] )( implicit mw: Monad[WriterW] ): WriterW[B] = {
      mw.bind( w )( f )
    }
  }

  /**
    * Implicit type converter for Monad[Writer]
    * Implements the real Monad behaviour for Writer
    */
  implicit def writerMonad[W: Monoid] =
    new Monad[({ type WriterW[J] = Writer[J, W] })#WriterW]() {
      // Used this feature:
      //   https://github.com/fpinscala/fpinscala/blob/7a43335a04679e140c8c4cf7c359fd8a39bbe39f/answers/src/main/scala/fpinscala/monads/Monad.scala#L133

      override def unit[A]( a: A ): Writer[A, W] = Writer( a, implicitly[Monoid[W]].mempty )

      override def bind[A, B]( ma: Writer[A, W] )( f: A => Writer[B, W] ): Writer[B, W] = {
        val newWriter = f( ma.value )
        Writer(
          newWriter.value,
          implicitly[Monoid[W]].mappend(ma.log, newWriter.log)
        )
      }
    }

}
