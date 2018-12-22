package com.colofabrix.scala.transformers

import com.colofabrix.scala.generics.{ Functor, Monad }
import com.colofabrix.scala.monads._

/**
  * Option Monad Transformer adapted from:
  *   https://blog.buildo.io/monad-transformers-for-the-working-programmer-aa7e981190e7
  * and
  *   https://github.com/typelevel/cats/blob/master/core/src/main/scala/cats/data/OptionT.scala
  */
case class OptionT[M[_], A]( value: M[Option[A]] ) {

  def map[B]( f: A => B )( implicit fm: Functor[M] ): OptionT[M, B] =
    OptionT( fm.fmap( value )( _.map(f) ) )

  def flatMap[B]( f: A => OptionT[M, B] )( implicit mm: Monad[M] ): OptionT[M, B] =
    OptionT(mm.bind(value) {
      case Some(x) => f(x).value
      case None => mm.unit(None)
    })

}
