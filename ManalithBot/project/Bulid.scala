import sbt._
import Keys._

object ManalithBotBulid extends Build {
	System.setProperty("log4j.configuration", "file:./log4j.xml")
	System.setProperty("file.encoding", "UTF-8")

	lazy val root = Project(id = "ManalithBot",
		base = file("."))
}