package net.amullins.dataextraction

import scala.xml._
import scala.xml.transform._

import org.slf4j.LoggerFactory


trait ExtractionOption[E <: Extraction[E]] {
  protected val logger = LoggerFactory.getLogger(classOf[ExtractionOption[_]])

  def run(x: E, changes: Map[String, String]): (E, Map[String, String])
}


/**
 * Just as you'd expect, this option trims whitespace from extracted values.
 */
private[dataextraction] class TrimOpt[E <: Extraction[E]] extends ExtractionOption[E] {
  def run(x: E, changes: Map[String, String]) =
    (x, changes.map { case (k, v) => (k, v.trim)})
}


/**
 * Removes HTML/XML tags.
 * @param breaksToNewlines Convert <br/> to newlines "/n" for display as plaintext.
 */
private[dataextraction] class StripTagsOpt[E <: Extraction[E]](breaksToNewlines: Boolean) extends ExtractionOption[E] {
  private val transformer = new RuleTransformer(new StripTagsRewriteRule)

  def run(x: E, changes: Map[String, String]) = {
    val _changes =
      try {
        changes.map { case (k, v) =>
          (k, transformer(Utils.tagsoup.loadString(v)).text)
        }
      } catch {
        case t: Throwable =>
          logger.debug("Couldn't strip tags. Invalid HTML.")
          changes
      }

    (x, _changes)
  }


  protected class StripTagsRewriteRule extends RewriteRule {
    override def transform(n: Node): Seq[Node] =
      n match {
        case Elem(_, "br", _, _, _*) if breaksToNewlines => Text("\n")
        case e => e
      }
  }
}