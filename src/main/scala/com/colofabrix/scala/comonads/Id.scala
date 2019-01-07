package com.colofabrix.scala.comonads

import com.colofabrix.scala.generics.Comonad

case class Id[A]( value: A )

object Id {

  /**
    * Extending the base Id to add Comonad methods.
    * It's an incorporation of the below implicits into the Id ADT
    */
  implicit class IdOps[A]( id: Id[A] ) {
    def extract( implicit wid: Comonad[Id] ): A =
      wid.extract( id )

    def duplicate( implicit wid: Comonad[Id] ): Id[Id[A]] =
      wid.duplicate( id )

    def coflatMap[B]( f: Id[A] => B )( implicit wid: Comonad[Id] ): Id[B] =
      wid.extend( id )( f )

    def map[B]( f: A => B )( implicit wid: Comonad[Id] ): Id[B] =
      wid.fmap( id )( f )
  }

  /**
    * Type class instance for Comonad[Id]
    * Implements the real Comonad behaviour for Id
    */
  implicit def comonadId[A]: Comonad[Id] =
    new Comonad[Id] {

    override def extract[A]( wa: Id[A] ): A =
      wa.value

    override def duplicate[A]( wa: Id[A] ): Id[Id[A]] =
      Id( wa )

    override def fmap[A, B]( wa: Id[A] )( f: A => B ): Id[B] =
      Id( f(wa.value) )
  }

}
