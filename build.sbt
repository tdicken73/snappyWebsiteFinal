name := "SnappyWebsiteFinal"

scalaVersion := "2.10.2"

resolvers += "FuseSource Community Snapshot Repository" at "http://repo.fusesource.com/nexus/content/groups/public-snapshots"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.2.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.2.3",
  "org.fusesource.lmdbjni" % "lmdbjni-all" % "99-master-SNAPSHOT")
  
  