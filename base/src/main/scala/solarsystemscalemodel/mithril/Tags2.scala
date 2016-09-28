package solarsystemscalemodel.mithril

import solarsystemscalemodel.mithril.MithrilBundle.TagFactory

/**
  * A Mithril implementation of scalatags (based on the scalatags DOM implemention)
  */
trait Tags2 extends scalatags.generic.Tags2[MithrilBuilder, MithrilElement, MithrilNode] with TagFactory {
  val title = typedTag[MithrilElement]("title") 
  val style = typedTag[MithrilElement]("style") 
  // Scripting
  val noscript = typedTag[MithrilElement]("noscript") 
  // Sections
  val section = typedTag[MithrilElement]("section") 
  val nav = typedTag[MithrilElement]("nav") 
  val article = typedTag[MithrilElement]("article") 
  val aside = typedTag[MithrilElement]("aside") 
  val address = typedTag[MithrilElement]("address") 
  val main = typedTag[MithrilElement]("main") 
  // Text level semantics
  val q = typedTag[MithrilElement]("q") 
  val dfn = typedTag[MithrilElement]("dfn") 
  val abbr = typedTag[MithrilElement]("abbr") 
  val data = typedTag[MithrilElement]("data") 
  val time = typedTag[MithrilElement]("time") 
  val `var` = typedTag[MithrilElement]("var") 
  val samp = typedTag[MithrilElement]("samp") 
  val kbd = typedTag[MithrilElement]("kbd") 
  val math = typedTag[MithrilElement]("math") 
  val mark = typedTag[MithrilElement]("mark") 
  val ruby = typedTag[MithrilElement]("ruby") 
  val rt = typedTag[MithrilElement]("rt") 
  val rp = typedTag[MithrilElement]("rp") 
  val bdi = typedTag[MithrilElement]("bdi") 
  val bdo = typedTag[MithrilElement]("bdo") 
  // Forms
  val keygen = typedTag[MithrilElement]("keygen", void = true) 
  val output = typedTag[MithrilElement]("output") 
  val progress = typedTag[MithrilElement]("progress") 
  val meter = typedTag[MithrilElement]("meter") 
  // Interactive elements
  val details = typedTag[MithrilElement]("details") 
  val summary = typedTag[MithrilElement]("summary") 
  val command = typedTag[MithrilElement]("command", void = true) 
  val menu = typedTag[MithrilElement]("menu") 

}
