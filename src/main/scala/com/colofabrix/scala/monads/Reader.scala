package com.colofabrix.scala.monads

import com.colofabrix.scala.generics.{ Functor, Monad }

case class Reader[R, A]( run: R => A )

object Reader {

  /**
    * Extending the base Reader to add Monad methods.
    * It's an incorporation of the below implicits into the Reader ADT
    */
  implicit class ReaderOps[R, A]( reader: Reader[R, A] ) {
    type ReaderS[S] = Reader[S, A]

    def map[B]( f: R => B )( implicit fr: Functor[ReaderS] ): ReaderS[B] =
      fr.fmap( reader )( f )

    def flatMap[B]( f: R => ReaderS[B] )( implicit mr: Monad[ReaderS] ): ReaderS[B] =
      mr.bind( reader )( f )
  }

  /**
    * Type class instance for Monad[Reader]
    * Implements the real Monad behaviour for Reader
    */
  implicit def readerMonad[R] =
    // This time I use a special compile plugin for the kind projector
    new Monad[Reader[R, ?]]() {
      override def unit[A]( a: A ): Reader[R, A] =
        Reader( _ => a )

      override def bind[A, B]( mr: Reader[R, A] )( f: A => Reader[R, B] ): Reader[R, B] =
        Reader({ r =>
          f( mr.run(r) ).run( r )
        })
    }

}
