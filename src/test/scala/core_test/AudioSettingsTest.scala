package core_test

import core.AudioSettings._
import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by gmgilmore on 3/2/15.
 */
class AudioSettingsTest extends FlatSpec with Matchers {

  "AudioSettings.getAudioSettingFromURL" should "not return 'best' for a twitch.tv url that lacks 'www' and the " +
    "proctcol" in {
    assert(!(getAudioSettingFromURL("twitch.tv/arteezy") == "best"))

  }

  it should "not return 'best' for a twitch.tv url that lacks 'www' but has the http protocol" in {
    assert(!(getAudioSettingFromURL("http://twitch.tv/arteezy") == "best"))

  }

  it should "not return 'best' for a twitch.tv url that has 'www' but lacks the http protocol" in {
    assert(!(getAudioSettingFromURL("www.twitch.tv/arteezy") == "best"))

  }

  it should "not return 'best' for a twitch.tv url that has 'www' and the http protocol" in {
    assert(!(getAudioSettingFromURL("http://www.twitch.tv/arteezy") == "best"))

  }

  it should "not return 'best' for a youtube url that lacks 'www' and the proctcol" in {
    assert(!(getAudioSettingFromURL("youtube.com/watch?v=wDCIg4AMOq4") == "best"))

  }

  it should "not return 'best' for a youtube  url that lacks 'www' but has the http protocol" in {
    assert(!(getAudioSettingFromURL("https://youtube.com/watch?v=wDCIg4AMOq4") == "best"))

  }

  it should "not return 'best' for a youtube url that has 'www' but lacks the http protocol" in {
    assert(!(getAudioSettingFromURL("www.youtube.com/watch?v=wDCIg4AMOq4") == "best"))

  }

  it should "not return 'best' for a youtube url that has 'www' and  the http protocol" in {
    assert(!(getAudioSettingFromURL("https://www.youtube.com/watch?v=wDCIg4AMOq4") == "best"))

  }

  it should "return 'best' for a url that livestreamer doesn't support" in {
    assert((getAudioSettingFromURL("https://www.google.com") == "best"))

  }

  it should "return 'best' for a url that livestreamer doesn't have a pure audio stream for" in {
    assert((getAudioSettingFromURL("www.ustream.tv/leolaporte") == "best"))

  }

  it should "return 'best' for a url that is incorrectly formatted " in {
    assert((getAudioSettingFromURL("r.e.d.d.i.t") == "best"))
    assert((getAudioSettingFromURL("omgwtflol") == "best"))
  }

}
