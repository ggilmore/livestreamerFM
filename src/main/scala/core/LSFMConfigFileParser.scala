package core

import java.io.File

import scala.collection.mutable.Map
import scala.io.Source

/**
 * Created by gmgilmore on 3/2/15.
 */
object LSFMConfigFileParser {


  def parseConfigFile(pathToConfig: String): LSFMConfigOptions = {

    val configFile = new File(pathToConfig)
    val lines = Source.fromFile(configFile).getLines().toVector
    val options: Map[String, String] = Map("player" -> OperatingSystem.getDefaultVLCLocation, "delay" -> "5000",
      "ipAndPort" -> "localhost:9000", "livestreamerConfigLocation" -> {
        val path = ArgParser.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
        val fixedPath = path.subSequence(0, path.lastIndexOf(File.separator) + 1)
        fixedPath.toString
      })

    for (line <- lines) {
      val splitLine = line.split("=")
      val (name, value) = (splitLine(0), splitLine(1))
      options(name.trim) = value.trim
    }

    new LSFMConfigOptions(options("player"), options("ipAndPort"), options("delay"), options
      ("livestreamerConfigLocation"))
  }


}
