package net.goldolphin.silvan.web

import org.apache.avro.io.{EncoderFactory, DecoderFactory}
import net.goldolphin.silvan.avro.{Product, SilvanApi}
import java.io.{ByteArrayOutputStream, StringWriter}
import org.codehaus.jackson.{JsonFactory, JsonGenerator}
import org.apache.avro.ipc.specific.SpecificResponder
import net.goldolphin.silvan.api.ApiProcessor
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}
import org.apache.avro.generic.GenericRecordBuilder

/**
 * Author: goldolphin
 * Date: 2012-07-30
 */

object SilvanServlet {
  private val responder = new SpecificResponder(classOf[SilvanApi], ApiProcessor)

  def main(args: Array[String]) {
    val writer = new SpecificDatumWriter(classOf[Product])
    val out = new ByteArrayOutputStream()
    val encoder = EncoderFactory.get().jsonEncoder(Product.SCHEMA$, out)
    val p = Product.newBuilder().setPrimaryName("名字").setSecondaryName("secondary").setSubline("sub").setSpec("spec")
    .setPrice("price").setUrl("url").build()
    println(p)

    writer.write(p, encoder)
    encoder.flush()
    val response = out.toString("utf-8")
    println(response)

    val reader = new SpecificDatumReader(classOf[Product])
    val decoder = DecoderFactory.get().jsonDecoder(Product.SCHEMA$, response)
    val p1 = reader.read(null, decoder)
    println(p1)

    val message = SilvanApi.PROTOCOL.getMessages.get("getProductByUrl")
    val r = new GenericRecordBuilder(message.re)
    println(message.getRequest)
  }
}
