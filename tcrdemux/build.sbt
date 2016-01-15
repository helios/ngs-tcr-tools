lazy val root = (project in file(".")).
  settings(
    name := "tcrdemux",
    version := "1.0",
    scalaVersion := "2.11.7"
  ).
 settings(
   libraryDependencies += "com.github.samtools" % "htsjdk" % "2.0.1",
   libraryDependencies += "org.rogach" %% "scallop" % "0.9.5",
   libraryDependencies += "org.yaml" % "snakeyaml" % "1.16",
   libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)
