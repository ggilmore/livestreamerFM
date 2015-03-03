package core

import java.io.IOException

import scala.sys.process._

object ArgParser extends App {
  /*
   * Desired Arguments: [STREAM_URL] [IP_ADDRESS:PORT] [LIVESTREAMER CONFIG FILE LOCATION]
   */

  private val DEFAULT_USAGE = "USAGE: [STREAM_URL] [IP_ADDRESS:PORT] [LIVESTREAMER_CONFIG_FILE_LOCATION] " +
    "\nOR: --config [LIVESTREAMERFM_CONFIG_FILE_LOCATION] [STREAM_URL]"
  private val IP_OR_PORT_ERROR = "That doesn't look like a real IP-Address and/or port. Check it."
  private val LIVESTREAMER_LOCATION_OSX = "/usr/local/bin/livestreamer"

  if (!(args.size > 0)) println(DEFAULT_USAGE)
  else args(0) match {
    case "--config" if (args.size == 3) => yesConfigFile(args.tail)
    case _ if (args.size == 3) => noConfigFile(args)
    case _ => println(DEFAULT_USAGE)
  }

  /**
   * run by arg parser when the first option is --config, reads the config file given in the arguments and then 
   * * starts the livestreamer process
   *
   *
   * @param args should be a length 2 string array with the first element being the path to the LivestreamerFM config
   *             file, and the second element being the url that you want to play 
   */
  private def yesConfigFile(args: Array[String]) = {
    val configPath = args(0)
    val url = args(1)
    val myOptions = LSFMConfigFileParser.parseConfigFile(configPath) 
    try {


      val (ip, port) = splitIPandPort(myOptions.ipAndPort)
      val livestreamerOptions = new LSConfigOptions(vlcLocation = myOptions.playerLocation, fileLocation = myOptions
        .livestreamerConfigLocation, delay = myOptions.delay, ip = ip, vlcPort = port)

      println("\n\n****** " + url + " AUDIO STREAM LOCATED @ http://" + ip + ":" + port + " ******\n\n")

      LivestreamerConfigFileWriter.writeNewConfigFile(livestreamerOptions)

      createLiveStreamerProcess(configLocation = myOptions.livestreamerConfigLocation + livestreamerOptions.name, url =
        url, audioOptionName =
        AudioSettings.getAudioSettingFromURL(url))
    }catch {
      case e: ArrayIndexOutOfBoundsException => println(DEFAULT_USAGE)
      
    }
  }

  /**
   * Arg parser runs this when no config file is specified. Starts the livestreamer process with the arguments passed
   * to ArgParser through the command line  
   * @param args should be a length 3 string array with the first element being the url that you want to play, the 
   *             second element being the ipAdress and port that you want to use (separated by a colon,
   *             ex: 192.168.1.1:9999), and the third element being the location of where you want to store the 
   *             configuration file that this program uses to control livestreamer 
   */
  private def noConfigFile(args: Array[String]) = {
    if (args.size != 3) println(DEFAULT_USAGE)
    else {
      val url = args(0) 
      try {
        val (ipAddress, port) = splitIPandPort(args(1))
        val configLocation = args(2)
        val option = new LSConfigOptions(fileLocation = configLocation, ip = ipAddress, vlcPort = port)
        if (!option.validateNetworkInfo) println(IP_OR_PORT_ERROR)
        else {
          println("\n\n****** " + url + " AUDIO STREAM LOCATED @ http://" + ipAddress + ":" + port + " ******\n\n")
          LivestreamerConfigFileWriter.writeNewConfigFile(option)
          createLiveStreamerProcess(configLocation = option.fileLocation + option.name, url = url, audioOptionName =
            AudioSettings.getAudioSettingFromURL(url))
        }
      } catch {
        case e: ArrayIndexOutOfBoundsException => println(DEFAULT_USAGE)
        
      }

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


//    println(livestreamerPath + " --config " + configLocation + " " + url + " " + audioOptionName)
    try {
      val lsprocess = Process(livestreamerPath + " --config " + configLocation + " " + url + " " + audioOptionName)
      val runningProcess = lsprocess.run()
      //makes sure to send SIGINT to the livestreamer process so that VLC shuts down cleanly
      sys addShutdownHook ({
        runningProcess.destroy
      })

    }
    catch {
      case t: IOException => println("Livestreamer installation not found. Are you sure it's installed and in the " +
        "system path?")
    }

  }

  def splitIPandPort(url: String): (String, String) = {
    val splitInput = url.trim.split(":")
    (splitInput(0).trim, splitInput(1).trim)
  }
}