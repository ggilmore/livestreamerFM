package core



import core.LSFMConfigFileParser.VALID_OPTIONS

/**
 * Simple container class that is used to store the options that are defined in LivestreamerFM's config file. 
 * @param playerLocation the path to the VLC binary for this system 
 * @param ipAndPort the ip and port number that VLC needs to bind to in order to launch the HTTP server. Syntax 
 *                  is ip:Port ex: 192.168.1.1:9999                  
 * @param delay the number in milliseconds that VLC should cache audio for - useful for preventing buffering issues 
 * @param livestreamerConfigLocation the path to the folder where LivestreamerFM should store the configuration file
 *                                 that livestreamer uses
 */
case class LSFMConfigOptions(playerLocation: String, ipAndPort: String,
                             delay: String, livestreamerConfigLocation: String)

object LSFMConfigOptions {
  /**
   * Creator function that is used to create an LSFMConfigOptions instance, typically used by LSFMConfigFileParser when
   * reading configuration files.
   * @param userOptions a map with k,v parings being [OPTION], [VALUE] parings as described in the README.md file 
   *                    section on the LivestreamerFM configuration file. If an option isn't defined in this map, 
   *                    a default value (as described in the README.md) will be used. 
   * @return a LSFMConfigOptions with all of it's parameters defined as [VALUE] as described above.
   */
  def buildLSFMConfigOption(userOptions: Map[String, String]): LSFMConfigOptions = {
    val player = userOptions.getOrElse("player", VALID_OPTIONS("player"))
    val ipAndPort = userOptions.getOrElse("ipandport", VALID_OPTIONS("ipandport"))
    val delay = userOptions.getOrElse("delay", VALID_OPTIONS("delay"))
    val livestreamerConfigLocation = userOptions.getOrElse("livestreamerconfiglocation", VALID_OPTIONS
      ("livestreamerconfiglocation"))
    LSFMConfigOptions(player, ipAndPort, delay, livestreamerConfigLocation)
  }

}