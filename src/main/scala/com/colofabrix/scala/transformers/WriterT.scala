package com.colofabrix.scala.transformers

import com.colofabrix.scala.generics._
import com.colofabrix.scala.monads._
import com.colofabrix.scala.monads.Writer._

case class WriterT[M[_], A, W: Monoid]( value: M[Writer[A, W]] ) {

  def map[B]( f: A => B )( implicit m: Functor[M] ): WriterT[M, B, W] =
    WriterT(m.fmap(value)(_.map(f)))

  def flatMap[B]( f: A => WriterT[M, B, W] )( implicit m: Monad[M] ): WriterT[M, B, W] =
    WriterT( m.bind(value)( w => f(w.value).value ) )

}
