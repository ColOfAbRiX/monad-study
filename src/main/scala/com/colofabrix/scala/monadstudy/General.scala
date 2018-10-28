package com.colofabrix.scala.monadstudy

trait Monoid[A] {
  def mempty: A
  def mappend(x: A, y: A): A
}
//
//trait Monad[M[_]] {
//  def bind[A, B]( f: A => M[B] ): M[B]
//  def unit[A]( a: A ): M[A]
//  def fmap[A, B]( f: A => B ): M[B]
//}
//
//sealed trait Option[+A] extends Monad[Option]
//case object None extends Option[Nothing] {
//  def bind[A, B]( f: A => Option[B] ): Option[B] = None
//  def unit[A]( a: A ): Option[A] = None
//  def fmap[A, B]( f: A => B ): Option[B] = None
//}
//case class Some[+A]( value: A ) extends Option[A] {
//  def bind[A, B]( f: A => Option[B] ): Option[B] = None
//  def unit[A]( a: A ): Option[A] = None
//  def fmap[A, B]( f: A => B ): Option[B] = Some( f(value) )
//}
