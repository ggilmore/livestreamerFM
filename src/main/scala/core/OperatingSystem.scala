package core

import java.io.File

/**
 * Created by gmgilmore on 3/1/15.
 */
object OperatingSystem extends Enumeration {

  type operatingSystem = Value
  val OSX, WINDOWS_X86, WINDOWS_X64, LINUX, ??? = Value
  private val osName = System.getProperty("os.name")
  private val osToVLCLocationMap = Map(OSX -> "/Applications/VLC.app/Contents/MacOS/VLC", LINUX -> "vlc",
    WINDOWS_X64 -> "C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe", WINDOWS_X86 -> "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe"
    , ??? -> "who knows")

  def getDefaultVLCLocation: String = {
    osToVLCLocationMap(getOS)
  }

  /**
   * Code found here: http://stackoverflow.com/a/2269242
   *  
   * @return true if this program is running on 64 bit windows, false otherwise
   */
  private def is64BitWin: Boolean = {
    var is64bit = false
    if (System.getProperty("os.name").contains("Windows")) {
      is64bit = (System.getenv("ProgramFiles(x86)") != null);
    } else {
      is64bit = (System.getProperty("os.arch").indexOf("64") != -1);
    }
    is64bit
  }

  private def getOS: OperatingSystem.Value = {
    osName match {
      case os if osName.contains("OS X") => OSX
      case os if osName.contains("Linux") => LINUX
      case os if osName.contains("Windows") && !is64BitWin => WINDOWS_X86
      case os if osName.contains("Windows") && is64BitWin => WINDOWS_X64
      case _ => ???
    }


  }


}
