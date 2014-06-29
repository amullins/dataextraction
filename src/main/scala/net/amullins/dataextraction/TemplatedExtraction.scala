package net.amullins.dataextraction

import scala.collection.JavaConverters._

import difflib.{DiffUtils, Delta}


object TemplatedExtraction {

  def apply(template: String, requiredKeys: Seq[String], options: ExtractionOption[TemplatedExtraction]*): TemplatedExtraction =
    TemplatedExtraction(new ExtractionTemplate(template), requiredKeys).options(options : _*)


  /// OPTIONS ///
  case object Trim extends TrimOpt[TemplatedExtraction]
  case class StripTags(breaksToNewlines: Boolean) extends StripTagsOpt[TemplatedExtraction](breaksToNewlines)

}

/**
 * Extract key/value pairs from a body of text according to a supplied template.
 * @param template The template that the body of text should follow.
 * @param requiredKeys Keys that MUST be extracted for the extraction to be considered a success.
 */
case class TemplatedExtraction(template: ExtractionTemplate, requiredKeys: Seq[String] = Seq.empty) extends Extraction[TemplatedExtraction] {

  /**
   * A valid change must have a key in the original string.
   */
  private object ValidChange {
    def unapply(delta: Delta[String]): Option[(String, String, String)] = {
      if (delta.getType == Delta.TYPE.CHANGE) {
        val original = delta.getOriginal.getLines.asScala.mkString("\n")
        val revised = delta.getRevised.getLines.asScala.mkString("\n")

        template.findKeys(original).headOption.map(k1 => (k1, original, revised))
      } else {
        None
      }
    }
  }

  /**
   * Perform an extraction on a body of text.
   * @param text The body of text from which key/values should be extracted.
   * @return ExtractionResult A wrapper around the extracted values and any errors that may occurred.
   */
  def extract(text: String) = {
    val patch = DiffUtils.diff(template.text.lines.toList.asJava, text.lines.toList.asJava)
    val deltas = patch.getDeltas.asScala

    val changes =
      runOptions(
        deltas.collect {
          case ValidChange(key, original, revised) => (key, revised)
        }.toMap
      )

    if (requiredKeys.forall(k => changes.exists(_._1 == k)))
      new ExtractionResult(changes)
    else
      FailedExtraction("Missing required keys.", changes)
  }

}
