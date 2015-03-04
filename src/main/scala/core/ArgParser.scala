package core

import java.io.{FileNotFoundException, File, IOException}
import java.net.FileNameMap

import core.Util._

import scala.io.Source
import scala.sys.process._

object ArgParser extends App {
  /*
   * Desired Arguments: [STREAM_URL] [IP_ADDRESS:PORT] [LIVESTREAMER CONFIG FILE LOCATION]
   */

  private val DEFAULT_USAGE = "USAGE: [STREAM_URL] [IP_ADDRESS:PORT] [LIVESTREAMER_CONFIG_FILE_LOCATION] " +
    "\nOR: --config [LIVESTREAMERFM_CONFIG_FILE_LOCATION] [STREAM_URL]"

  private val CONFIG_INVALID_OPTION = "ERROR: You tried to specifiy an option in the LivestreamerFM configuration " +
    "file that " +
    "was invalid. See the README.md for more info on how to correctly format the file."

  private val IP_OR_PORT_ERROR = "That doesn't look like a real IP-Address and/or port. Check it."

  private val LIVESTREAMER_LOCATION_OSX = "/usr/local/bin/livestreamer"

  args match {
    case Array("--config", configPath, url) => {
      try {
        val lines = Source.fromFile(new File(configPath)).getLines.toList
        verifyConfigFileArugments(lines, url) match {
          case Right(options) => createLiveStreamerProcess(options)
          case Left(errors) => errors.foreach(err => println(err.getErrorMessage))
        }
      }
      catch {
        case e: FileNotFoundException => println(s"ERROR: The LivestreamerFM configuration file was not found at $configPath. " +
          "Make sure it's correct")
      }
    }
    case Array(url, ipAndPort, lsConfigLocation) => {
      verifyNoConfigFileArguments(url, ipAndPort, lsConfigLocation) match {
        case Right(options) => createLiveStreamerProcess(options)
        case Left(errors) => errors.foreach(err => println(err.getErrorMessage))
      }
    }
    case _ => println(DEFAULT_USAGE)
  }

  /**
   * run by arg parser when the first option is --config, reads the config file given in the arguments and then 
   * * starts the livestreamer process
   *
   */
  private def verifyConfigFileArugments(configLines: List[String], url: String):
  Either[Set[GeneralError], LiveStreamerProcessInfo] = {

    LSFMConfigFileParser.parseConfigFile(configLines) match {
      case Right(myOptions) => {
        //good config
        splitIpAndPort(myOptions.ipAndPort) match {
          case Some((ip, port)) => {
            val livestreamerOptions = new LSConfigOptions(vlcLocation = myOptions.playerLocation, fileLocation =
              myOptions
                .livestreamerConfigLocation, delay = myOptions.delay, ip = ip, vlcPort = port)
            println(s"\n\n****** $url AUDIO STREAM LOCATED @ http:// $ip:$port ******\n\n")

            LivestreamerConfigFileWriter.writeNewConfigFile(livestreamerOptions) match {
              case Some(BadConfigPath) => Left(Set(ConfigFileNotFound(livestreamerOptions.fileLocation+ livestreamerOptions.name)))
              case None => Right(LiveStreamerProcessInfo(configLocation = livestreamerOptions.fileLocation + livestreamerOptions.name, 
                url = url, ip=livestreamerOptions.ip, port = livestreamerOptions.vlcPort, 
                audioOptionName = AudioSettings.getAudioSettingFromURL(url)))
            }
          }
          case None => Left(Set(IPAndPortFormatError))
        }
      }
      case Left(errSet) => Left(errSet)

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
  private def createLiveStreamerProcess(options:LiveStreamerProcessInfo) {


    //    println(livestreamerPath + " --config " + configLocation + " " + url + " " + audioOptionName)
    try {

      val lsprocess = Process(s"${options.livestreamerPath} --config ${options.configLocation} ${options.url} ${options.audioOptionName}")
      val runningProcess = lsprocess.run()
      println(s"\n\n****** ${options.url} AUDIO STREAM LOCATED @ http://${options.ip}:${options.port} ******\n\n")
      //makes sure to send SIGINT to the livestreamer process so that VLC shuts down cleanly
      sys addShutdownHook ({runningProcess.destroy})
    }
    catch {
      case t: IOException => println("Livestreamer installation not found. Are you sure it's installed and in the " +
        "system path?")
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
  private def verifyNoConfigFileArguments(url:String, ipAndPort:String, lsConfigLocation:String):Either[Set[GeneralError], LiveStreamerProcessInfo] = {

    splitIpAndPort(ipAndPort) match {
      case Some((ipAddress, port)) => {
        val option = LSConfigOptions(fileLocation = lsConfigLocation, ip = ipAddress, vlcPort = port)
        if (!option.validateNetworkInfo) Left(Set(IPAndPortFormatError))
        else {
          LivestreamerConfigFileWriter.writeNewConfigFile(option) match {
            case Some(BadConfigPath) => Left(Set(LSConfigWriterPathInvalid(option.fileLocation,option.name)))
            case None => Right(LiveStreamerProcessInfo(configLocation = option.fileLocation + option.name, url = url, ip = ipAddress, 
            port= port, audioOptionName = AudioSettings.getAudioSettingFromURL(url)))
          }
        }
      }
      case None => Left(Set(IPAndPortFormatError))
    }
    
  }
    
    private case class LiveStreamerProcessInfo(livestreamerPath:String = "livestreamer",
    configLocation:String, url:String, ip:String, port:String,audioOptionName:String)
  

}
    