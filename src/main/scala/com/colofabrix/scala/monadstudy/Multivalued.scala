package com.colofabrix.scala.monadstudy

object Multivalued {

  case class Complex( r: Double, t: Double )

  private def deMoivreRoots( c: Complex, roots: Int ): Seq[Complex] = Seq.tabulate( roots ) { n =>
    Complex(
      Math.sqrt( c.r ) * Math.cos( (c.t + n * 2 * Math.PI) / roots ),
      Math.sqrt( c.r ) * Math.sin( (c.t + n * 2 * Math.PI) / roots )
    )
  }

  def sqrt( c: Complex ): Seq[Complex] = deMoivreRoots( c, 2 )

  def cbrt( c: Complex ): Seq[Complex] = deMoivreRoots( c, 3 )

  /**
    * EXAMPLE #1
    * Wiring functions together: I can't have any side effect so I must
    * return the log as output parameter of the function. The caller will
    * manage this return value.
    */
  def example1( x: Complex): Unit = {
    val y1 = sqrt( x )
    val result = y1.flatMap( x => cbrt( x ) )

    println( "EXAMPLE #1 - Wiring functions together" )
    println( s"Input:  $x" )
    println( s"Sixth roots: $result" )
    println( "" )
  }

  /**
    * EXAMPLE #2
    * Adding a plumbing function: I don't want the caller to manage the binding
    * of the functions, it should only use them, so I create a new plumbing
    * function that does exactly that, it binds together function calls.
    */
  def bind2( f: Complex => Seq[Complex] )( c: Seq[Complex] ): Seq[Complex] = c.flatMap(f)

  def unit2( c: Complex ): Seq[Complex] = Seq(c)

  def example2( x: Complex ): Unit = {
    val result = bind2( cbrt )(
      bind2( sqrt )( unit2( x ) )
    )

    println( "EXAMPLE #2 - Adding a plumbing function" )
    println( s"Input:  $x" )
    println( s"Sixth roots: $result" )
    println( "" )
  }

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
