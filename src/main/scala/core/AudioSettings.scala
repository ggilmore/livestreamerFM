package core

import java.net.{MalformedURLException, URL}

/**
 * Convenient Enum for websites' audio settings for livestreamer. 
 *
 * If the plugin for Livestreamer doesn't have an audio only setting, then we'll just default to "best". If the url 
 * is bogus or livestreamer doesn't have a plugin  for the website, Livestreamer should fail anyway.
 * Created by gmgilmore on 3/1/15.
 */
object AudioSettings extends Enumeration {


  type SupportedWebsite = Value
  val TWITCH, YOUTUBE, UNSUPPORTED = Value
  private val websiteAudioMapping: Map[AudioSettings.Value, String] = Map(TWITCH -> "audio", YOUTUBE -> "audio_mp4",
    UNSUPPORTED -> "best")

  /**
   * helper function used to get the audio quality setting for livestreamer associated with a particular url 
   * @param url the url to get the livestreamer audio quality setting for (currently only twitch and youtube
   *            have their own special audio options)
   * @return the audio quality setting associated with a url, or "best" if the url is not supported by
   *         LivestreamerFM at this time (or if the "url" is not correctly formatted)
   */
  def getAudioSettingFromURL(url: String): String = websiteAudioMapping(getWebSiteFromURL(url))

  private def getWebSiteFromURL(url: String): AudioSettings.Value = {

    try {
      val host = new URL(
        if (url.startsWith("http://") || url.startsWith("https://")) url
        else if (url.startsWith("www")) "http://" + url
        else
          "http://www." + url).getHost.toLowerCase
      host match {
        case twitch if host.contains("twitch.tv") => AudioSettings.TWITCH
        case youtube if host.contains("youtube.com") => AudioSettings.YOUTUBE
        case _ => AudioSettings.UNSUPPORTED
      }
    } catch {
      case t: MalformedURLException => AudioSettings.UNSUPPORTED

    }
  }


}


