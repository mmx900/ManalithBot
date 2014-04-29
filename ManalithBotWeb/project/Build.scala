import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

	val appName = "ManalithBotWeb"
	val appVersion = "1.0-SNAPSHOT"

	val appDependencies = Seq(
		// Add your project dependencies here,
		javaCore,
		javaJdbc,
		javaJpa,
		"org.hibernate" % "hibernate-entitymanager" % "4.1.7.Final",
		"com.h2database" % "h2" % "1.4.177",
		"org.apache.commons" % "commons-lang3" % "3.2.1")

	val main = play.Project(appName, appVersion, appDependencies).settings(
		// Add your own project settings here
		ebeanEnabled := false)

}
