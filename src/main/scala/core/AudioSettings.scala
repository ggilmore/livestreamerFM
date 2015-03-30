package core

import java.net.{MalformedURLException, URL}

/**
 * Convenient Enum for websites' audio settings for livestreamer. 
 *
 * If the plugin for Livestreamer doesn't have an audio only setting, then we'll just default to "best". If the url 
 * is bogus or livestreamer doesn't have a plugin  for the website, Livestreamer should fail anyway.
 * Created by gmgilmore on 3/1/15.
 */

trait AudioSettings {
  def getAudioSetting: String
}

object AudioSettings {
  /**
   * helper function used to get the audio quality setting for livestreamer associated with a particular url
   * @param url the url to get the livestreamer audio quality setting for (currently only twitch and youtube
   *            have their own special audio options)
   * @return the audio quality setting associated with a url, or "best" if the url is not supported by
   *         LivestreamerFM at this time (or if the "url" is not correctly formatted)
   */
  private def getWebSiteFromURL(url: String): AudioSettings = {
    try {
      val host = new URL(
        if (url.startsWith("http://") || url.startsWith("https://")) url
        else if (url.startsWith("www")) "http://" + url
        else
          "http://www." + url).getHost.toLowerCase
      host match {
        case twitch if host.contains("twitch.tv") => TWITCH
        case youtube if host.contains("youtube.com") => YOUTUBE
        case _ => NOT_RECOGNIZED
      }
    } catch {
      case t: MalformedURLException => NOT_RECOGNIZED

    }
  }
  
  def getAudioSettingFromURL(url:String):String = {
    getWebSiteFromURL(url).getAudioSetting
  }

}

case object TWITCH extends AudioSettings {
  def getAudioSetting: String = "audio"
}

case object YOUTUBE extends AudioSettings {
  def getAudioSetting: String = "audio_mp4"
}

case object NOT_RECOGNIZED extends AudioSettings {
  def getAudioSetting: String = "best"
}








