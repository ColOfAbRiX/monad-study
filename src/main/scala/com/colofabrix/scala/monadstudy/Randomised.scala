package com.colofabrix.scala.monadstudy

object Randomised {

  /**
    * EXAMPLE #1
    * Normal functions composition: calling one function and passing its result
    * to another one.
    */

  /**
    * EXAMPLE #2
    * Adding some FP-style debugging: I can't have any side effect so I must
    * return the log as output parameter of the function. The caller will
    * manage this return value.
    */

  /**
    * EXAMPLE #3
    * Adding a plumbing function: I don't want the caller to manage the binding
    * of the functions, it should only use them, so I create a new plumbing
    * function that does exactly that, it binds together function calls.
    */

  /**
    * EXAMPLE #4
    * Renaming and cleaning: use better names, use currying and explicit the
    * output data structures so that it becomes more practical to use the
    * binding function.
    */

  /**
    * EXAMPLE #5
    * Explicit Writer monad: create a class that incorporates the generic
    * functionalities developed in the previous example and that makes the
    * usage of these functionalities even more practical.
    */

  /**
    * EXAMPLE #6
    * Scala idiomatic way of monads: Scala allows to use a special syntax with
    * a for-expression to work with monads in a nicer way. And usability is
    * improved even more.
    */

  /**
    * EXAMPLE #7
    * Introduce generics: use generic types for the Writer monad so that it can
    * be used in different contexts and with more than just logs
    */

}
