package com.colofabrix.scala.monads

import com.colofabrix.scala.generics.{ Functor, Monad }

case class Reader[R, A]( run: R => A )

object Reader {

  def ask[R]: Reader[R, R] = Reader( r => r )

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
    * Type class instance for Functor[Reader]
    * Implements the real Functor behaviour for Reader
    */
  implicit def readerFunctor[R] = new Functor[Reader[R, ?]] {
    override def fmap[A, B]( fa: Reader[R, A] )( f: A => B ): Reader[R, B] =
      Reader( r => f(fa.run(r)) )
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
