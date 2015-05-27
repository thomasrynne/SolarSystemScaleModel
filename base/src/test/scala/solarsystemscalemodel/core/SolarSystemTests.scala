package solarsystemscalemodel.core

import solarsystemscalemodel.core.UOM._
import solarsystemscalemodel.core.Distance._
import solarsystemscalemodel.core.ModelSolarSystem._
import solarsystemscalemodel.core.MeasurementSystem._

import utest._
import utest.ExecutionContext.RunNow

import scala.math.BigDecimal.RoundingMode

object SolarSystemTests extends TestSuite {

  def assertEqualDistance(actual:Distance, expected:Distance): Unit = {
    assert(actual.uom == expected.uom)
    //different routes to the same number can lead to very small differences in the value so the values are rounded
    assert(actual.value.setScale(8, RoundingMode.HALF_UP) == expected.value.setScale(8, RoundingMode.HALF_UP))
  }

  val tests = TestSuite {
    "InitalModel"-{
      "should have 100cm sun"- {
        val initialModel = ModelSolarSystem.initialModel
        assert(initialModel("Sun").diameter == 100(cm))
      }
    }

    val Sun = ModelSolarSystem.planets.find(_.name == "Sun").get
    val Earth = ModelSolarSystem.planets.find(_.name == "Earth").get
    val Neptune = ModelSolarSystem.planets.find(_.name == "Neptune").get
    val Jupiter = ModelSolarSystem.planets.find(_.name == "Jupiter").get

    "withDistance:Orbit"-{
      val tenNeptuneModel = ModelSolarSystem.initialModel.withDistance(Neptune, OrbitMeasurement, Some(10))
      "should change the specified orbit"-{
        assertEqualDistance(tenNeptuneModel("Neptune").orbit, 10 (Km))
      }
      "should scale the other dimensions"-{
        val scale = 10 (Km) / Neptune.orbit
        assertEqualDistance(tenNeptuneModel("Jupiter").diameter, (Jupiter.diameter * scale).inSensibleUnit(Metric))
        assertEqualDistance(tenNeptuneModel("Earth").orbit, (Earth.orbit * scale).inSensibleUnit(Metric))
      }
    }

    "withDistance:Diameter"-{
      val oneHundredMMEarth = ModelSolarSystem.initialModel.withDistance(Earth, DiameterMeasurement, Some(100))
      "should change the specified diameter"-{
        assertEqualDistance(oneHundredMMEarth("Earth").diameter, 100 (mm))
      }
      "should scale the other dimensions"-{
        val scale = 100 (mm) / Earth.diameter
        assertEqualDistance(oneHundredMMEarth("Jupiter").diameter, (Jupiter.diameter * scale).inSensibleUnit(Metric))
        assertEqualDistance(oneHundredMMEarth("Earth").orbit, (Earth.orbit * scale).inSensibleUnit(Metric))
      }
    }

    "withMeasurementSystem"-{
      val imperialModel = ModelSolarSystem.initialModel.withSystem(MeasurementSystem.Imperial)
      "should change the model dimensions"-{
        assertEqualDistance(imperialModel("Sun").diameter, ModelSolarSystem.initialModel("Sun").diameter.in(Yards))
        assertEqualDistance(imperialModel("Jupiter").orbit, ModelSolarSystem.initialModel("Jupiter").orbit.in(Yards))
      }
    }

    "withUOM"-{
      val mmEarthModel = ModelSolarSystem.initialModel.withUOM(Earth, DiameterMeasurement, mm)
      "should convert the value to the new unit"-{
        assert(mmEarthModel("Earth").diameter.uom == mm)
        val earthDiameterInOriginalModel = ModelSolarSystem.initialModel("Earth").diameter.in(mm).roundTo(2)
        assertEqualDistance(mmEarthModel("Earth").diameter, earthDiameterInOriginalModel)
      }
    }
  }
}
