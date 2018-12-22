package com.colofabrix.scala.monads

import com.colofabrix.scala.generics.{ Functor, Monad, Monoid }

case class Id[A]( value: A )

/**
  * Identity Data Type
  */
object Id {

  /**
    * Extending the base Writer to add Monad methods.
    * It's an incorporation of the below implicits into the Writer ADT
    */
  implicit class IdOps[A]( id: Id[A] ) {
    def map[B]( f: A => B )( implicit fi: Functor[Id] ): Id[B] = fi.fmap(id)(f)
    def flatMap[B]( f: A => Id[B] )( implicit mi: Monad[Id] ): Id[B] = mi.bind(id)(f)
  }

  /**
    * Type class instance for Monoid[Id]
    * Implements the real Monoid behaviour for Id
    */
  implicit def monoidId[A: Monoid]: Monoid[Id[A]] = new Monoid[Id[A]] {
    private val ma = implicitly[Monoid[A]]
    override def mempty: Id[A] = Id( ma.mempty )
    override def mappend( x: Id[A], y: Id[A] ): Id[A] = Id( ma.mappend(x.value, y.value) )
  }

  /**
    * Type class instance for Monad[Id]
    * Implements the real Monad behaviour for Id
    */
  implicit val monadId: Monad[Id] = new Monad[Id] {
    override def unit[A]( a: A ): Id[A] = Id(a)
    override def bind[A, B]( ma: Id[A] )( f: A => Id[B] ): Id[B] = f(ma.value)
  }

}
