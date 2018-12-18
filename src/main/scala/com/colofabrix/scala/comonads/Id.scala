package com.colofabrix.scala.comonads

import com.colofabrix.scala.monads.{ Functor, Monad }

case class Id[A]( value: A )

object Id {

  /**
    * Extending the base Writer to add Monad methods.
    * It's an incorporation of the below implicits into the Writer ADT
    */
  implicit class IdOps[A]( id: Id[A] ) {
    def map[B]( f: A => B )( implicit fi: Functor[Id] ): Id[B] = fi.fmap(id)(f)
    def flatMap[B]( f: A => Id[B] )( implicit mi: Monad[Id] ): Id[B] = mi.bind(id)(f)
    def extract( implicit wi: Comonad[Id] ): A = wi.extract( this.id )
    def duplicate( implicit wi: Comonad[Id] ): Id[Id[A]] = wi.duplicate( this.id )
  }

  /**
    * Implicit type converter for Monad[Id]
    * Implements the real Monad behaviour for Id
    */
  implicit def comonadId[A]: Comonad[Id] = new Comonad[Id] {
    override def extract[A]( wa: Id[A] ): A = wa.value
    override def duplicate[A]( wa: Id[A] ): Id[Id[A]] = Id(wa)
    override def fmap[A, B]( wa: Id[A] )( f: A => B ): Id[B] = Id(f(wa.value))
  }

}
