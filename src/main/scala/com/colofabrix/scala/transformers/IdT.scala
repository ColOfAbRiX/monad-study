package com.colofabrix.scala.transformers

import com.colofabrix.scala.generics.{ Functor, Monad }
import com.colofabrix.scala.monads.Id

/**
  * Id Option Monad Transformer
  */
case class IdT[M[_], A]( value: M[Id[A]] ) {

  def map[B]( f: A => B )( implicit m: Functor[M] ): IdT[M, B] =
    IdT( m.fmap(value){ x => Id(f(x.value)) } )

  def flatMap[B]( f: A => IdT[M, B] )( implicit m: Monad[M] ): IdT[M, B] =
    IdT( m.bind(value) { x => f(x.value).value } )

}
