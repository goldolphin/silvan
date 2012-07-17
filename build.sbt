name := "silvan"

version := "0.1"

scalaVersion := "2.9.1"

retrieveManaged := true

/// Library Settings
// avro
libraryDependencies += "org.apache.avro" % "avro" % "1.7.0" withSources()

libraryDependencies += "org.apache.avro" % "avro-ipc" % "1.7.0" withSources()

// lucene
libraryDependencies += "org.apache.lucene" % "lucene-core" % "3.6.0" withSources()

