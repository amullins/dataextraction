import sbt._


object Dependencies {

  val diffutils =       "jp.skypencil.java-diff-utils"  %   "diffutils"         % "1.5.0"
  val jsr305 =          "com.google.code.findbugs"      %   "jsr305"            % "2.0.3"
  val tagsoup =         "org.ccil.cowan.tagsoup"        %   "tagsoup"           % "1.2.1"
  val slf4j =           "org.slf4j"                     %   "slf4j-api"         % "1.7.1"

	val scalatest =       "org.scalatest"                 %%  "scalatest"         % "1.9.1"   % "test"


  val global: Seq[ModuleID] = Seq(diffutils, jsr305, tagsoup, slf4j, scalatest)

}