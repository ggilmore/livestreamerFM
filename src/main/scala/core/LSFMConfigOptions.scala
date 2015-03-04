package core

/**
 * Simple container class that is used to store the options that are defined in LivestreamerFM's config file.
 * Created by gmgilmore on 3/2/15.
 */


import core.LSFMConfigFileParser.VALID_OPTIONS

object LSFMConfigOptions {

  def buildLSFMConfigOption(userOptions: Map[String, String]): LSFMConfigOptions = {
    val player = userOptions.getOrElse("player", VALID_OPTIONS("player"))
    val ipAndPort = userOptions.getOrElse("ipandport", VALID_OPTIONS("ipandport"))
    val delay = userOptions.getOrElse("delay", VALID_OPTIONS("delay"))
    val livestreamerConfigLocation = userOptions.getOrElse("livestreamerconfiglocation", VALID_OPTIONS
      ("livestreamerconfiglocation"))
    LSFMConfigOptions(player, ipAndPort, delay, livestreamerConfigLocation)
  }

}

case class LSFMConfigOptions(playerLocation: String, ipAndPort: String,
                             delay: String, livestreamerConfigLocation: String) {

}

