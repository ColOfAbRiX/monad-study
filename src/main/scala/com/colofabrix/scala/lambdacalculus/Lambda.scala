package com.colofabrix.scala.lambdacalculus

/**
  * Value-level implementation of the lambda calculus as exposed on https://glebec.github.io/lambda-talk/
  * NOTE: Not tested properly
  */
object Lambda {

  // Because there must be a λ symbol in a lambda calculus code
  type λ = Any
  type λ0 = λ
  type λ1 = λ => λ
  type λ2 = λ => λ => λ
  type λ3 = λ => λ => λ => λ
  type λ4 = λ => λ => λ => λ => λ

  // Scala enforces type safety but doing so prevents to be generic in the type
  // of input and outputs. A force casting is used to shift the type check at
  // runtime.
  implicit class Shortcuts(value: λ) {
    def as[A]: A = value.asInstanceOf[A]
    def fn0: λ0 = as[λ0]
    def fn1: λ1 = as[λ1]
    def fn2: λ2 = as[λ2]
    def fn3: λ3 = as[λ3]
    def fn4: λ4 = as[λ4]
  }

  //  COMBINATORS  //

  // I - Idiot - Identity - λx.x
  def I(x: λ): λ = x
  def identity = I _
  def idiot = I _

  // M - Mockingbird - Self application - λf.ff
  def M(f: λ): λ = f.fn1 andThen f.fn1
  def ω = M _
  def mockingbird = M _

  // K - Kestrel - Constant, first - λab.a
  def K(a: λ)(b: λ): λ = a
  def konst = K _
  def kestrel = K _

  // KI - Kite - Second - λab.b
  def KI(a: λ)(b: λ): λ = K(I _) _
  def kite = KI _

  // C - Cardinal - Flip arguments - λfab.fba
  def C(f: λ)(a: λ)(b: λ): λ = f.fn2(b)(a)
  def flip = C _
  def cardinal = C _

  // B - Bluebird - 1<-1 Composition - λfgx.f(gx)
  def B(f: λ)(g: λ)(x: λ): λ = f.fn1(g.fn1(x))
  def compose = B _
  def bluebird = B _

  // T - Thrush - Hold an argument - λab.ba
  def TH(a: λ)(b: λ): λ = b.fn1(a)
  def thrush = B _

  // V - Vireo - Hold a pair - λabf.fab
  def V(a: λ)(b: λ)(f: λ): λ = f.fn2(a)(b)
  def vireo = V _
  def pair = V _

  // B' - Blackbird - 1<-2 Composition - λfgab.f(gab)
  def B1: λ = B(B _)(B _) _
  def blackbird = B1 _

  //  CHURCH ENCODING - BOOLEAN ALGEBRA //

  // True - λxy.x
  def T(x: λ)(y: λ): λ = x

  // False - λxy.y
  def F(x: λ)(y: λ): λ = y

  // Not - λb.bFT
  def NOT(b: λ): λ = C(b)(F _)(T _)

  // And - λpq.pqF
  def AND(p: λ)(q: λ): λ = p.fn2(q)(F _)

  // Or - λpq.pTq
  def OR(p: λ)(q: λ): λ = p.fn2(T _)(q)

  // Boolean equality - λpq.p (qTF) (qFT)
  def BEQ(p: λ)(q: λ): λ = p.fn2( q.fn2(T _)(F _) )( q.fn2(F _)(T _) )

  //  CHURCH ENCODING - NUMERALS //

  // Zero - λfx.x
  def ZERO(f: λ)(x: λ): λ = x

  // Successor - λnfx.f(nfx)
  def SUCC(n: λ)(f: λ)(x: λ): λ = f.fn1(n.fn2(f)(x))

  // Addition - λab.a(succ)b
  def ADD(a: λ)(b: λ): λ = a.fn2(SUCC _)(b)

  // Multiplication - λab.a∘b = compose
  def MULT(a: λ)(b: λ): λ = B _

  // Exponentiation - λab.ba
  def POW(a: λ)(b: λ): λ = TH(a)(b)

  // Is Zero - λn.n(λ_.F)T
  def ISZERO(n: λ): λ = n.fn2(F _)(T _)
  def IS0 = ISZERO _

  //  CHURCH ENCODING - ARITHMETIC //

  val n0 = ZERO _
  val n1 = SUCC(n0) _
  val n2 = SUCC(SUCC(n0) _) _
  val n3 = SUCC(SUCC(SUCC(n0) _) _) _
  val n4 = SUCC(n3) _
  val n5 = ADD(n2)(n3)
  val n6 = ADD(n3)(n3)
  val n8 = MULT(n4)(n2)
  val n9 = SUCC(n8) _

}

/**
  * This is a copy-paste from http://mez.cl/en/lambda-dsl/
  */
object Mezcla {

  import Term._

  sealed trait Term {
    def $(that: Term) = Application(this, that)
  }
  object Term {
    implicit def symToVar(s: Symbol): Variable = Variable(s)
  }
  case class Variable(name: Symbol)                   extends Term
  case class Abstraction(param: Variable, body: Term) extends Term
  case class Application(fn: Term, arg: Term)         extends Term

  def λ(p: Variable, ps: Variable*)(body: Term) = Abstraction(p, ps.foldRight(body) { (v, b) ⇒ Abstraction(v, b) })

  val I     = λ('x) { 'x }
  val T     = λ('x, 'y) { 'x }
  val F     = λ('x, 'y) { 'y }
  val `¬`   = λ('p) { 'p $ F $ T }
  val ω     = λ('f) { 'f $ 'f }
  val Ω     = ω $ ω
  val S     = λ('x, 'y, 'z) { 'x $ 'z $ ('y $ 'z) }
  val `1+2` = λ('m, 'n) {
    'm $ ( λ('i, 's, 'z) { 'i $ 's $ ('s $ 'z) }) $ 'n
  } $ λ('s, 'z) { 's $ 'z } $ λ('s, 'z) { 's $ ('s $ 'z) }
  val (s, m, i, n, z) = ('s, 'm, 'i, 'n, 'z)
  val one   = λ(s, z) { s $ z }
  val two   = λ(s, z) { s $ (s $ z) }
  val scc   = λ(i, s, z) { i $ s $ (s $ z) }
  val add   = λ(m, n) { m $ scc $ n }
  val onePlusTwo = add $ one $ two

}

