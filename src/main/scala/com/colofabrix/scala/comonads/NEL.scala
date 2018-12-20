package com.colofabrix.scala.comonads

/**
  * Non Empty List
  */
case class NEL[+A]( head: A, tail: Option[NEL[A]] )

object NEL {

  implicit def nelComonad[A]: Comonad[NEL] = new Comonad[NEL] {
    override def extract[A]( wnel: NEL[A] ): A = wnel.head

    override def duplicate[A]( wnel: NEL[A] ): NEL[NEL[A]] =
      NEL( wnel, wnel.tail.map( dumplicate ) )

    override def fmap[A, B]( wnel: NEL[A] )( f: A => B ): NEL[B] = wnel match {
      case NEL( head, None ) => NEL( f(head), None )
      case NEL( head, Some(tail) ) => NEL( f( head ), Some( fmap(tail)(f) ) )
    }
  }

}
