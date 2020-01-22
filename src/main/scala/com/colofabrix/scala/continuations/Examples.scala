package com.colofabrix.scala.continuations

object Examples {

  import com.colofabrix.scala.continuations.Continuations._

  def run(): Unit = {
    println(
      """
        |  ~ ~  CONTINUATIONS  ~ ~
        """.stripMargin
    )


    println( "\n ~ Simple continuation ~ \n" )

    def printer(input: String): Unit = println(input)
    uppercase1("my string", printer)


    println( "\n ~ Error handling continuation ~ \n" )

    def printerError[A](result: Either[Throwable, A]): Unit = {
      result match {
        case Left(error)  => println(error.toString())
        case Right(value) => println(s"Square root is: ${value.toString()}")
      }
    }
    sqrt2(4.0, printerError)
    sqrt2(-3.0, printerError)


    println( "\n ~ Using a wrapper/container to make the call pure ~ \n" )

    val pureResult1 = sqrt3(4.0)
    val pureResult2 = sqrt3(-3.0)

    pureResult1.unsafeRunAsync(printerError)
    pureResult2.unsafeRunAsync(printerError)


    println( "\n ~ The wrapper/container is a monad ~ \n" )
    sqrt3(4.0).map(x => x - 5.0)

    println("")
  }

}
