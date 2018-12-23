package com.colofabrix.scala.transformers

import com.colofabrix.scala.generics.{ Functor, Monad }

case class ListT[M[_], A]( value: M[List[A]]) {

  def map[B]( f: A => B )( implicit fl: Functor[M] ): ListT[M, B] =
    ListT( fl.fmap( value )( _.map(f) ) )

  def flatMap[B]( f: A => ListT[M, B] )( implicit ml: Monad[M] ): ListT[M, B] =
    ListT( ml.bind(value) {
      case Nil => ml.unit(Nil)
      case list =>
        list.map(f(_).value)
            .reduce { (accumulator, current) =>
              ml.bind(accumulator) { x =>
                ml.fmap(current) { _ ++ x }
              }
            }
    })

}
