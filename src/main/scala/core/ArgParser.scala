package core

import java.io.{FileNotFoundException, File, IOException}
import core.Util._
import scala.io.Source
import scala.sys.process._

/**
 * * Main class that does the argument parsing, and hands those arguments off to helper functions that either start the 
 * livestreamer process with appropriate options or return error messages to the user.  
 */
object ArgParser extends App {
  /*
   * Desired Arguments: [STREAM_URL] [IP_ADDRESS:PORT] [LIVESTREAMER CONFIG FILE LOCATION]
   *                    --config [LIVESTREAMERFM_CONFIG_FILE_LOCATION] [STREAM_URL] 
   */

  private val DEFAULT_USAGE = "USAGE: [STREAM_URL] [IP_ADDRESS:PORT] [LIVESTREAMER_CONFIG_FILE_LOCATION] " +
    "\nOR: --config [LIVESTREAMERFM_CONFIG_FILE_LOCATION] [STREAM_URL]"
  
  args match {
    case Array("--config", configPath, url) => {
      try {
        val lines = Source.fromFile(new File(configPath)).getLines.toList
        verifyConfigFileArguments(lines, url) match {
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
   * * Run by arg parser when the config flag is set. 
   * @param configLines the lines from the configuration file passed to the argument parser
   * @param url the url to pass to livestreamer to grab the stream from
   * @return A set of CoreError's if there was an error when trying to read from the configuration file, or a 
   *         LivestreamerProcessInfo that has all the correct configuration options in order to start the livestreamer 
   *         process.          
   */
  private def verifyConfigFileArguments(configLines: List[String], url: String):
  Either[Set[CoreError], LiveStreamerProcessInfo] = {

    LSFMConfigFileParser.parseConfigFile(configLines) match {
      case Right(myOptions) => {
        //good config
        splitIpAndPort(myOptions.ipAndPort) match {
          case Some((ip, port)) => {
            val livestreamerOptions = new LSConfigOptions(vlcLocation = myOptions.playerLocation, fileLocation =
              myOptions
                .livestreamerConfigLocation, delay = myOptions.delay, ip = ip, vlcPort = port)
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
   * Starts the livestreamer process with all the correct flags set, prints the location of the audio stream. 
   *  Prints an error message if there was an error when trying to run livestreamer. 
   *  @param options a LiveStreamerProcessInfo instance that contains all the necessary settings in order to configure 
   *                 livestreamer. 
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
   * * 
   * @param url
   * @param ipAndPort
   * @param lsConfigLocation
   * @return
   */
  private def verifyNoConfigFileArguments(url:String, ipAndPort:String, lsConfigLocation:String):Either[Set[CoreError], LiveStreamerProcessInfo] = {

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
    