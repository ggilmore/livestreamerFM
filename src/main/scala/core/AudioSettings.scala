package core

import java.net.URL

/**
 * Convenient Enum for websites' audio settings for livestreamer 
 * Created by gmgilmore on 3/1/15.
 */
object AudioSettings extends Enumeration {


  type SupportedWebsite = Value
  val TWITCH, YOUTUBE, UNSUPPORTED = Value
  private val websiteAudioMapping: Map[AudioSettings.Value, String] = Map(TWITCH -> "audio", YOUTUBE -> "audio_mp4",
    UNSUPPORTED -> "best")

  /**
   * helper function used to get the audio quality setting for livestreamer associated with a particular url 
   * @param url the http url to get the livestreamer audio quality setting for
   * @return the audio quality setting associated with a url, or "hot_garbage" if the url is not supported by 
   *         LivestreamerFM at this time (or if the "url" is not correctly formatted)
   */
  def getAudioSettingFromURL(url: String): String = websiteAudioMapping(getWebSiteFromURL(url))

  private def getWebSiteFromURL(url: String): AudioSettings.Value = {
    val host = new URL(if (url.startsWith("http://")) url else "http://" + url).getHost.toLowerCase
    host match {
      case "twitch.tv" => AudioSettings.TWITCH
      case "youtube.com" => AudioSettings.YOUTUBE
      case _ => AudioSettings.UNSUPPORTED
    }
  }


}


