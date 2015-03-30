package core

/**
 * Created by gmgilmore on 3/4/15.
 */
object Util {
  
  def splitIpAndPort(url: String): Option[(String, String)] = {
    val splitInput = url.trim.split(":")
    splitInput match {
      case Array(ip, port) => Some((ip, port))
      case _ => None //if there was no colon or if there are too many colons
    }

  }
  
   
  
}

