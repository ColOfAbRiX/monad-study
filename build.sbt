name := "MonadStudy"

version := "0.1"
scalaVersion := "2.12.10"
mainClass in (Compile, run) := Some("com.colofabrix.scala.MainMonadStudy")

resolvers += Resolver.sonatypeRepo("releases")
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8")
