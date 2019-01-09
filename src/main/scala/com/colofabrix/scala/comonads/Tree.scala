package com.colofabrix.scala.comonads

sealed trait Tree[+A]
case class Node[+A]( value: A, left: Tree[A], right: Tree[A] ) extends Tree[A]
case object Empty extends Tree[Nothing]
