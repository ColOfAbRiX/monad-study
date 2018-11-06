package com.colofabrix.scala.fpgeneric

class Writer[+A, B]( x: A, ys: B )( implicit mb: Monoid[B] )
