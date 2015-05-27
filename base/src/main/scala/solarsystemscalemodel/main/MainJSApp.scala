package solarsystemscalemodel.main

import org.scalajs.dom.Element
import solarsystemscalemodel.core.{UOM, MeasurementSystem, Distance, ModelSolarSystem}
import solarsystemscalemodel.core.ModelSolarSystem._

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSName}
import scalatags.Text.short

/**
 * Renders a ModelSolarSystem.Model to a virtual DOM using Mithril.
 *
 * All events result in a modification of the model which is then fully re-rendered.
 *
 * Mithril ensures that only the differences are applied to the real dom.
 */
object MainJSApp extends js.JSApp {

  @JSName("m")
  object Mithril extends js.Object {
    def module(element:Element, module:ModelPage):Unit = js.native
  }

  def main(): Unit = {
    val mainDiv = org.scalajs.dom.document.getElementById("main")
    val modelPage = new ModelPage
    Mithril.module(mainDiv, modelPage)
  }

  //Represents the edit made by the user. Only one cell can be modified at a a time
  //The edit needs to be held outside the ModelSolarSystem.Model because it can be an invalid edit
  //which the model never sees.
  sealed trait PageEdit
  case object NoPageEdit extends PageEdit
  case class SomePageEdit(text:String, isError:Boolean) extends PageEdit

  case class PageState(model:Model, edit:PageEdit=NoPageEdit)

  class ModelPage {
    var state:PageState = PageState(ModelSolarSystem.initialModel)
    def updateModel(updatedPageState:PageState): Unit = {
      state = updatedPageState
    }
    case class DistanceInput(celestialBody:CelestialBody, measurement:Measurement, uom:UOM, text:String, isError:Boolean) {
      def cls = {
        //the 'ok' value is needed so that the class attribute is always present
        //otherwise mithril will re-create the input element when the class appears and focus is lost
        if (isError) "error" else "ok"
      }

      def name = s"${celestialBody.name}-${measurement.name}"
    }
    case class FormattedEntry(entry:ModelEntry, diameter:DistanceInput, orbit:DistanceInput) {
      def name = entry.name
      def isSun = name == "Sun"
    }
    import solarsystemscalemodel.mithril.MithrilBundle.attrs._
    import solarsystemscalemodel.mithril.MithrilBundle.short._
    import solarsystemscalemodel.mithril.MithrilBundle.tags._
    import solarsystemscalemodel.mithril.MithrilBundle.short.SeqFrag
    @JSExport
    def view() = {
      def createDistanceInput(planet:CelestialBody, setting:Measurement, distance:Distance) = {
        val (text, isError) = if (setting == state.model.constrainedMeasurement && planet == state.model.constrainedBody) {
          state.edit match {
            case NoPageEdit => (distance.value.formatted("%.2f"), false)
            case SomePageEdit(text, isError) => (text, isError)
          }
        } else {
          (distance.value.formatted("%.2f"), false)
        }
        DistanceInput(planet, setting, distance.uom, text, isError)
      }
      val entries = state.model.entries.map { body =>
        FormattedEntry(body,
          createDistanceInput(body.celestialBody, DiameterMeasurement, body.diameter),
          createDistanceInput(body.celestialBody, OrbitMeasurement, body.orbit))
      }
      div(
        "Units: ", MeasurementSystem.values.map { system =>
          label(`for`:=system.name,
            system.name,
            input(tpe:="radio", name:="measurementsystem", id:=system.name,
              value:=system.name,
              if (system == state.model.measurementSystem) { checked := true } else "",
              onchange := { (event:js.Dynamic) => { updateModel(PageState(state.model.withSystem(system))) } }
            )
          )
        },
        table(
          thead(tr(th("Celestial Object"), th("Diameter"), th("Orbit"))),
          tbody(entries.toSeq.map { entry =>
            tr(
              key:= entry.name,
              td(entry.name),
              td(
                textInput(entry.diameter),
                unitsSelection(entry.diameter)
              ),
              if (entry.isSun) td() else td(
                textInput(entry.orbit),
                unitsSelection(entry.orbit)
              )
            )
          })
        )
      ).render
    }

    def unitsSelection(distanceInput:DistanceInput) = {
      select(
        value:= distanceInput.uom.name,
        cls:="units",
        name:=distanceInput.name+"-uom",
        onchange:=  {(e:js.Dynamic) =>
          val uom = UOM.fromText(e.target.value.asInstanceOf[String])
          updateModel(PageState(state.model.withUOM(distanceInput.celestialBody, distanceInput.measurement, uom)))
        },
        state.model.measurementSystem.units.map { unit => option(value:=unit.name,unit.name) })
    }
    
    def textInput(distanceInput:DistanceInput) = {
      val onInputFunction = { (event: js.Dynamic) => {
        val text = event.target.value.asInstanceOf[String]
        val distance = try {
          Some(BigDecimal(text))
        } catch {
          case _: NumberFormatException => None
        }
        val newModel = state.model.withDistance(distanceInput.celestialBody, distanceInput.measurement, distance)
        updateModel(PageState(newModel, SomePageEdit(text, distance.isEmpty)))
      } }

      input(tpe := "text", size := 8, cls := distanceInput.cls,
        name := distanceInput.name, value := distanceInput.text, oninput := onInputFunction)
    }
  }

}
