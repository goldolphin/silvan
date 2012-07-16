import java.io.File
import org.apache.avro.compiler.specific.SpecificCompiler
import sbt._
import Process._
import sbt.Keys._

/**
 * Author: goldolphin
 * Date: 2012-07-17
 */

object SilvanBuild extends Build {
  val avroConfig = config("avro")

  val generate = TaskKey[Seq[File]]("generate", "Generate the Java sources for the Avro files.")

  lazy val avroSettings: Seq[Setting[_]] = inConfig(avroConfig)(Seq[Setting[_]](
    sourceDirectory <<= (sourceDirectory in Compile) {
      _ / "avro"
    },
    javaSource <<= (sourceManaged in Compile) {
      _ / "compiled_avro"
    },
    version := "1.6.1",

    managedClasspath <<= (classpathTypes, update) map {
      (ct, report) =>
        Classpaths.managedJars(avroConfig, ct, report)
    },

    generate <<= sourceGeneratorTask
  )) ++ Seq[Setting[_]](
    sourceGenerators in Compile <+= (generate in avroConfig),
    managedSourceDirectories in Compile <+= (javaSource in avroConfig),
  )

  private def compile(srcDir: File, target: File, log: Logger) = {
    for (schema <- (srcDir ** "*.avsc").get) {
      log.info("Compiling Avro schema %s".format(schema))
      SpecificCompiler.compileSchema(schema.asFile, target)
    }

    for (protocol <- (srcDir ** "*.avpr").get) {
      log.info("Compiling Avro protocol %s".format(protocol))
      SpecificCompiler.compileProtocol(protocol.asFile, target)
    }

    (target ** "*.java").get.toSet
  }

  private def sourceGeneratorTask = (streams, sourceDirectory in avroConfig, javaSource in avroConfig, cacheDirectory) map {
    (out, srcDir, targetDir, cache) =>
    //TODO: Use FileFunction.cached to cache the compiled files. See: https://github.com/harrah/xsbt/wiki/FAQ
      compile(srcDir, targetDir, out.log).toSeq
  }
}
