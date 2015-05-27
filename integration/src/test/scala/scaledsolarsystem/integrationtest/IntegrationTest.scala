package solarsystemscalemodel.integrationtest

import java.io.File

import org.openqa.selenium.chrome.{ChromeDriverService, ChromeDriver}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfter, Matchers, FlatSpec}
import org.scalatest.selenium.{WebBrowser}

/**
 * These are end-to-end integration tests which use selenium and chromedriver to launch a real browser and
 * check that modifying values in the page updates the rest of the page
 */
class IntegrationTest extends FlatSpec with Matchers with WebBrowser with BeforeAndAfter with BeforeAndAfterAll  {

  System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "/usr/lib/chromium-browser/chromedriver")
  val service = ChromeDriverService.createDefaultService()
  val indexPath = "file://" + new File("index.html").getAbsolutePath

  implicit lazy val webDriver = new ChromeDriver(service)

  override def afterAll {
    service.stop()
  }

  before {
    go to (indexPath) //read index.html at the start of every test to get to a consistent state
  }

  "The initial page" should "have the correct title" in {
    pageTitle should be("Scale Solar System") //trivial test to confirm that index.html was read
  }

  "Modifying earth diameter" should "modify neptunes diameter" in {
    val neptuneBefore = textField("Neptune-diameter").value
    textField("Earth-diameter").value = "100"
    textField("Neptune-diameter").value should not be neptuneBefore //just want to confirm the value updates
  }

  "Switching to Imperial" should "change mm to inches" in {
    radioButtonGroup("measurementsystem").value = "Imperial"
    singleSel("Neptune-diameter-uom").value should be ("Inches")
  }

  "Changing Units" should "convert the value" in {
    singleSel("Earth-diameter-uom").value = "cm"
    textField("Earth-diameter").value = "100"
    singleSel("Earth-diameter-uom").value = "M"
    textField("Earth-diameter").value should be ("1.00")
  }

  "Entering an invalid number" should "leave other values unchanged,  and keep the junk" in {
    val neptuneBefore = textField("Neptune-diameter").value
    textField("Earth-diameter").value = "textnotanumber"
    textField("Earth-diameter").value should be ("textnotanumber")
    textField("Neptune-diameter").value should be (neptuneBefore)
    val Red = "rgba(255, 0, 0, 1)"
    textField("Earth-diameter").underlying.getCssValue("background-color") should be (Red)
  }
}