package core

import java.io.File

import org.scalatest.FlatSpec
import org.scalatest.matchers.Matchers

/**
 * Created by gmgilmore on 3/1/15.
 */
class LiveStreamerConfigFileWriterTest extends FlatSpec with Matchers{

  "LiveStreamerConfigFileWriter" should "create a new configuration file if one is not present at the given path" in{
    val options = new LSConfigOptions(fileLocation = "src/test/resources", ip = "localhost")
    val configFile = new File("src/test/resources/livestreamerconfig.txt")
    assert (configFile.exists == false)
  }
}
