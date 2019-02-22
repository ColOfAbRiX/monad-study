package com.colofabrix.scala.trampolines

trait Trampoline[A]

case class Done[A](value: A) extends Trampoline[A]

case class More[A](run: () => Trampoline[A]) extends Trampoline[A]

object Trampoline {
  def run[A](trampoline: Trampoline[A]): A = trampoline match {
    case Done(v) => v
    case More(t) => run(t())
  }
}
