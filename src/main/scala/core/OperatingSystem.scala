package core


/**
 * Created by gmgilmore on 3/1/15.
 */
trait OperatingSystem {
  /**
   *
   * @return the path to the default vlc location for the operating system that this program is running on
   */
  def getDefaultVLCLocation: String
}

object OperatingSystem {
  def getOS: OperatingSystem = {
    val osName = System.getProperty("os.name")
    osName match {
      case osx if osName.contains("OS X") => OSX
      case linux if osName.contains("Linux") => LINUX
      case win if osName.contains("Windows") && !is64BitWin => WINDOWS_X86
      case win if osName.contains("Windows") && is64BitWin => WINDOWS_X64
      case _ => UNKNOWN
    }
  }

  /**
   * Code found here: http://stackoverflow.com/a/2269242
   *
   * @return true if this program is running on 64 bit windows, false otherwise
   */
  private def is64BitWin: Boolean = {
    var is64bit = false
    if (System.getProperty("os.name").contains("Windows")) {
      is64bit = System.getenv("ProgramFiles(x86)") != null
    } else {
      is64bit = System.getProperty("os.arch").indexOf("64") != -1
    }
    is64bit
  }

}

case object OSX extends OperatingSystem {
  def getDefaultVLCLocation: String = "/Applications/VLC.app/Contents/MacOS/VLC"
}

case object WINDOWS_X86 extends OperatingSystem {
  def getDefaultVLCLocation: String = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe"
}

case object WINDOWS_X64 extends OperatingSystem {
  def getDefaultVLCLocation: String = "C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe"
}

case object LINUX extends OperatingSystem {
  def getDefaultVLCLocation: String = "vlc"
}

case object UNKNOWN extends OperatingSystem {
  def getDefaultVLCLocation: String = "???"
}





