package net.goldolphin.silvan.api

import net.goldolphin.silvan.avro.{Product, SilvanApi}

/**
 * Author: caofx
 * Date: 2012-07-18
 */

object ApiProcessor extends SilvanApi {
  def getProductByUrl(url: CharSequence, product: Product): Product = null
}
