package net.goldolphin.silvan.crawler

import java.net.{URL, URLDecoder, URLEncoder}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import annotation.tailrec
import org.json.JSONObject
import org.apache.commons.lang.StringEscapeUtils

/**
 * Author: caofx
 * Date: 2012-07-27
 */

class EsteelauderCrawler {
  private val searchBase = "http://www.google.com.hk/search?hl=zh-CN&q=%%q+site%3Awww.esteelauder.com.cn%2Fproduct"
  private val searchResultPattern = """.*?\?q=(.+?)\&.*?""".r

  def search(q: String): String = {
    val searchUrl = searchBase.replace("%%q", URLEncoder.encode(q, "utf-8"))
    val doc = fetch(searchUrl)
    val searchResultPattern(pageUrl) = doc.select("div#ires > ol h3.r > a").get(0).attr("abs:href")
    URLDecoder.decode(pageUrl, "utf-8")
  }

  def list(url: String): List[Product] = {
    val doc = fetch(url)
    val html = doc.html()
    val mark = "var page_data = "
    val res = new JSONObject(html.substring(html.indexOf(mark) + mark.length))
      .getJSONObject("catalog").getJSONObject("mpp").getJSONObject("rpcdata")
      .getJSONArray("products").getJSONObject(0)

    val pName = res.getString("PROD_RGN_NAME")
    val sName = res.getString("PROD_RGN_SUBHEADING")
    val subline = res.optString("SUB_LINE", "")

    val skus = res.getJSONArray("skus").getJSONObject(0)
    val spec = skus.getString("PRODUCT_SIZE")
    val price = URLDecoder.decode(skus.getString("formattedPrice"), "utf-8")
    List(new Product(pName, sName, subline, spec, price, null))
  }

  def fetchItem(url: String): Product = {
    val doc = fetch(url)
    val html = doc.html()
    val mark = "var page_data = "
    val res = new JSONObject(html.substring(html.indexOf(mark) + mark.length))
      .getJSONObject("catalog").getJSONObject("spp").getJSONObject("rpcdata")
      .getJSONArray("products").getJSONObject(0)

    val pName = res.getString("PROD_RGN_NAME")
    val sName = res.getString("PROD_RGN_SUBHEADING")
    val subline = res.optString("SUB_LINE", "")

    val skus = res.getJSONArray("skus").getJSONObject(0)
    val spec = skus.getString("PRODUCT_SIZE")
    val price = StringEscapeUtils.unescapeHtml(skus.getString("formattedPrice"))
    new Product(pName, sName, subline, spec, price, new URL(new URL(url), res.getString("url")).toString)
  }

  def fetch(url: String): Document = Jsoup.connect(url).userAgent("silvan").get()

  def findPaired(left: Char, right: Char, input: String, fromIndex: Int): Option[String] = {
    val start = input.indexOf(left, fromIndex)
    if (start < 0) {
      None
    } else {
      @tailrec
      def find(i: Int, deep: Int): Int = {
        if (deep == 0) {
          i
        } else if (i == input.length) {
          -1
        } else {
          input(i) match {
            case `left` => find(i + 1, deep + 1)
            case `right` => find(i + 1, deep - 1)
            case _ => find(i + 1, deep)
          }
        }
      }
      find(start + 1, 1) match {
        case -1 => None
        case end => Option(input.substring(start, end))
      }
    }
  }
}

object EsteelauderCrawler extends EsteelauderCrawler {

  def main(args: Array[String]) {
    val url = search("盈润保湿眼霜")
    val pCN = fetchItem(url)
    val pUS = fetchItem(url.replace("www.esteelauder.com.cn/", "www.esteelauder.com/"))
    println(pCN)
    println(pUS)
  }
}

/**
 *
 * @param primaryName
 * @param secondaryName
 * @param spec
 * @param price
 */
case class Product(val primaryName: String, val secondaryName: String, val subline: String, val spec: String, val price: String, val url: String)