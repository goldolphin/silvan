package net.goldolphin.silvan.crawler

import io.Source
import org.jsoup.Jsoup

/**
 * Author: caofx
 * Date: 2012-07-26
 */

object Crawler {
  def fetch(url: String) = {
    Jsoup.connect(url).get()
  }

  def main(args: Array[String]) {
    val r = fetch("http://www.esteelauder.com.cn/products/search/ecat.tmpl?search=%E6%8A%A4%E8%82%A4")
    println(r)
  }
}
