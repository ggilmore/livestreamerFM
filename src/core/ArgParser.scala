package core

import java.io.IOException
import java.net.{MalformedURLException, URL}

import scala.io.StdIn
import scala.sys.process._

object ArgParser extends App {
  /*
   * Desired Arguments: [STREAM_URL] [AUDIO_OPTION] [IP_ADDRESS] [PORT] [LIVESTREAMER CONFIG FILE LOCATION]
   */

  private val DEFAULT_USAGE = "USAGE: [STREAM_URL] [AUDIO_OPTION] [IP_ADDRESS] [PORT] [LIVESTREAMER_CONFIG_FILE_LOCATION]"
  private val IP_OR_PORT_ERROR = "That doesn't look like a real IP-Address and/or port. Check it."
  private val LIVESTREAMER_LOCATION_OSX = "/usr/local/bin/livestreamer"

  if (args.size != 5) println(DEFAULT_USAGE)
  else {
    val url = args(0)
    val audioOption = args(1)
    val ipAddress = args(2)
    val port = args(3)
    val configLocation = args(4)
    val option = new LSConfigOptions(fileLocation = configLocation, ip = ipAddress, vlcPort = port)
    if (!option.validate) println(IP_OR_PORT_ERROR)
    else {
      println("\n\n****** " + url + " AUDIO STREAM LOCATED @ http://" + ipAddress + ":" + port + " ******\n\n")
      LivestreamerConfigFileWriter.writeNewConfigFile(option)
      createLiveStreamerProcess(configLocation = option.fileLocation + option.name, url = url, audioOptionName = audioOption)
    }

  }

  private val urlToAudioOptionMap: Map[String, String] = Map("twitch.tv/" -> "audio", "youtube.com/" -> "audio_mp4").withDefaultValue("")

  private def getAudioOption(url: String): String = {
    try {
      val parsedUrl = new URL(url)
      println(parsedUrl.getHost)
      urlToAudioOptionMap(parsedUrl.getHost)
    } catch {
      case t: MalformedURLException => ""
    }
  }

  /**
   * Starts the Livestreamer process with the specified options passed to it
   * @param livestreamerPath path to the location of the Livestreamer binary
   * @param configLocation path of the configuration file that is passed to Livestreamer
   * @param url the url of the stream to listen to (e.g. twitch.tv/arteezy)
   * @param audioOptionName the specific audio option for the stream that is
   *                        passed to Livestreamer (e.g. "audio" for a twitch url or "audio_mp4" for youtube urls)
   */
  private def createLiveStreamerProcess(livestreamerPath: String = "livestreamer",
                                        configLocation: String, url: String, audioOptionName: String) {


    try {
      val lsprocess = Process(livestreamerPath + " --config " + configLocation + " " + url + " " + audioOptionName)
      val runningProcess = lsprocess.run()
      //makes sure to send SIGINT to the livestreamer process so that VLC shuts down cleanly
      sys addShutdownHook ({
        println("Hook caught!")
        runningProcess.destroy()
      })

    }
    catch {
      case t: IOException => println("Livestreamer installation not found. Are you sure it's installed and in the system path?")
    }

  }
}