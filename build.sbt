name := "silvan"

version := "0.1"

scalaVersion := "2.9.1"

retrieveManaged := true

seq(webSettings :_*)

/// Library Settings
// avro
libraryDependencies += "org.apache.avro" % "avro" % "1.7.0"

libraryDependencies += "org.apache.avro" % "avro-ipc" % "1.7.0"

// lucene
libraryDependencies += "org.apache.lucene" % "lucene-core" % "3.6.0"

// lift
libraryDependencies += "net.liftweb" %% "lift-webkit" % "2.4"

libraryDependencies += "net.liftweb" %% "lift-record" % "2.4"

// jetty
libraryDependencies += "org.mortbay.jetty" % "jetty" % "6.1.22" % "container"

// slf4j
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.6"

