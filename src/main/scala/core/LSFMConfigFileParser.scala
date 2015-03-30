package core

import java.io.File


/**
 * Parses configuration files for LiveStreamerFM.
 */
object LSFMConfigFileParser {

  val VALID_OPTIONS: Map[String, String] = Map("player" -> OperatingSystem.getOS.getDefaultVLCLocation, "delay" ->
    "5000",
    "ipandport" -> "localhost:9999", "livestreamerconfiglocation" -> {
      //This code is just to get the path of where livestreamerFM is currently running.
      val path = ArgParser.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
      val fixedPath = path.subSequence(0, path.lastIndexOf(File.separator) + 1)
      fixedPath.toString
    })

  /**
   * * 
   * @param lines lines from a LivestreamerFM configuration file, (format described in README.md) 
   *
   * @return either a Set of CoreError's, if the file format was not as described in README.md, or a 
   *         LSFMConfigOptions that contains all the information defined in the configuration file (or default 
   *         *         information as referred to 
   *         above)
   */
  def parseConfigFile(lines: List[String]): Either[Set[CoreError], LSFMConfigOptions] = {
    val results = lines.filter(x => !x.isEmpty).map(line => parseIndivididualLine(line)).toList
    val (err, userOptions) = split[CoreError, Option](results)
    if (err.nonEmpty) Left(err.toSet)
    else {
      val usersOptionsMap = userOptions.foldLeft(Map[String, String]()) { (m, x) => m.updated(x.name, x.value)}
      Right(LSFMConfigOptions.buildLSFMConfigOption(usersOptionsMap))
    }

  }


  /**
   *
   * @param line a line from a LivestreamerFM configuration file 
   * @return an Option containing the option's name, value pair, or a CoreError if 1) the line was incorrectly formatted
   *         or 2) if option is not one of the valid options as described in the README.md
   */
  private def parseIndivididualLine(line: String): Either[CoreError, Option] = {
    line.split("=").map(_.trim) match {
      case Array(name, value) => {
        if (VALID_OPTIONS.keySet.contains(name.toLowerCase)) Right(Option(name.toLowerCase, value))
        else Left(InvalidOption)
      }
      case _ => Left(NoOptionValSeparation)
    }
  }

  def split[E, V](results: List[Either[E, V]]): (List[E], List[V]) = {
    def loop(listE: List[E], listV: List[V], rest: List[Either[E, V]]): (List[E], List[V]) = rest match {
      case Nil => (listE, listV)
      case Right(v) :: xs => loop(listE, listV :+ v, xs)
      case Left(e) :: xs => loop(listE :+ e, listV, xs)
    }
    loop(List(), List(), results)
  }

  private case class Option(name: String, value: String)

}
