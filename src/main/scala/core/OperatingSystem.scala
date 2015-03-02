package core

import java.io.File

/**
 * Created by gmgilmore on 3/1/15.
 */
object OperatingSystem extends Enumeration {

  type operatingSystem = Value
  val OSX, WINDOWS_X86, WINDOWS_X64, LINUX, ??? = Value
  private val osName = System.getProperty("os.name")
  private val arch = System.getProperty("os.arch")
  private val osToVLCLocationMap = Map(OSX -> "/Applications/VLC.app/Contents/MacOS/VLC", LINUX -> "vlc",
    WINDOWS_X64 -> "C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe", WINDOWS_X86 -> "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe"
    , ??? -> "who knows")

  def getDefaultVLCLocation: String = {
    osToVLCLocationMap(getOS)
  }

  private def getOS: OperatingSystem.Value = {
    osName match {
      case os if osName.contains("OS X") => OSX
      case os if osName.contains("Linux") => LINUX
      case os if osName.contains("Windows") && arch == "x86" => WINDOWS_X86
      case os if osName.contains("Windows") && arch == "amd64" => WINDOWS_X64
      case _ => ???
    }


  }


}
