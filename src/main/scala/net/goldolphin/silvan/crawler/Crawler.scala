package net.goldolphin.silvan.crawler

import io.Source
import org.jsoup.Jsoup

/**
 * Author: caofx
 * Date: 2012-07-26
 */

class Crawler {
  def fetch(url: String) = {
    val s = Jsoup.connect(url).get()
  }
}
