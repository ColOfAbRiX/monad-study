//package com.colofabrix.scala
//
//import com.colofabrix.scala.generics.Comonad
//
///*
//http://blog.sigfpe.com/2006/12/evaluating-cellular-automata-is.html
// */
//object tmp {
//
//  case class U[X](left: Stream[X], center: X, right: Stream[X]) {
//    def shiftRight = this match {
//      case U(a, b, c #:: cs) => U(b #:: a, c, cs)
//    }
//
//    def shiftLeft = this match {
//      case U(a #:: as, b, c) => U(as, a, b #:: c)
//    }
//  }
//
//  // Not necessary, as Comonad also has fmap.
//  /*
//  implicit object uFunctor extends Functor[U] {
//    def fmap[A, B](x: U[A], f: A => B): U[B] = U(x.left.map(f), f(x.center), x.right.map(f))
//  }
//  */
//
//  implicit object uComonad extends Comonad[U] {
//    def copure[A](u: U[A]): A = u.center
//    def cojoin[A](a: U[A]): U[U[A]] = U(Stream.iterate(a)(_.shiftLeft).tail, a, Stream.iterate(a)(_.shiftRight).tail)
//    def fmap[A, B](x: U[A], f: A => B): U[B] = U(x.left.map(f), x.center |> f, x.right.map(f))
//  }
//
//  def rule(u: U[Boolean]) = u match {
//    case U(a #:: _, b, c #:: _) => !(a && b && !c || (a == b))
//  }
//
//  def shift[A](i: Int, u: U[A]) = {
//    Stream.iterate(u)(x => if (i < 0) x.shiftLeft else x.shiftRight).apply(i.abs)
//  }
//
//  def half[A](u: U[A]) = u match {
//    case U(_, b, c) => Stream(b) ++ c
//  }
//
//  def toList[A](i: Int, j: Int, u: U[A]) = half(shift(i, u)).take(j - i)
//
//  val u = U(Stream continually false, true, Stream continually false)
//
//  val s = Stream.iterate(u)(_ =>> rule)
//
//  val s0 = s.map(r => toList(-20, 20, r).map(x => if(x) '#' else ' '))
//
//  val s1 = s.map(r => toList(-20, 20, r).map(x => if(x) '#' else ' ').mkString("|")).take(20).force.mkString("\n")
//
//  println(s1)
//
//}
