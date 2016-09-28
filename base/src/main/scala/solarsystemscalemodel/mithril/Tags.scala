package solarsystemscalemodel.mithril

import solarsystemscalemodel.mithril.MithrilBundle.TagFactory

/**
  * A Mithril implementation of scalatags (based on the scalatags DOM implemention)
  */
trait Tags extends scalatags.generic.Tags[MithrilBuilder, MithrilElement, MithrilNode] with TagFactory {

  // Root Element
  val html = typedTag[MithrilElement]("html")
  // Document Metadata
  val head = typedTag[MithrilElement]("head")
  val base = typedTag[MithrilElement]("base", void = true)
  val link = typedTag[MithrilElement]("link", void = true)
  val meta = typedTag[MithrilElement]("meta", void = true)
  // Scripting
  val script = typedTag[MithrilElement]("script")
  // Sections
  val body = typedTag[MithrilElement]("body")
  val h1 = typedTag[MithrilElement]("h1")
  val h2 = typedTag[MithrilElement]("h2")
  val h3 = typedTag[MithrilElement]("h3")
  val h4 = typedTag[MithrilElement]("h4")
  val h5 = typedTag[MithrilElement]("h5")
  val h6 = typedTag[MithrilElement]("h6")
  val header = typedTag[MithrilElement]("header")
  val footer = typedTag[MithrilElement]("footer")
  // Grouping content
  val p = typedTag[MithrilElement]("p")
  val hr = typedTag[MithrilElement]("hr", void = true)
  val pre = typedTag[MithrilElement]("pre")
  val blockquote = typedTag[MithrilElement]("blockquote")
  val ol = typedTag[MithrilElement]("ol")
  val ul = typedTag[MithrilElement]("ul")
  val li = typedTag[MithrilElement]("li")
  val dl = typedTag[MithrilElement]("dl")
  val dt = typedTag[MithrilElement]("dt")
  val dd = typedTag[MithrilElement]("dd")
  val figure = typedTag[MithrilElement]("figure")
  val figcaption = typedTag[MithrilElement]("figcaption")
  val div = typedTag[MithrilElement]("div")
  // Text-level semantics
  val a = typedTag[MithrilElement]("a")
  val em = typedTag[MithrilElement]("em")
  val strong = typedTag[MithrilElement]("strong")
  val small = typedTag[MithrilElement]("small")
  val s = typedTag[MithrilElement]("s")
  val cite = typedTag[MithrilElement]("cite")
  val code = typedTag[MithrilElement]("code")
  val sub = typedTag[MithrilElement]("sub")
  val sup = typedTag[MithrilElement]("sup")
  val i = typedTag[MithrilElement]("i")
  val b = typedTag[MithrilElement]("b")
  val u = typedTag[MithrilElement]("u")
  val span = typedTag[MithrilElement]("span")
  val br = typedTag[MithrilElement]("br", void = true)
  val wbr = typedTag[MithrilElement]("wbr", void = true)
  // Edits
  val ins = typedTag[MithrilElement]("ins")
  val del = typedTag[MithrilElement]("del")
  // Embedded content
  val img = typedTag[MithrilElement]("img", void = true)
  val iframe = typedTag[MithrilElement]("iframe")
  val embed = typedTag[MithrilElement]("embed", void = true)
  val `object` = typedTag[MithrilElement]("object")
  val param = typedTag[MithrilElement]("param", void = true)
  val video = typedTag[MithrilElement]("video")
  val audio = typedTag[MithrilElement]("audio")
  val source = typedTag[MithrilElement]("source", void = true)
  val track = typedTag[MithrilElement]("track", void = true)
  val canvas = typedTag[MithrilElement]("canvas")
  val map = typedTag[MithrilElement]("map")
  val area = typedTag[MithrilElement]("area", void = true)
  // Tabular data
  val table = typedTag[MithrilElement]("table")
  val caption = typedTag[MithrilElement]("caption")
  val colgroup = typedTag[MithrilElement]("colgroup")
  val col = typedTag[MithrilElement]("col", void = true)
  val tbody = typedTag[MithrilElement]("tbody")
  val thead = typedTag[MithrilElement]("thead")
  val tfoot = typedTag[MithrilElement]("tfoot")
  val tr = typedTag[MithrilElement]("tr")
  val td = typedTag[MithrilElement]("td")
  val th = typedTag[MithrilElement]("th")
  // Forms
  val form = typedTag[MithrilElement]("form")
  val fieldset = typedTag[MithrilElement]("fieldset")
  val legend = typedTag[MithrilElement]("legend")
  val label = typedTag[MithrilElement]("label")
  val input = typedTag[MithrilElement]("input", void = true) //.voidTag[MithrilElement]
  val button = typedTag[MithrilElement]("button")
  val select = typedTag[MithrilElement]("select")
  val datalist = typedTag[MithrilElement]("datalist")
  val optgroup = typedTag[MithrilElement]("optgroup")
  val option = typedTag[MithrilElement]("option")
  val textarea = typedTag[MithrilElement]("textarea")
}
