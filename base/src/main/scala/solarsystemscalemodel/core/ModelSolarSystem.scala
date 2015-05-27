package solarsystemscalemodel.core

import Distance._
import UOM._

/**
 * Represents a scale model of the solar system.
 * 
 * The model is scaled based on the required diameter or orbit of a specific planet/Sun.
 * The values can be shown in metric or imperial.
 */
object ModelSolarSystem {

  case class CelestialBody(name:String, diameter:Distance, orbit:Distance) //Sun or planet

  def planets = List(
    //From http://www.bobthealien.co.uk/table.htm
    CelestialBody("Sun",    1391980 (Km),          0  (Km)),
    CelestialBody("Mercury",   4878 (Km),   57900000  (Km)),
    CelestialBody("Venus",    12104 (Km),  108160000  (Km)),
    CelestialBody("Earth",    12756 (Km),  149600000  (Km)),
    CelestialBody("Mars",      6794 (Km),  227936640  (Km)),
    CelestialBody("Jupiter", 142984 (Km),  778369000  (Km)),
    CelestialBody("Saturn",  120536 (Km), 1427034000  (Km)),
    CelestialBody("Uranus",   51118 (Km), 2870658186L (Km)),
    CelestialBody("Neptune",  49532 (Km), 4496976000L (Km))
  )

  case class ModelEntry(celestialBody:CelestialBody, diameter:Distance, orbit:Distance) {
    def name = celestialBody.name
    def valueFor(measurement: Measurement) = measurement match {
      case DiameterMeasurement => diameter
      case OrbitMeasurement => orbit
    }
  }

  /** Returns the default model. Modified versions can be created using the methods on Model */
  def initialModel = {
    val sun = planets.find(_.name == "Sun").get
    new Model(sun, DiameterMeasurement, 100 (cm), MeasurementSystem.Metric)
  }

  //Used to capture diameter vs orbit behaviour
  sealed trait Measurement { def name:String }
  case object DiameterMeasurement extends Measurement { def name = "diameter" }
  case object OrbitMeasurement extends Measurement { def name = "orbit" }

  /** Represents a scale model of the solar system where one dimension is specified and the rest are calculated. */
  case class Model(
                    constrainedBody:CelestialBody,      //The body (Sun/planet) whos dimension is being specified
                    constrainedMeasurement:Measurement,     //The measurement (diameter/orbit) being speficied
                    constrainedValue:Distance,          //The value of the constrained measurement
                    measurementSystem:MeasurementSystem //Imperial or Metric
                    ) {

    /** lists the sun and planets with their scaled values */
    val entries = {
      val realDistance = constrainedMeasurement match {
        case DiameterMeasurement => constrainedBody.diameter
        case OrbitMeasurement => constrainedBody.orbit
      }
      val scale = constrainedValue / realDistance
      def calculateModelValue(forBody:CelestialBody, forSetting:Measurement, orignalDistance:Distance) = {
        if (forBody == constrainedBody && constrainedMeasurement == forSetting) {
          constrainedValue
        } else {
          (orignalDistance * scale).inSensibleUnit(measurementSystem)
        }
      }
      planets.map { planet =>
        ModelEntry(planet,
          calculateModelValue(planet, DiameterMeasurement, planet.diameter),
          calculateModelValue(planet, OrbitMeasurement, planet.orbit))
      }
    }

    /** Looks up the model entry for a single celestial body by name (for use in tests) */
    def apply(celestialBody:String) = entries.find(_.name == celestialBody).getOrElse(
      throw new IllegalArgumentException("Unknown celestial body: " + celestialBody))

    /** Creates a new model in the specified measurement system (metric/imperial) */
    def withSystem(system:MeasurementSystem) =
      new Model(constrainedBody, constrainedMeasurement, constrainedValue.inSensibleUnit(system), system)

    /** Creates a new model where the named celestial body measurement has the specified unit (its value is updated) */
    def withUOM(celestialBody:CelestialBody, measurement: Measurement, uom:UOM) = {
      val entry = this(celestialBody.name)
      val currentValue = entry.valueFor(measurement)
      new Model(entry.celestialBody, measurement, currentValue.in(uom).roundTo(2), measurementSystem)
    }

    /** Creates a new model where the named celestial body and measurement has the specified value in the model */
    def withDistance(celestialBody:CelestialBody, measurement: Measurement, value:Option[BigDecimal]) = {
      val entry = this(celestialBody.name)
      val newDistance:Distance = value.map { _(entry.valueFor(measurement).uom) }.getOrElse(entry.valueFor(measurement))
      new Model(entry.celestialBody, measurement, newDistance, measurementSystem)
    }

  }
}
