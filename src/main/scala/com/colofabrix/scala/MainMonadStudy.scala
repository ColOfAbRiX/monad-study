package com.colofabrix.scala

object MainMonadStudy extends App {

  // Stage 1: how monad emerges as general solution
  inventingmonads.Examples.run()

  // Stage 2: using Scala's language features to implement generic types
  monads.Examples.run()

  // Stage 3: get more comfortable with functional types working con comonads
  comonads.Examples.run()

}
