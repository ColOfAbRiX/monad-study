package com.colofabrix.scala.monadstudy

object Multivalued {

  case class Complex( x: Double, y: Double ) {
    val r: Double = Math.hypot( x, y )
    val t: Double = Math.atan2( x, y )
    override def toString: String = s"C(%.3f, %.3f)".format(x, y)
  }

  private def deMoivreRoots( c: Complex, roots: Int ): Seq[Complex] = Seq.tabulate( roots ) { n =>
    Complex(
      Math.pow( c.r, 1 / roots.toDouble ) * Math.cos( (c.t + 2.0 * n * Math.PI) / roots.toDouble ),
      Math.pow( c.r, 1 / roots.toDouble ) * Math.sin( (c.t + 2.0 * n * Math.PI) / roots.toDouble )
    )
  }

  /**
    * EXAMPLE #1
    * Wiring functions together: I can't have any side effect so I must
    * return the log as output parameter of the function. The caller will
    * manage this return value.
    */

  def sqrt1( c: Complex ): Seq[Complex] = deMoivreRoots( c, 2 )

  def cbrt1( c: Complex ): Seq[Complex] = deMoivreRoots( c, 3 )

  def example1( x: Complex): Unit = {
    val y1 = sqrt1( x )
    val result = y1.flatMap( x => cbrt1( x ) )

    println( "EXAMPLE #1 - Wiring functions together" )
    println( s"Input:  $x" )
    println( s"Sixth roots:\n  ${result.mkString("\n  ")}" )
    println( "" )
  }

  /**
    * EXAMPLE #2
    * Adding a plumbing function: I don't want the caller to manage the binding
    * of the functions, it should only use them, so I create a new plumbing
    * function that does exactly that, it binds together function calls.
    */
  def sqrt2( c: Complex ): Seq[Complex] = deMoivreRoots( c, 2 )

  def cbrt2( c: Complex ): Seq[Complex] = deMoivreRoots( c, 3 )

  def bind2( f: Complex => Seq[Complex] )( c: Seq[Complex] ): Seq[Complex] = c.flatMap(f)

  def unit2( c: Complex ): Seq[Complex] = Seq(c)

  def example2( x: Complex ): Unit = {
    val result = bind2( cbrt2 )(
      bind2( sqrt2 )( unit2( x ) )
    )

    println( "EXAMPLE #2 - Adding a plumbing function" )
    println( s"Input:  $x" )
    println( s"Sixth roots:\n  ${result.mkString("\n  ")}" )
    println( "" )
  }

  /**
    * EXAMPLE #3
    * Renaming and cleaning: use better names, use currying and explicit the
    * output data structures so that it becomes more practical to use the
    * binding function.
    */
  type Multivalued3[A] = Seq[A]

  def sqrt3( c: Complex ): Seq[Complex] = deMoivreRoots( c, 2 )

  def cbrt3( c: Complex ): Seq[Complex] = deMoivreRoots( c, 3 )

  def bind3( f: Complex => Multivalued3[Complex] )( c: Multivalued3[Complex] ): Multivalued3[Complex] = c.flatMap(f)

  def unit3( c: Complex ): Multivalued3[Complex] = Seq(c)

  def example3( x: Complex ): Unit = {
    val initial = unit3( x )
    val inner = bind3( sqrt3 )( _ )
    val outer = bind3( cbrt3 )( _ )
    val result = outer( inner( initial ) )

    println( "EXAMPLE #3 - Renaming and cleaning" )
    println( s"Input:  $x" )
    println( s"Sixth roots:\n  ${result.mkString("\n  ")}" )
    println( "" )
  }

  /**
    * EXAMPLE #4
    * Explicit Multivalued monad: create a class that incorporates the generic
    * functionalities developed in the previous example and that makes the usage
    * of these functionalities even more practical.
    */
  case class Multivalued4 private ( xs: Seq[Complex] ) {
    def bind( f: Complex => Multivalued4 ): Multivalued4 = Multivalued4( xs.flatMap( f( _ ).xs ) )
  }
  object Multivalued4 {
    def apply( c: Complex ): Multivalued4 = new Multivalued4( Seq(c) )
  }

  def sqrt4( c: Complex ): Multivalued4 = Multivalued4( deMoivreRoots( c, 2 ) )

  def cbrt4( c: Complex ): Multivalued4 = Multivalued4( deMoivreRoots( c, 3 ) )

  def example4( x: Complex ): Unit = {
    val result = Multivalued4( x )
      .bind( sqrt4 )
      .bind( cbrt4 )

    println( "EXAMPLE #4 - Explicit Multivalued monad" )
    println( s"Input:  $x" )
    println( s"Sixth roots:\n  ${result.xs.mkString("\n  ")}" )
    println( "" )
  }

  /**
    * EXAMPLE #5
    * Scala idiomatic way of monads: Scala allows to use a special syntax with
    * a for-expression to work with monads in a nicer way. And usability is
    * improved even more.
    */
  case class Multivalued5 private ( xs: Seq[Complex] ) {
    def flatMap( f: Complex => Multivalued5 ): Multivalued5 = Multivalued5( xs.flatMap( f( _ ).xs ) )
    def map( f: Complex => Complex ): Multivalued5 = Multivalued5( xs.map(f) )
  }
  object Multivalued5 {
    def apply( c: Complex ): Multivalued5 = new Multivalued5( Seq(c) )
  }

  def sqrt5( c: Complex ): Multivalued5 = Multivalued5( deMoivreRoots( c, 2 ) )

  def cbrt5( c: Complex ): Multivalued5 = Multivalued5( deMoivreRoots( c, 3 ) )

  def example5( x: Complex ): Unit = {
    val result = for {
      y1 <- Multivalued5( x )
      y2 <- sqrt5( y1 )
      y3 <- cbrt5( y2 )
    } yield y3

    println( "EXAMPLE #5 - Scala idiomatic way of monads" )
    println( s"Input:  $x" )
    println( s"Sixth roots:\n  ${result.xs.mkString("\n  ")}" )
    println( "" )
  }

  /**
    * EXAMPLE #6
    * Introduce generics: use generic types for the List (MyList) monad so that
    * it can be used in different contexts and with more than just logs
    */
  case class List[+A] private ( xs: Seq[A] ) {
    def flatMap[B]( f: A => List[B] ): List[B] = List( xs.flatMap( f( _ ).xs ) )
    def map[B]( f: A => B ): List[B] = List( xs.map(f) )
  }
  object List {
    def apply[A]( c: A ): List[A] = new List( Seq(c) )
  }

  def sqrt6( c: Complex ): List[Complex] = List( deMoivreRoots( c, 2 ) )

  def cbrt6( c: Complex ): List[Complex] = List( deMoivreRoots( c, 3 ) )

  def example6( x: Complex ): Unit = {
    val result = for {
      y1 <- List( x )
      y2 <- sqrt6( y1 )
      y3 <- cbrt6( y2 )
    } yield y3

    println( "EXAMPLE #6 - Introduce generics" )
    println( s"Input:  $x" )
    println( s"Sixth roots:\n  ${result.xs.mkString("\n  ")}" )
    println( "" )
  }

}
