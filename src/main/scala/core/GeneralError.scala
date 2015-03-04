package core

/**
 * Created by gmgilmore on 3/4/15.
 */
sealed trait GeneralError {
  def getErrorMessage: String
}

/**
 * Thrown by LSFMConfigFileParser
 */
case object NoOptionValSeparation extends GeneralError {
  def getErrorMessage = "ERROR: Your LivestreamerFM config file looks incorrectly formatted. You had an " +
    "[OPTION] [VALUE] pair with no '=' sign to separate the two. \n       See the README.md for more info on how to " +
    "correctly" +
    " format the file."
}

case object InvalidOption extends GeneralError {
  def getErrorMessage = "ERROR: You tried to specifiy an option in the LivestreamerFM configuration " +
    "file that was invalid. See the README.md for more info on how to correctly format the file."
}

case class ConfigFileNotFound(configPath: String) extends GeneralError {
  def getErrorMessage = s"ERROR: The LivestreamerFM configuration file was not found at $configPath. " +
    "Make sure it's correct"
}

case class LSConfigWriterPathInvalid(lsOptionsPath:String, lsOptionsName:String) extends GeneralError{
  def getErrorMessage =  s"LivestreamerFM was unable to write to ${lsOptionsPath + lsOptionsName}.Is this a real path?"
  
}
/**
 * Thrown by Util.splitIPAndPort
 */
case object IPAndPortFormatError extends GeneralError {
  def getErrorMessage = "ERROR: The ip:Port parameter that you passed is incorrectly Formatted.\n See the README.md " +
    "for more info on how to correctly format the file."

}

