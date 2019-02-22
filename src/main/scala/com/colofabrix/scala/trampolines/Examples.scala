package com.colofabrix.scala.trampolines

object Examples {

  import com.colofabrix.scala.generics._

  def run(): Unit = {
    println(
      """
        |  ~ ~  TRAMPOLINING  ~ ~
        """.stripMargin
    )

    val longList = (1 to 100000).toList

    //
    // Test the StackOverflowError exception with long recursion that can't
    // be optimized with tail recursion
    //
    println( "\n ~ Test the StackOverflowError exception ~ \n" )

    def isLengthEven1[A]( list: List[A] ): Boolean = {
      def even( eList: List[A] ): Boolean = eList match {
        case Nil => true
        case _ :: xs => odd( xs )
      }

      def odd( dList: List[A] ): Boolean = dList match {
        case Nil => false
        case _ :: xs => even( xs )
      }

      even(list)
    }

    try {
      val result1 = isLengthEven1( longList )
      println( result1 )
    }
    catch {
      case _: java.lang.StackOverflowError =>
        println("Exception! Encountered StackOverflowError")
    }

    //
    // Introducing Trampolining
    //
    println( "\n ~ Introducing Trampolining ~ \n" )

    def isLengthEven2[A]( list: List[A] ): Trampoline[Boolean] = {
      def even( eList: List[A] ): Trampoline[Boolean] = eList match {
        case Nil => Done(true)
        case _ :: xs => More(() => odd( xs ))
      }

      def odd( dList: List[A] ): Trampoline[Boolean] = dList match {
        case Nil => Done(false)
        case _ :: xs => More(() => even( xs ))
      }

      even(list)
    }

    val computation = More(() => isLengthEven2( longList ))
    val result2 = Trampoline.run(computation)
    println( s"The length of the list is even: $result2" )

    println("")
  }

}
