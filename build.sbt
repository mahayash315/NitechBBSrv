name := """NitechBBSrv"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

// dist 時にドキュメントを作成しない設定
// ※ scala document 生成時に illegal cyclic reference 例外が出て dist できなかったため追加
sources in (Compile,doc) := Seq.empty

// dist 時にドキュメントを生成しない設定
publishArtifact in (Compile, packageDoc) := false

resolvers += "Atilika Repository" at "http://www.atilika.org/nexus/content/repositories/atilika"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.30",
  "com.google.code.gson" % "gson" % "2.2.4",
  "org.atilika.kuromoji" % "kuromoji" % "0.7.7",
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

fork in (Test) := false


// dist 時の出力ファイル名を変更
name in Universal := name.value