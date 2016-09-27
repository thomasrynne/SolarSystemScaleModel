package solarsystemscalemodel.mithril


import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.{Dictionary, Function}
import scalatags.generic._
import scalatags.stylesheet.{Cls, StyleSheetFrag}
import scalatags.{Companion, DataConverters, Escaping}

/**
  * A Mithril implementation of scalatags (based on the scalatags DOM implemention)
  *
  * A mutable datastructure which represents an html element
  */
class MithrilBuilder(uri:String, tag:String) {

  private val attributes = Dictionary.empty[js.Any]
  private val children = new js.Array[js.Any]()

  def addElement(child: MithrilElement) {
    children.push(child)
  }

  def setAttribute(name: String, value: js.Any): Unit = {
    attributes(name) = value
  }
  def setKey(key: String): Unit = {
    attributes("key") = key
  }
  def appendText(text: String): Unit = {
    children.append(text)
  }

  def build:MithrilElement = {
    js.Dynamic.global.m(tag, attributes, children).asInstanceOf[MithrilElement]
  }

  def setStyleProperty(name: String, value: String) { ???}
  def addRaw(text: String): Unit = { ??? }
}

trait MtFrag extends scalatags.generic.Frag[MithrilBuilder,MithrilElement]{
  def render: MithrilElement
  def applyTo(b: MithrilBuilder) = b.addElement(render)
}

sealed trait MithrilNode extends js.Any
trait MithrilElement extends MithrilNode
trait MithrilText extends MithrilNode

object MithrilElement {
  def text(text:String) = {
    //This will be invoked if a string is rendered without any enclosing element.
    text.asInstanceOf[MithrilElement]
  }
  def fragment(children:Seq[MithrilNode]) = {
    //This will be invoked if a sequence is rendered without any enclosing element.
    //I am not sure what the best behaviour is in this case.
    js.Dynamic.global.m("div", null, null).asInstanceOf[MithrilElement]
  }
}

object MithrilBundle extends Bundle[MithrilBuilder, MithrilElement, MithrilNode]
  with Aliases[MithrilBuilder, MithrilElement, MithrilNode]{

  trait MithrilAttributes extends Util {
    //A mithril specific attribute used to ensure the correct elements are altered when elements are removed or added
    val key = attr("key")
    //A mithril specific attribute which provide a hook into element creation on the dom
    val config = attr("config")
  }

  trait Aggregate extends scalatags.generic.Aggregate[MithrilBuilder, MithrilElement, MithrilNode]{
    type StringFrag = MithrilBundle.StringFrag
    type RawFrag = MithrilBundle.RawFrag
    type HtmlTag = MithrilBundle.TypedTag[MithrilElement]
    type SvgTag = MithrilBundle.TypedTag[MithrilElement]
    type Tag = MithrilBundle.TypedTag[MithrilElement]
    val RawFrag = MithrilBundle.RawFrag
    val StringFrag = MithrilBundle.StringFrag
    val HtmlTag = MithrilBundle.TypedTag
    val SvgTag = MithrilBundle.TypedTag
    val Tag = MithrilBundle.TypedTag

    def StyleFrag(s: StylePair[MithrilBuilder, _]): StyleSheetFrag = ???

    def ClsModifier(s: Cls): Modifier = ???

    def genericAttr[T] = new MithrilBundle.GenericAttr[T]

    def genericStyle[T] = new MithrilBundle.GenericStyle[T]

    def genericPixelStyle[T](implicit ev: StyleValue[T]): PixelStyleValue[T] = new MithrilBundle.GenericPixelStyle[T](ev)

    def genericPixelStylePx[T](implicit ev: StyleValue[String]): PixelStyleValue[T] = new MithrilBundle.GenericPixelStylePx[T](ev)

    implicit def stringFrag(v: String): StringFrag = new MithrilBundle.StringFrag(v)

    def raw(s: String) = RawFrag(s)
  }

  trait TagFactory extends scalatags.generic.Util[MithrilBuilder, MithrilElement, MithrilNode]{
    def tag(s: String, void: Boolean = false): ConcreteHtmlTag[MithrilElement] = {
      typedTag[MithrilElement](s, void)
    }
    def typedTag[T <: MithrilElement](s: String, void: Boolean = false)
                                  (implicit ns: Namespace): ConcreteHtmlTag[T] = {
      if (!Escaping.validTag(s))
        throw new IllegalArgumentException(
          s"Illegal tag name: $s is not a valid XML tag name"
        )
      makeAbstractTypedTag[T](s, void, ns)
    }
  }


  trait Cap extends Util with TagFactory { self =>
    type ConcreteHtmlTag[T <: MithrilElement] = TypedTag[T]

    protected[this] implicit def stringAttrX = new GenericAttr[String]
    protected[this] implicit def stringStyleX = new GenericStyle[String]
    protected[this] implicit def stringPixelStyleX = new GenericPixelStyle[String](stringStyleX)
    implicit def UnitFrag(u: Unit): StringFrag = new MithrilBundle.StringFrag("")
    def makeAbstractTypedTag[T <: MithrilElement](tag: String, void: Boolean, namespaceConfig: Namespace): TypedTag[T] = {
      TypedTag(tag, Nil, void, namespaceConfig)
    }

    implicit class SeqFrag[A](xs: Seq[A])(implicit ev: A => Frag) extends Frag {
      def applyTo(t: MithrilBuilder): Unit = xs.foreach(_.applyTo(t))
      def render: MithrilElement = {
        MithrilElement.fragment(xs.map(_.render))
      }
    }
  }

  case class StringFrag(v: String) extends MtFrag {
    def render: MithrilElement = MithrilElement.text(v)
    override def applyTo(builder: MithrilBuilder) { builder.appendText(v) }
  }

  case class RawFrag(v: String) extends Modifier{
    def applyTo(builder: MithrilBuilder): Unit = {
      builder.addRaw(v)
    }
  }

  class GenericAttr[T] extends AttrValue[T] {
    def apply(builder: MithrilBuilder, attribute: Attr, value: T): Unit = {
      builder.setAttribute(attribute.name, value.asInstanceOf[js.Any])
    }
  }

  class GenericStyle[T] extends StyleValue[T] {
    def apply(builder: MithrilBuilder, s: Style, v: T): Unit = {
      builder.setStyleProperty(s.cssName, v.toString)
    }
  }

  class GenericPixelStyle[T](ev: StyleValue[T]) extends PixelStyleValue[T]{
    def apply(s: Style, v: T) = StylePair(s, v, ev)
  }

  class GenericPixelStylePx[T](ev: StyleValue[String]) extends PixelStyleValue[T]{
    def apply(s: Style, v: T) = StylePair(s, v + "px", ev)
  }

  case class TypedTag[+Output <: MithrilElement](tag: String = "",
                                                 modifiers: List[Seq[Modifier]],
                                                 void: Boolean = false,
                                                 namespace: Namespace)
    extends scalatags.generic.TypedTag[MithrilBuilder, Output, MithrilNode]
      with MtFrag {
    protected[this] type Self = TypedTag[Output]

    /**
      * Trivial override, not strictly necessary, but it makes IntelliJ happy...
      */
    def apply(xs: Modifier*): TypedTag[Output] = {
      this.copy(tag = tag, void = void, modifiers = xs :: modifiers)
    }

    override def toString = render.toString

    def render: Output = {
      val elem = new MithrilBuilder(namespace.uri, tag)
      build(elem)
      elem.build.asInstanceOf[Output]
    }
  }
  object attrs extends MithrilBundle.Cap with Attrs
  object tags extends MithrilBundle.Cap with solarsystemscalemodel.mithril.Tags
  object tags2 extends MithrilBundle.Cap with solarsystemscalemodel.mithril.Tags2
  object styles extends MithrilBundle.Cap with Styles
  object styles2 extends MithrilBundle.Cap with Styles2
  object svgTags extends MithrilBundle.Cap with solarsystemscalemodel.mithril.SvgTags
  object svgAttrs extends MithrilBundle.Cap with SvgAttrs

  object implicits extends Aggregate with DataConverters

  object all
    extends Cap
      with Attrs
      with Styles
      with solarsystemscalemodel.mithril.Tags
      with DataConverters
      with Aggregate
      with MithrilAttributes
      with LowPriorityImplicits

  object short
    extends Cap
      with DataConverters
      with solarsystemscalemodel.mithril.Tags
      with AbstractShort
      with Aggregate
      with MithrilAttributes
      with LowPriorityImplicits{

    object * extends Cap with Attrs with Styles
  }

  object StringFrag extends Companion[StringFrag]
  object RawFrag extends Companion[RawFrag]
}
trait LowPriorityImplicits {
  implicit def bindJsAnyLike[T](implicit ev$1: T => Function) = new scalatags.generic.AttrValue[MithrilBuilder, T]{
    def apply(builder: MithrilBuilder, a: scalatags.generic.Attr, f: T): Unit = {
      builder.setAttribute(a.name, f)
    }
  }
}