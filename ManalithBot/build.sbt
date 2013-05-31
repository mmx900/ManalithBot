organization := "org.manalith.ircbot"

name := "ManalithBot"

version := "0.9"

libraryDependencies ++= Seq( 
		"org.pircbotx" % "pircbotx" % "1.9", 
		"org.apache.commons" % "commons-lang3" % "3.1", 
		"org.apache.httpcomponents" % "httpclient" % "4.2.5", 
		"org.json" % "json" % "20090211", 
		"org.jsoup" % "jsoup" % "1.7.2", 
		"commons-cli" % "commons-cli" % "1.2", 
		"org.hsqldb" % "hsqldb" % "2.2.9", 
		"log4j" % "log4j" % "1.2.14", 
		"org.hibernate" % "hibernate-core" % "4.2.1.Final", 
		"org.hibernate" % "hibernate-entitymanager" % "4.2.1.Final", 
		"commons-io" % "commons-io" % "2.4", 
		"commons-net" % "commons-net" % "3.2", 
		"org.springframework" % "spring-beans" % "3.2.2.RELEASE", 
		"org.springframework" % "spring-core" % "3.2.2.RELEASE", 
		"org.springframework" % "spring-context" % "3.2.2.RELEASE", 
		"org.springframework" % "spring-orm" % "3.2.2.RELEASE", 
		"org.springframework" % "spring-jdbc" % "3.2.2.RELEASE", 
		"org.springframework" % "spring-tx" % "3.2.2.RELEASE", 
		"org.springframework" % "spring-web" % "3.2.2.RELEASE", 
		"org.springframework.security" % "spring-security-core" % "3.1.4.RELEASE", 
		"org.springframework.security" % "spring-security-config" % "3.1.4.RELEASE", 
		"org.springframework.security" % "spring-security-remoting" % "3.1.4.RELEASE", 
		"org.springframework" % "spring-test" % "3.2.2.RELEASE", 
		"org.scala-lang" % "scala-library" % "2.10.1", 
		"org.codehaus.groovy" % "groovy-all" % "2.1.3", 
		"org.codehaus.gmaven.runtime" % "gmaven-runtime" % "1.5", 
		"rome" % "rome" % "1.0", 
		"javax.annotation" % "jsr250-api" % "1.0", 
		"org.apache.felix" % "org.apache.felix.main" % "4.2.1", 
		"junit" % "junit" % "4.11", 
		"cglib" % "cglib" % "2.2.2", 
		"com.fasterxml.jackson.core" % "jackson-annotations" % "2.2.0", 
		"com.fasterxml.jackson.core" % "jackson-databind" % "2.2.0", 
		"com.fasterxml.jackson.core" % "jackson-core" % "2.2.0", 
		"commons-configuration" % "commons-configuration" % "1.9", 
		"org.twitter4j" % "twitter4j-core" % "3.0.3", 
		"org.seleniumhq.selenium" % "selenium-java" % "2.32.0"
)

mainClass in (Compile, run) := Some("org.manalith.ircbot.BotMain")

fork in (Test,run) := true

javaOptions in (Test, run) += "-Dfile.encoding=UTF-8 -Dlog4j.configuration=file:./log4j.properties" 

EclipseKeys.withSource := true
