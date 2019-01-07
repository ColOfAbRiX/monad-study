package com.colofabrix.scala.comonads

sealed trait Tree[+A]
case class Node[+A]( value: A, left: Tree[A], right: Tree[A] ) extends Tree[A]
case class Leaf[+A]( a: A ) extends Node( a, Empty(), Empty() )
case object Empty extends Tree[Nothing]
