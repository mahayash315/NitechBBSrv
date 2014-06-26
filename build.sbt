name := "NitechBBSrv"

version := "1.0-SNAPSHOT"

Keys.fork in Test := false

parallelExecution in Test := false

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.30",
  "com.google.code.gson" % "gson" % "2.2.4",
  "org.atilika.kuromoji" % "kuromoji" % "0.7.7",
  javaJdbc,
  javaEbean,
  cache
)     

play.Project.playJavaSettings

resolvers += "Atilika Repository" at "http://www.atilika.org/nexus/content/repositories/atilika"