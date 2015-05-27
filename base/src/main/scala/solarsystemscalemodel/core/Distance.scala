package solarsystemscalemodel.core

import java.math.MathContext

import scala.math.BigDecimal.RoundingMode

/**
 * Represents a distance unit of measurement and its millimeter value for use in conversions.
 */
case class UOM(name:String, unitValueInMillimeters:BigDecimal)
object UOM {
  val mm = UOM("mm", 1)
  val cm = UOM("cm", 10)
  val M = UOM("M", 1000)
  val Km = UOM("KM", 1000000)

  val Inches = UOM("Inches",     25.4)
  val Feet =   UOM("Feet",      304.8)
  val Yards =  UOM("Yards",     914.4)
  val Miles =  UOM("Miles", 1609344.0)

  val units = Seq(mm, cm, M, Km, Inches, Feet, Yards, Miles)
  def fromText(text:String) = units.find(_.name == text).getOrElse(
    throw new IllegalArgumentException("Unknown unit: " + text))
}

/**
 * Represents a meaning full collection of units which are commonly used together
 */
object MeasurementSystem {
  import UOM._
  val Metric = new MeasurementSystem("Metric", mm, cm, M, Km)
  val Imperial = new MeasurementSystem("Imperial", Inches, Feet, Yards, Miles)
  val values = Seq(Metric, Imperial)
}
case class MeasurementSystem(name:String, units:UOM*) {
  val unitsInAscendingOrder = units.sortBy(_.unitValueInMillimeters)
}

/**
 * Represents a distance with a known unit 
 */
case class Distance(value:BigDecimal, uom:UOM) {

  require(value >= BigDecimal(0.0)) //negative values are not tested or needed so check that they are not used

  /** Converts the Distance to the specified units (converting the value) */
  def in(toUOM:UOM):Distance = Distance(value * (uom.unitValueInMillimeters / toUOM.unitValueInMillimeters), toUOM)
  
  /** Divides by a scalar (a unit-less value) */
  def / (scale:BigDecimal):Distance = {
    require(scale > BigDecimal(0.0), "scale can not be negative: " + scale)
    copy(value = (value / scale))
  }

  /** Multiplies by a scalar (a unit-less value) */
  def * (scale:BigDecimal):Distance = copy(value = (value * scale))

  /** Divides by another Distance to get a scalar (a unit-less value) */
  def / (rhs:Distance):BigDecimal= {
    require(rhs.value > BigDecimal(0.0), "rhs can not be negative: " + rhs)
    this.in(rhs.uom).value / rhs.value
  }

  /** Rounds the value to the specified number of decimal places */
  def roundTo(decimalPlaces:Int) = {
    if (decimalPlaces < 0) throw new IllegalArgumentException("decimal places must be >= 0: " + decimalPlaces)
    this.copy(value = value.setScale(decimalPlaces, RoundingMode.HALF_UP))
  }

  /**
   * Converts to the largest unit available in the measurement system where the value is greater than 1
   */
  def inSensibleUnit(system:MeasurementSystem):Distance = {
    val millimeters = in(UOM.mm).value
    val unitsWhereValueGreaterThanOne = system.unitsInAscendingOrder.filter(
      unit => millimeters > unit.unitValueInMillimeters)
    in(unitsWhereValueGreaterThanOne.lastOption.getOrElse(system.units.head))
  }
}

object Distance {
  //These implicit classes mean literal Distances can be entered as "100 (cm)" in code
  implicit class RichLong(value:Long) {
    def apply(uom:UOM) = Distance(value, uom)
  }
  implicit class RichInt(value:Int) {
    def apply(uom:UOM) = Distance(value, uom)
  }
  implicit class RichDouble(value:Double) {
    def apply(uom:UOM) = Distance(value, uom)
  }
  implicit class RichBigDecimal(value:BigDecimal) {
    def apply(uom:UOM) = Distance(value, uom)
  }
}
