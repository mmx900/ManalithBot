import sbt._
import Keys._

object Bulid extends Build {
	System.setProperty("log4j.configuration", "file:./log4j.properties")
	System.setProperty("file.encoding", "UTF-8")

	lazy val root = Project(id = "ManalithBot",
		base = file("."))
}