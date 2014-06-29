package net.amullins.dataextraction

import scala.collection.mutable


trait Extraction[E <: Extraction[E]] { self: E =>

  def extract(text: String): ExtractionResult


  protected val _options = mutable.Buffer[ExtractionOption[E]]()
  def options: Seq[ExtractionOption[E]] = _options.toSeq
  def options(options: ExtractionOption[E]*): E = {
    _options ++= options
    this
  }

  def hasOption(option: ExtractionOption[E]): Boolean = _options.contains(option)

  protected def runOptions(changes: Map[String, String]) = {
    var _changes = changes
    _options.foreach { option =>
      _changes = option.run(this, _changes)._2
    }
    _changes
  }

}
