package com.colofabrix.scala

object MainMonadStudy extends App {

  // Stage 1: how monad emerges as general solution
  inventingmonads.Examples.run()

  // Stage 2: using Scala's language features to implement generic types
  monads.Examples.run()

  // Stage 3: comonads are duals to monads. Getting comfortable with FP
  comonads.Examples.run()

  // Stage 4: going further into FP and working with monad transformers
  transformers.Examples.run()

  // Stage 5: more FP, working with trampolining
  trampolines.Examples.run()

  // Stage 6: more FP, testing the continuation passing style
  continuations.Examples.run()

  println("")
}
