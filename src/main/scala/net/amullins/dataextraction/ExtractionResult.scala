package net.amullins.dataextraction


class ExtractionResult(val map: Map[String, String]) {
  val isOk = true

  def keys: Set[String] = map.keySet
  def values: Set[String] = map.valuesIterator.toSet
}

case class FailedExtraction(
  message: String = "Extraction Failed!",
  partialExtraction: Map[String, String] = Map.empty
)
  extends ExtractionResult(partialExtraction)
{
  override val isOk = false
}