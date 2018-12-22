package com.colofabrix.scala.comonads

import com.colofabrix.scala.generics.{ Comonad, Functor }

/**
  * Non Empty List
  */
case class NEL[A]( head: A, tail: Option[NEL[A]] ) {
  override def toString: String = {
    val h = head.toString.replaceAll("\n", "\n  ")

    val t = (tail match {
      case Some(x) => x.toString
      case None => "None"
    }).replaceAll("\n", "\n  ")

    s"\nH: $h\nT: $t"
  }
}

object NEL {

  /*
   Commodity constructors
   */
  def apply[A]( head: A ) = new NEL( head, None )
  def apply[A]( head: A, tail: NEL[A] ) = new NEL(head, Some(tail) )

  /**
    * Extending the base NEL to add methods.
    * It's an incorporation of the below implicits into the Writer ADT
    */
  implicit class NELOps[A]( nel: NEL[A] ) {
    def extract( implicit wnel: Comonad[NEL] ): A =
      wnel.extract( nel )

    def duplicate( implicit wnel: Comonad[NEL] ): NEL[NEL[A]] =
      wnel.duplicate( nel )

    def coflatMap[B]( f: NEL[A] => B )( implicit wnel: Comonad[NEL] ): NEL[B] =
      wnel.extend( nel )( f )

    def map[B]( f: A => B )( implicit wnel: Functor[NEL] ): NEL[B] =
      wnel.fmap( nel )( f )

    def foldLeft( a: A )( f: (A, A) => A ): A = nel.tail match {
      case Some(t) => t.foldLeft( f(a, nel.head) )( f )
      case None => f( a, nel.head )
    }
  }

  /**
    * Type class instance for Comonad[NEL]
    * Implements the real Comonad behaviour for NEL
    */
  implicit def nelComonad: Comonad[NEL] = new Comonad[NEL] {
    override def extract[A]( wnel: NEL[A] ): A = wnel.head

    override def duplicate[A]( wnel: NEL[A] ): NEL[NEL[A]] =
      NEL( wnel, wnel.tail.map( duplicate ) )

    override def fmap[A, B]( wnel: NEL[A] )( f: A => B ): NEL[B] =
      NEL( f(wnel.head), wnel.tail.map( fmap(_)(f) ) )
  }

}
