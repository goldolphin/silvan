package net.goldolphin.silvan.crawler

import java.net.{URLDecoder, URLEncoder}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Author: caofx
 * Date: 2012-07-27
 */

class EsteelauderCrawler {
  private val searchBase = "http://www.google.com.hk/search?hl=zh-CN&q=%%q+site%3Awww.esteelauder.com.cn%2Fproduct"
  private val searchResultPattern = """.*?\?q=(.+?)\&.*?""".r

  def search(q: String) = {
    val searchUrl = searchBase.replace("%%q", URLEncoder.encode(q, "utf-8"))
    val doc = fetch(searchUrl)
    val searchResultPattern(pageUrl) = doc.select("div#ires > ol h3.r > a").get(0).attr("abs:href")
    URLDecoder.decode(pageUrl, "utf-8")
  }

  def fetchItem(url: String): Product = {
    val doc = fetch(url)
    println(doc)
    val pName = doc.select("h2.prod_title_primary").get(0).text()
    val sName = doc.select("h3.prod_title_secondary").text()
    val (spec, price) = {
      val a = doc.select("span#price-span").get(0).text().trim.split("\\s+")
      (a(0).trim, a(1).trim)
    }
    new Product(pName, sName, spec, price)
  }

  def fetch(url: String): Document = Jsoup.connect(url).userAgent("silvan").get()
}

object EsteelauderCrawler extends EsteelauderCrawler {
  def main(args: Array[String]) {
    val url = "http://www.esteelauder.com.cn/product/685/2880/productcatalog/skincare/bycategory/eyecare/Hydra-Complete/Multi-Level-Moisture-Eye-Gel-Creme/index.tmpl"
    val p = fetchItem(url)
    println(p)
  }
}

/**
 *
 * @param primaryName
 * @param secondaryName
 * @param spec
 * @param price
 */
case class Product(val primaryName: String, val secondaryName: String, val spec: String, val price: String)