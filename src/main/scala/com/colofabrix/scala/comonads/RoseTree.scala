package com.colofabrix.scala.comonads

import com.colofabrix.scala.generics.Comonad

case class RoseTree[+A]( value: A, children: List[RoseTree[A]] )

object RoseTree {

  implicit class RoseTreeOps[A]( rt: RoseTree[A] ) {
    def extract( implicit wid: Comonad[RoseTree] ): A =
      wid.extract( rt )

    def duplicate( implicit wid: Comonad[RoseTree] ): RoseTree[RoseTree[A]] =
      wid.duplicate( rt )

    def coflatMap[B]( f: RoseTree[A] => B )( implicit wid: Comonad[RoseTree] ): RoseTree[B] =
      wid.extend( rt )( f )

    def map[B]( f: A => B )( implicit wid: Comonad[RoseTree] ): RoseTree[B] =
      wid.fmap( rt )( f )
  }

  implicit def roseTreeComonad[A] = new Comonad[RoseTree] {
    override def extract[A]( wa: RoseTree[A] ): A = ???

    override def duplicate[A]( wa: RoseTree[A] ): RoseTree[RoseTree[A]] = ???

    override def fmap[A, B]( wa: RoseTree[A] )( f: A => B ): RoseTree[B] = ???
  }

}
