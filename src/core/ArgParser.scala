package core
import scala.sys.process._
import scala.io.StdIn
object ArgParser extends App {
  /*
   * Desired Arguments: [STREAM_URL] [IP_ADDRESS] [PORT] [LIVESTREAMER CONFIG FILE LOCATION]
   */

  private val USAGE = "USAGE: [STREAM_URL] [IP_ADDRESS] [PORT] [LIVESTREAMER_CONFIG_FILE_LOCATION]"
  private val IP_OR_PORT_ERROR = "That doesn't look like a real IP-Address or port. Check it."
  private val LIVESTREAMER_LOCATION_OSX = "/usr/local/bin/livestreamer"

  if (args.size != 4) println(USAGE)
  else {
    val url = args(0)
    val ipAddress = args(1)
    val port = args(2)
    val configLocation = args(3)
    val option = new ConfigOptions(fileLocation = configLocation, ip = ipAddress, vlcPort = port)
    if (!option.validate) println(IP_OR_PORT_ERROR) else {
      ConfigFileWriter.writeNewConfigFile(option)
      createLiveStreamerProcess(configLocation = option.fileLocation + option.name)
    }

  }

  private def createLiveStreamerProcess(livestreamerPath: String = LIVESTREAMER_LOCATION_OSX, configLocation: String) {
    val lsprocess = Process(livestreamerPath + " --config " + configLocation)
    lsprocess.run()
    val x = StdIn.readLine()

  }
}