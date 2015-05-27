package solarsystemscalemodel.core

import utest._

import solarsystemscalemodel.core.UOM._
import solarsystemscalemodel.core.Distance._

import utest.ExecutionContext.RunNow

//Using utest because scalatest does not work in the scalajs runtime
object DistanceTests extends TestSuite {

  //Just used to make some of the tests more readable
  implicit class TestableDistance(distance:Distance) {
    def isConvertedTo(other:Distance): Unit = {
      assert(other == distance.in(other.uom))
    }
    def hasSensibleMetricValue(sensible:Distance): Unit = {
      assert(sensible == distance.inSensibleUnit(MeasurementSystem.Metric))
    }
    def hasSensibleImperialValue(sensible:Distance): Unit = {
      assert(sensible == distance.inSensibleUnit(MeasurementSystem.Imperial))
    }
  }

  def bd(value:Double) = BigDecimal(value)

  val tests = TestSuite {
    'inUOM {
       1 (mm) isConvertedTo    1 (mm)
       1 (cm) isConvertedTo   10 (mm)
      10 (mm) isConvertedTo    1 (cm)
       1 (Km) isConvertedTo 1000 (M)
       1 (Miles) isConvertedTo   1.609344 (Km)
       1 (Inches) isConvertedTo  2.54 (cm)
      12 (Inches) isConvertedTo  1.0 (Feet)
    }
    'inSensibleUnitMetric {
      0.0011 (Km) hasSensibleMetricValue 1.1 (M)
        1100 (M)  hasSensibleMetricValue 1.1 (Km)
         1.3 (cm) hasSensibleMetricValue 1.3 (cm)
         9.0 (mm) hasSensibleMetricValue 9.0 (mm)

         1.0 (Feet)  hasSensibleMetricValue  30.48 (cm)
          10 (Miles) hasSensibleMetricValue (10 (Miles) in (Km))
    }
    'inSensibleUnitImperial {
       3   (Inches) hasSensibleImperialValue  3 (Inches)
      24   (Inches) hasSensibleImperialValue  2 (Feet)
       0.5 (Feet)   hasSensibleImperialValue  6 (Inches)

       5   (cm)     hasSensibleImperialValue (5 (cm) in (Inches))
       2   (Km)     hasSensibleImperialValue (2 (Km) in (Miles))
    }
    'round {
      assert((1      (cm) roundTo(2)) == 1    (cm))
      assert((1.12   (cm) roundTo(2)) == 1.12 (cm))
      assert((1.1234 (cm) roundTo(2)) == 1.12 (cm))
      assert((1.1261 (cm) roundTo(2)) == 1.13 (cm))
      assert((1.1261 (cm) roundTo(0)) == 1.00 (cm))
      assert((1.1261 (cm) roundTo(10)) == 1.1261 (cm))
      intercept[IllegalArgumentException]{ 1.1261 (cm) roundTo(-1) }
    }
    "negative not allowed"-{
      intercept[IllegalArgumentException]{ Distance(bd(-1.00), M) }
      intercept[IllegalArgumentException]{ Distance(bd(-0.01), M) }
    }
    "*"-{
      assert( (10 (M) * 5) == 50 (M) )
      assert( (10 (cm) * 5.2) == 52 (cm) )
      assert( (10 (cm) * 0) == 0 (cm) )
    }
    "/ scalar"-{
      assert( (10 (M) / 5) == (10/5) (M))
      assert( (10 (cm) / 5.2) == Distance(bd(10.0)/bd(5.2), cm) )
      intercept[IllegalArgumentException]{ 10 (cm) / 0.0 }
    }
    "/ Distance"-{
      assert( (10 (M) / 2 (M))  == bd(5.0) ) //simple case
      assert( ( 1 (M) / 1 (cm)) == bd(100) ) //different units
      intercept[IllegalArgumentException]{ 10 (cm) / 0.0 (cm) } //divide by zero
    }
  }

}
