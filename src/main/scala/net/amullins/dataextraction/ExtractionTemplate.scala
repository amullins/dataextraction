package net.amullins.dataextraction


object ExtractionTemplate {

  /**
   * For keys in your text like {{ MY_KEY }}
   */
  val mustacheTagRegex = "\\{\\{\\s*([\\w\\.]+)\\s*\\}\\}".r

  /**
   * For keys in your text like <%= MY_KEY %>
   */
  val erbTagRegex = "<%=\\s*([\\w\\.]+)\\s*%>".r

  /**
   * For keys in your text like {% MY_KEY %}
   */
  val djangoTagRegex = "\\{%\\s*([\\w\\.]+)\\s*%\\}".r

}

/**
 * A template for use with AutoExtraction
 * @param text A string containing the keys that can be considered placeholders for the data that will be extracted.
 */
class ExtractionTemplate(val text: String) {

  private var _keySearchCompleted = false
  private var _keys = Seq[String]()

  /**
   * Retrieve the keys that *should* by extracted.
   * @return
   */
  def keys = synchronized {
    if (!_keySearchCompleted) {
      _keys = findKeys(text)
      _keySearchCompleted = true
    }
    _keys
  }

  /**
   * Extracts the keys from the text.
   *
   * Override this if you want to use a different format for your keys.
   * Uses mustache-style tags, by default.
   *
   * @return The extracted keys
   */
  def findKeys(in: String): Seq[String] =
    ExtractionTemplate.mustacheTagRegex.findAllMatchIn(in).collect {
      case m if m.groupCount == 1 => m.group(1)
    }.toSeq

}
