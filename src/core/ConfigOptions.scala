package core
/**
 * Container used to create a new config file for livestreamer
 *
 *
 * @param name = name of the desired config file
 * @param vlcLocation = location of the VLC installation on this machine
 * @param fileLocation = path to where you want this configuration file to live, must end with '/' character (folder)
 * @param delay = The amount (in milliseconds) of audio that you want VLC to cache before streaming.
 *  Useful for preventing excessive buffering issues
 * @param ip = the ipAdress that you want VLC to bind to. Looks like: [0-255].[0-255].[0-255].[0-255]
 * @param vlcPort = the port that you want VLC to use, between 0 and 65536
 *
 */
case class ConfigOptions(name: String = "livestreamerconfig",
                         vlcLocation: String = "/Applications/VLC.app/Contents/MacOS/VLC",
                         fileLocation: String, delay: String = "5000", ip: String,
                         vlcPort: String = "9999") {
  def validate: Boolean ={
    validateIPAddress(ip) && validPort(vlcPort)
  }

  private def validateIPAddress(address: String): Boolean = {
    address.split('.').forall { x => validQuad(x) }
  }

  private def validQuad(candidate: String): Boolean = {
    var successful = false
    try {
      successful = 0 until 256 contains candidate.toInt
    } catch {
      case t: NumberFormatException => // TODO: handle error
    }
    successful

  }

  private def validPort(candidate: String): Boolean = {
    var valid = false;
    try {
      valid = 0 to 65536 contains candidate.toInt
    } catch {
      case t: NumberFormatException => t.printStackTrace() // TODO: handle error
    }
    valid
  }
}