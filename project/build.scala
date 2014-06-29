import sbt._
import sbt.Keys._

import sbtrelease.ReleasePlugin._


object dataextraction extends Build {
	import Resolvers._


  lazy val main = {
    val projectName = "dataextraction"
    val projectSettings =
      baseSettings :+
        (name := projectName) :+
        (libraryDependencies ++= Dependencies.global)

    Project(id = projectName, base = file("."), settings = projectSettings)
  }


  val baseSettings = {
 		Defaults.defaultSettings ++
    releaseSettings ++
 		Seq[Def.Setting[_]](
 			resolvers ++= Seq(maven2, mavenLocal),
 			scalacOptions ++= Seq(
 				"-language:implicitConversions",
 				"-language:reflectiveCalls",
 				"-language:existentials",
 				"-language:higherKinds",
 				"-deprecation",
 				"-feature",
 				"-unchecked"
 			),
 			unmanagedBase := file("lib"),
 			organization := "net.amullins",
 			scalaVersion := "2.10.3"
 		)
 	}

}


object Resolvers {
	val maven2 =              "Java.net Maven2 Repo"  at "http://download.java.net/maven/2/"
	val mavenLocal =          "Local Maven"           at Path.userHome.asFile.toURI.toURL + ".m2/repository"
}
