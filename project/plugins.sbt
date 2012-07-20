// avro
libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.7.0" withSources()

// web
libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % (v + "-0.2.11.1"))
