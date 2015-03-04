package core_test

import core.{OperatingSystem, LSFMConfigOptions}
import core.LSFMConfigFileParser.VALID_OPTIONS
import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by gmgilmore on 3/4/15.
 */
class LSFMConfigOptionsTest extends FlatSpec with Matchers {

  "LSFMConfigOptions.buildLSFMConfigOption" should "use all the options passed by the user when the specified all of " +
    "them manually" in {
    val options = LSFMConfigOptions.buildLSFMConfigOption(Map("player" -> "pathtoVLC",
      "ipandport" -> "233.233.233.233:6666", "delay" -> "7777", "livestreamerconfiglocation" -> "/bogus"))

    assert(options == LSFMConfigOptions(playerLocation = "pathtoVLC", ipAndPort = "233.233.233.233:6666",
      delay = "7777", livestreamerConfigLocation = "/bogus"))
  }

  it should "use all the options passed by the user when they specifiy every one besides player, and use the default " +
    "options for player" in {
    val options = LSFMConfigOptions.buildLSFMConfigOption(Map("ipandport" -> "233.233.233.233:6666", 
      "delay" -> "7777", "livestreamerconfiglocation" -> "/bogus"))

    assert(options == LSFMConfigOptions(playerLocation = VALID_OPTIONS("player"), 
      ipAndPort = "233.233.233.233:6666", delay = "7777", livestreamerConfigLocation = "/bogus"))
  }

  it should "use all the options passed by the user when they specifiy every one besides ipAndPort, and use the default " +
      "options for ipAndPort" in {
      val options = LSFMConfigOptions.buildLSFMConfigOption(Map("player" -> "pathtoVLC", 
        "delay" -> "7777", "livestreamerconfiglocation" -> "/bogus"))

      assert(options == LSFMConfigOptions(playerLocation = "pathtoVLC",
        ipAndPort = VALID_OPTIONS("ipandport"), delay = "7777", livestreamerConfigLocation = "/bogus"))
    }

  it should "use all the options passed by the user when they specifiy every one besides delay, and use the default " +
      "options for delay" in {
    val options = LSFMConfigOptions.buildLSFMConfigOption(Map("player" -> "pathtoVLC",
      "ipandport" -> "233.233.233.233:6666", "livestreamerconfiglocation" -> "/bogus"))

    assert(options == LSFMConfigOptions(playerLocation = "pathtoVLC", ipAndPort = "233.233.233.233:6666",
      delay = VALID_OPTIONS("delay"), livestreamerConfigLocation = "/bogus"))
    }

  it should "use all the options passed by the user when they specifiy every one besides livestreamerConfigLocation, and use the default " +
        "options for livestreamerConfigLocation" in {
    val options = LSFMConfigOptions.buildLSFMConfigOption(Map("player" -> "pathtoVLC",
      "ipandport" -> "233.233.233.233:6666", "delay" -> "7777"))

    assert(options == LSFMConfigOptions(playerLocation = "pathtoVLC", ipAndPort = "233.233.233.233:6666",
      delay = "7777", livestreamerConfigLocation = VALID_OPTIONS("livestreamerconfiglocation")))
  }


  it should "use all the default options when the user passes in no options "in {
    val options = LSFMConfigOptions.buildLSFMConfigOption(Map())

    assert(options == LSFMConfigOptions(playerLocation = VALID_OPTIONS("player"), ipAndPort = VALID_OPTIONS("ipandport"),
      delay = VALID_OPTIONS("delay"), livestreamerConfigLocation = VALID_OPTIONS("livestreamerconfiglocation")))
  }
  


}
