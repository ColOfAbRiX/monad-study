package com.colofabrix.scala.fpgeneric

case class Writer[+A, B: Monoid]( x: A, ys: B )

class WriterW[W: Monoid] {

  type WriterW[A] = Writer[A, W]

}
