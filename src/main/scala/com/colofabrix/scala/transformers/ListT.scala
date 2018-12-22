package com.colofabrix.scala.transformers

import com.colofabrix.scala.generics.{ Functor, Monad }

case class ListT[M[_], A]( value: M[List[A]]) {

  def map[B]( f: A => B )( implicit fl: Functor[M] ): ListT[M, B] =
    ListT( fl.fmap( value )( _.map(f) ) )

  //def flatMap[B]( f: A => ListT[M, B] )( implicit ml: Monad[M] ): ListT[M, B] =
  //  ListT( ml.bind(value)(xs =>
  //    xs.flatMap( x =>  )
  //  ))

}
