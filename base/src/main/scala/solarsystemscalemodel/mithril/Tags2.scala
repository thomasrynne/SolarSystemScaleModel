package solarsystemscalemodel.mithril

//A Mithril implementation of scalatags (based on the scalatags DOM implemention)
trait Tags2 extends scalatags.generic.Tags2[MithrilBuilder,MithrilElement,MithrilNode] {
  val title = "title".tag
  val style = "style".tag
  // Scripting
  val noscript = "noscript".tag
  // Sections
  val section = "section".tag
  val nav = "nav".tag
  val article = "article".tag
  val aside = "aside".tag
  val address = "address".tag
  val main = "main".tag
  // Text level semantics
  val q = "q".tag
  val dfn = "dfn".tag
  val abbr = "abbr".tag
  val data = "data".tag
  val time = "time".tag
  val `var` = "var".tag
  val samp = "samp".tag
  val kbd = "kbd".tag
  val math = "math".tag
  val mark = "mark".tag
  val ruby = "ruby".tag
  val rt = "rt".tag
  val rp = "rp".tag
  val bdi = "bdi".tag
  val bdo = "bdo".tag
  // Forms
  val keygen = "keygen".voidTag
  val output = "output".tag
  val progress = "progress".tag
  val meter = "meter".tag
  // Interactive elements
  val details = "details".tag
  val summary = "summary".tag
  val command = "command".voidTag
  val menu = "menu".tag

}
