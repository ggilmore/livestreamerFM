package core

/**
 *An interface to safely capture and pass around errors in the core of the program. 
 * All of these are pretty self explanatory.
 * Created by gmgilmore on 3/4/15.
 */
sealed trait CoreError {
  def getErrorMessage: String
}

/**
 * Thrown by LSFMConfigFileParser
 */
case object NoOptionValSeparation extends CoreError {
  def getErrorMessage = "ERROR: Your LivestreamerFM config file looks incorrectly formatted. You had an " +
    "[OPTION] [VALUE] pair with no '=' sign to separate the two. \n       See the README.md for more info on how to " +
    "correctly" +
    " format the file."
}

case object InvalidOption extends CoreError {
  def getErrorMessage = "ERROR: You tried to specifiy an option in the LivestreamerFM configuration " +
    "file that was invalid. See the README.md for more info on how to correctly format the file."
}

case class ConfigFileNotFound(configPath: String) extends CoreError {
  def getErrorMessage = s"ERROR: The LivestreamerFM configuration file was not found at $configPath. " +
    "Make sure it's correct."
}

case class LSConfigWriterPathInvalid(lsOptionsPath:String, lsOptionsName:String) extends CoreError{
  def getErrorMessage =  s"LivestreamerFM was unable to write to ${lsOptionsPath + lsOptionsName}.Is this a real path?"
  
}
/**
 * Thrown by Util.splitIPAndPort
 */
case object IPAndPortFormatError extends CoreError {
  def getErrorMessage = "ERROR: The ip:Port parameter that you passed is incorrectly Formatted.\n See the README.md " +
    "for more info on how to correctly format the file."

}



