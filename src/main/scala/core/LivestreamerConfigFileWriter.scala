package core

import java.io._

object LivestreamerConfigFileWriter {
  private val DO_NOT_EDIT = "#CONFIG FILE GENERATED BY LIVESTREAMERFM, DO NOT EDIT"

  /**
   * Creates a new configuration file for Livestreamer
   * @param options = the options to write into the config file
   *
   * @return a text file that contains all the configuration options 
   *         necessary to configure livestreamer
   */
  def writeNewConfigFile(options: LSConfigOptions) = {
    println(options.fileLocation + options.name)
    val writer = new PrintWriter(new File(options.fileLocation + options.name))
    val playerOptions = ("player=" + options.vlcLocation + " -I http —---network-caching <" + options.delay +
      "> —-playandexit --no-sout-video --sout-audio --sout '#standard{access=http,mux=asf,vcodec=h264,acodec=mp4a,dst="
      + options.ip + ":" + options.vlcPort + "}'")
    writer.write(DO_NOT_EDIT + "\n")
    writer.write(playerOptions)
    writer.close
  }

  //  def checkFile(filePath: String): Boolean = {
  //    val file = new File(filePath)
  //    file.exists() match {
  //      case false => false
  //      case true => {
  //        val lines = Vector() ++ Source.fromFile(file).getLines()
  //        val result = lines.filter { x => x.contains(DO_NOT_EDIT) }
  //        result.size > 0
  //      }
  //    }
  //  }

}