name := "Patmos"

scalaVersion := "2.11.12"

scalacOptions ++= Seq("-Xmax-classfile-name", "130", "-unchecked", "-deprecation", "-feature", "-language:reflectiveCalls")

libraryDependencies += scalaVersion("org.scala-lang" % "scala-compiler" % _).value

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

// here switch between Chisel 2 and 3

//libraryDependencies += "edu.berkeley.cs" %% "chisel" % "2.2.38"

libraryDependencies += "edu.berkeley.cs" %% "chisel3" % "3.1.7"
libraryDependencies += "edu.berkeley.cs" %% "chisel-iotesters" % "1.3.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5"
