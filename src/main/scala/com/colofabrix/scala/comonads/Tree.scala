package com.colofabrix.scala.comonads

sealed trait Tree[A]
case class Node[A]( value: A, left: List[Tree[A]], right: List[Tree[A]] )
case object Empty extends Tree[Nothing]
