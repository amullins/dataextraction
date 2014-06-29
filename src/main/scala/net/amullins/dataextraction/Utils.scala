package net.amullins.dataextraction

import scala.xml._
import scala.xml.factory.XMLLoader

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl


object Utils {

  private val saxFactory = new SAXFactoryImpl
  def tagsoup: XMLLoader[Elem] = XML.withSAXParser(saxFactory.newSAXParser())

}
