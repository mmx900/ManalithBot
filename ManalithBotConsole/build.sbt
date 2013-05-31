organization := "org.manalith.ircbot"

name := "ManalithBotConsole"

version := "0.0.1-SNAPSHOT"

libraryDependencies ++= Seq( 
		"log4j" % "log4j" % "1.2.14", 
		"org.apache.commons" % "commons-lang3" % "3.1", 
		"commons-cli" % "commons-cli" % "1.2", 
		"org.springframework" % "spring-beans" % "3.1.1.RELEASE", 
		"org.springframework" % "spring-core" % "3.1.1.RELEASE", 
		"org.springframework" % "spring-context" % "3.1.1.RELEASE", 
		"jline" % "jline" % "2.7", 
		"org.springframework.security" % "spring-security-config" % "3.1.0.RELEASE", 
		"org.springframework.security" % "spring-security-remoting" % "3.1.0.RELEASE", 
		"javax.annotation" % "jsr250-api" % "1.0"
)

mainClass in (Compile, run) := Some("org.manalith.ircbot.console.Launcher")

EclipseKeys.withSource := true
