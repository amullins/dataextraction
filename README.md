# Data Extraction
------------------
This library aims to be a general information extraction library using a variety of methods.

## Firstly, support for extracting via template:
**Supply a template with some keys:**
```
Hi, {{firstname}},

This is a new contact from a form on your website.

Name: {{fullname}}  
Email: {{emailaddress}}  
Phone: {{phonenumber}}  
Comments: {{customercomments}}
```

*Supports different keys syntax: Mustache, ERB or Django*

## Example Usage
```scala
// for the extraction to be considered a success, these keys must be extracted
val optionalRequiredKeys = Seq('fullname', 'emailaddress')

val template =
  new ExtractionTemplate("a sample email body with keys - see above") {
    // if you want to use a syntax other than mustache (the defualt) for keys, override the findKeys method
    override def findKeys(in: String): Seq[String] =
      ExtractionTemplate.erbTagRegex.findAllMatchIn(in).collect {
        case m if m.groupCount == 1 => m.group(1)
      }.toSeq
  }

val extractFromThis =
  "staying with the above sample, this would be a new email body"

val extraction: ExtractionResult = {
  import TemplatedExtraction.{Trim, StripTags} // options
  
  // if the template is HTML, you may want to use StripTags
  TemplatedExtraction(template, optionalRequiredKeys, Trim)
    .extract(extractFromThis)
}
```