package com.colofabrix.scala.comonads

/**
  * Store Comonad (with simplified internal structure for simplicity)
  *
  * This models a "store" of values of type A indexed by the type S. We have the
  * ability to directly access the A value under a given S using peek, and there
  * is a distinguished cursor or current position. The comonadic extract just
  * reads the value under the cursor, and duplicate gives us a whole store full
  * of stores such that if we peek at any one of them, we get a Store whose
  * cursor is set to the given s. We’re defining a seek(s) operation that moves
  * the cursor to a given position s by taking advantage of duplicate.
  */
case class Store[S, A]( peek: S => A, cursor: S ) {
  def seek( s: S ) =
    duplicate.peek( s )

  def extract: A =
    peek( cursor )

  def duplicate: Store[S, Store[S, A]] =
    coflatMap(x => x)

  def map[B]( f: A => B ): Store[S, B] =
    coflatMap( s => f(extract) )

  def coflatMap[B]( f: Store[S, A] => B ): Store[S, B] =
    Store( s => f(Store(peek, s)), cursor )
}
