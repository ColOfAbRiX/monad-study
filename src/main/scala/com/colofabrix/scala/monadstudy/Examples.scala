package com.colofabrix.scala.monadstudy

import com.colofabrix.scala.monadstudy.Multivalued.Complex

/**
  * Some practice with the topics explained in the article:
  *   http://blog.sigfpe.com/2006/08/you-could-have-invented-monads-and.html
  * These exercises are aimed at understanding how monads got developed and the
  * concepts that lie underneath them.
  */

object Examples {

  def run(): Unit = {
    println(
      """
        |  ~ ~  MONAD STUDY  ~ ~
        |
        | For each problem all the various outputs of the exercises must be the same
        | to demonstrate that the various techniques are equivalent and improve in
        | usability, cleanliness and expressive power.""".stripMargin
    )

    println( "\n ~ Debuggable ~ \n" )
    val dblNumber = new scala.util.Random().nextDouble()

    Debuggable.example1( dblNumber )
    Debuggable.example2( dblNumber )
    Debuggable.example3( dblNumber )
    Debuggable.example4( dblNumber )
    Debuggable.example5( dblNumber )
    Debuggable.example6( dblNumber )
    Debuggable.example7( dblNumber )

    println( "\n ~ Multivalued ~ \n" )
    val cmplxNumber = Complex( 4, 0 )

    Multivalued.example1( cmplxNumber )
    Multivalued.example2( cmplxNumber )
    Multivalued.example3( cmplxNumber )
    Multivalued.example4( cmplxNumber )
    Multivalued.example5( cmplxNumber )
    Multivalued.example6( cmplxNumber )

    println( "\n ~ Randomized ~ \n" )
    val intNumber = new java.util.Random().nextInt()

    Randomised.example1( intNumber )
    Randomised.example2( intNumber )
    Randomised.example3( intNumber )
    Randomised.example4( intNumber )
    Randomised.example5( intNumber )
    Randomised.example6( intNumber )
    Randomised.example7( intNumber )
    Randomised.example8( intNumber )
  }

}
