package core

import java.io.IOException

import core.LSFMConfigFileParser.{ConfigFileNotFound, InvalidOption, NoOptionValSeparation}

import scala.sys.process._

object ArgParser extends App {
  /*
   * Desired Arguments: [STREAM_URL] [IP_ADDRESS:PORT] [LIVESTREAMER CONFIG FILE LOCATION]
   */

  private val DEFAULT_USAGE = "USAGE: [STREAM_URL] [IP_ADDRESS:PORT] [LIVESTREAMER_CONFIG_FILE_LOCATION] " +
    "\nOR: --config [LIVESTREAMERFM_CONFIG_FILE_LOCATION] [STREAM_URL]"

  private val CONFIG_FORMAT_ERROR = "ERROR: Your LivestreamerFM config file looks incorrectly formatted. You had an " +
    "[OPTION]" +
    " [VALUE] pair with no '=' sign to separate the two. \n       See the README.md for more info on how to correctly" +
    " format " +
    "the file."

  private val CONFIG_INVALID_OPTION = "ERROR: You tried to specifiy an option in the LivestreamerFM configuration " +
    "file that " +
    "was invalid. See the README.md for more info on how to correctly format the file."

  private val IP_OR_PORT_ERROR = "That doesn't look like a real IP-Address and/or port. Check it."

  private val LIVESTREAMER_LOCATION_OSX = "/usr/local/bin/livestreamer"

  args match {
    case Array("--config", _, _) => yesConfigFile(args.tail)
    case Array(_, _, _) => noConfigFile(args)
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
    val Array(configPath, url) = args

    LSFMConfigFileParser.parseConfigFile(configPath) match {
      case Right(myOptions) => {
        //good config

        splitIpAndPort(myOptions.ipAndPort) match {
          case Some((ip, port)) => {
            val livestreamerOptions = new LSConfigOptions(vlcLocation = myOptions.playerLocation, fileLocation =
              myOptions

                .livestreamerConfigLocation, delay = myOptions.delay, ip = ip, vlcPort = port)

            println(s"\n\n****** $url AUDIO STREAM LOCATED @ http:// $ip:$port ******\n\n")

            LivestreamerConfigFileWriter.writeNewConfigFile(livestreamerOptions) match {
              case Some(BadConfigPath) => println(
                s"""LivestreamerFM was unable to write to
                   | ${livestreamerOptions.fileLocation + livestreamerOptions.name}.
                                                                                     |Is this a real path?"""
                  .stripMargin)
              case None => {
                createLiveStreamerProcess(configLocation = myOptions.livestreamerConfigLocation + livestreamerOptions
                  .name, url = url, audioOptionName = AudioSettings.getAudioSettingFromURL(url))
              }
            }
          }
          case None => println(DEFAULT_USAGE)
        }
      }
      case Left(errSet) => {
        //some error happened when we tried to read the config file
        errSet.foreach(error => error match {
          case ConfigFileNotFound => println(s"ERROR: The LivestreamerFM configuration file was not found at $configPath. " + 
            "Make sure it's correct")
          case InvalidOption => println(CONFIG_INVALID_OPTION)
          case NoOptionValSeparation => println(CONFIG_FORMAT_ERROR)
        })

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
      val lsprocess = Process(s"$livestreamerPath --config $configLocation $url $audioOptionName")
      val runningProcess = lsprocess.run()
      //makes sure to send SIGINT to the livestreamer process so that VLC shuts down cleanly
      sys addShutdownHook ({runningProcess.destroy})
    }
    catch {
      case t: IOException => println("Livestreamer installation not found. Are you sure it's installed and in the " +
        "system path?")
    }

  }

  private def splitIpAndPort(url: String): Option[(String, String)] = {
    val splitInput = url.trim.split(":")
    splitInput match {
      case Array(ip, port) => Some((ip, port))
      case _ => None //if there was no colon or if there are too many colons
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
    val Array(url, ipAndPort, configLocation) = args

    splitIpAndPort(ipAndPort) match {
      case Some((ipAddress, port)) => {
        val option = new LSConfigOptions(fileLocation = configLocation, ip = ipAddress, vlcPort = port)
        if (!option.validateNetworkInfo) println(IP_OR_PORT_ERROR)
        else {
          println(s"\n\n****** $url AUDIO STREAM LOCATED @ http://$ipAddress:$port ******\n\n")

          LivestreamerConfigFileWriter.writeNewConfigFile(option) match {
            case Some(BadConfigPath) => println(
              s"LivestreamerFM was unable to write to ${option.fileLocation + option.name}. Is this a real path?")
            case None => createLiveStreamerProcess(configLocation = option.fileLocation + option.name, url = url,
              audioOptionName = AudioSettings.getAudioSettingFromURL(url))
          }
        }
      }
      case None => println(DEFAULT_USAGE)
    }


  }


}