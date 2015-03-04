package core


/**
 * Container used to create a new config file for livestreamer
 *
 *
 * @param name = name of the desired config file
 * @param vlcLocation = location of the VLC installation on this machine
 * @param fileLocation = path to where you want this configuration file to live, must end with '/' character (folder)
 * @param delay = The amount (in milliseconds) of audio that you want VLC to cache before streaming.
 *              Useful for preventing excessive buffering issues
 * @param ip = the ipAdress that you want VLC to bind to. Looks like: [0-255].[0-255].[0-255].[0-255]
 * @param vlcPort = the port that you want VLC to use, between 0 and 65536
 *
 */
case class LSConfigOptions(name: String = "livestreamerconfig.txt",
                           vlcLocation: String = OperatingSystem.getOS.getDefaultVLCLocation,
                           fileLocation: String, delay: String = "5000", ip: String,
                           vlcPort: String = "9999") {
  def validateNetworkInfo: Boolean = validateIPAddress(ip) && validPort(vlcPort)

  /**
   *
   * @param address an ip address to check
   * @return true if "address" has the correct form of an ip address (four numbers between 0 and 255 (inclusive) 
   *         separated by periods) or "address" == 'localhost' , false otherwise
   */
  private def validateIPAddress(address: String) = {
    (address == "localhost") || (address.split('.').forall { x => validQuad(x)} && address.split('.').length == 4)
  }


  private def validQuad(candidate: String) = {
    var successful = false
    try {
      successful = 0 until 256 contains candidate.toInt
    } catch {
      case t: NumberFormatException =>
    }
    successful

  }

  /**
   * *
   * @param candidate the port to test
   * @return true if candidate is a number between 0 65535 (inclusive), false otherwise 
   */
  private def validPort(candidate: String) = {
    var valid = false;
    try {
      valid = 0 to 65536 contains candidate.toInt
    } catch {
      case t: NumberFormatException =>
    }
    valid
  }
}