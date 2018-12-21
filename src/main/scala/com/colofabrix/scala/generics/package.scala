package com.colofabrix.scala

package object generics {

  /**
    * A wrapper to simplify the usage of folding on monoids
    */
  def fold[A: Monoid]( xs: List[A] ): A = {
    val ma = implicitly[Monoid[A]]
    xs.foldLeft( ma.mempty )( ma.mappend )
  }

  /**
    * A wrapper to simplify the usage of foldLeft on monads
    */
  def foldLeft[M[_]: Monad, A, B]( ma: M[A] )( accumulator: A )( f: (A, A) => B ): M[B] = {
    val monad = implicitly[Monad[M]]
    monad.bind( ma ) { current =>
      monad.unit( f(accumulator, current) )
    }
  }

  /**
    * A wrapper to simplify the usage of bind on monads
    */
  def bind[M[_]: Monad, A, B]( x: M[A] )( f: A  => M[B] ): M[B] = {
    implicitly[Monad[M]].bind(x)(f)
  }

  /**
    * A wrapper to simplify the usage of bind on monads
    */
  def unit[M[_]: Monad, A]( a: A ): M[A] = {
    implicitly[Monad[M]].unit(a)
  }

  /**
    * A wrapper to simplify the usage of map on functors
    */
  def map[M[_]: Functor, A, B]( x: M[A] )( f: A => B ): M[B] = {
    implicitly[Functor[M]].fmap(x)(f)
  }

}
