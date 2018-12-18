package com.colofabrix.scala.comonads

case class Id[A]( value: A )

object Id {

  /**
    * Extending the base Writer to add Monad methods.
    * It's an incorporation of the below implicits into the Writer ADT
    */
  implicit class IdOps[A]( id: Id[A] ) {
    def extract( implicit wi: Comonad[Id] ): A = wi.extract( id )
    def duplicate( implicit wi: Comonad[Id] ): Id[Id[A]] = wi.duplicate( id )
    def coflatMap[B]( f: Id[A] => B )( implicit wi: Comonad[Id] ): Id[B] = wi.extend( id )( f )
    def map[B]( f: A => B )( implicit wi: Comonad[Id] ): Id[B] = wi.fmap( id )( f )
  }

  /**
    * Implicit type converter for Monad[Id]
    * Implements the real Monad behaviour for Id
    */
  implicit def comonadId[A]: Comonad[Id] = new Comonad[Id] {
    override def extract[A]( wa: Id[A] ): A = wa.value
    override def duplicate[A]( wa: Id[A] ): Id[Id[A]] = Id( wa )
    override def fmap[A, B]( wa: Id[A] )( f: A => B ): Id[B] = Id( f(wa.value) )
  }

}
