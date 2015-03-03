package core_test

import core.LSConfigOptions
import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by gmgilmore on 3/1/15.
 */
class LSConfigOptionTest extends FlatSpec with Matchers {

  "LSConfigOptions.validLookingNetworkingInfo" should "return true for an ip address that follows the correct format and a valid " +
    "networking port" in {
    val option = new LSConfigOptions(fileLocation = "src/test/resources/", ip = "192.168.1.1", vlcPort = "9999")
    assert(option.validateNetworkInfo)

  }

  it should "return true for an ip address that has all 0's  and a valid " +
    "networking port" in {
    val option = new LSConfigOptions(fileLocation = "src/test/resources/", ip = "0.0.0.0", vlcPort = "9999")
    assert(option.validateNetworkInfo)

  }

  it should "return true for an ip address that has all 255's and a valid " +
    "networking port" in {
    val option = new LSConfigOptions(fileLocation = "src/test/resources/", ip = "255.255.255.255", vlcPort = "9999")
    assert(option.validateNetworkInfo)

  }

  it should "return true for an ip address that is localhost and a valid " +
    "networking port" in {
    val option = new LSConfigOptions(fileLocation = "src/test/resources/", ip = "localhost", vlcPort = "9999")
    assert(option.validateNetworkInfo)

  }


  it should "return true for an ip address that follows the correct format and a " +
    "networking port that is 0" in {
    val option = new LSConfigOptions(fileLocation = "src/test/resources/", ip = "192.168.1.1", vlcPort = "0")
    assert(option.validateNetworkInfo)

  }

  it should "return true for an ip address that follows the correct format and a " +
    "networking port that is 65535" in {
    val option = new LSConfigOptions(fileLocation = "src/test/resources/", ip = "192.168.1.1", vlcPort = "65535")
    assert(option.validateNetworkInfo)

  }

  it should "return false for an ip address that is just some strange word and a " +
    "networking port that is valid" in {
    val option = new LSConfigOptions(fileLocation = "src/test/resources/", ip = "garbage", vlcPort = "9999")
    assert(!option.validateNetworkInfo)

  }

  it should "return false for an ip address that is outside of the allowed ranges and a " +
    "networking port that is valid" in {
    val option = new LSConfigOptions(fileLocation = "src/test/resources/", ip = "700.1.1.1", vlcPort = "9999")
    assert(!option.validateNetworkInfo)

  }

  it should "return false for an ip address that is not 4 numbers and a " +
    "networking port that is valid" in {
    val option = new LSConfigOptions(fileLocation = "src/test/resources/", ip = "1.1.1", vlcPort = "9999")
    assert(!option.validateNetworkInfo)

  }

  it should "return false for an ip address that follows the correct format and a " +
    "networking port that is garbage" in {
    val option = new LSConfigOptions(fileLocation = "src/test/resources/", ip = "192.168.1.1", vlcPort = "garbage")
    assert(!option.validateNetworkInfo)

  }

  it should "return false for an ip address that follows the correct format and a " +
    "networking port that is below 0" in {
    val option = new LSConfigOptions(fileLocation = "src/test/resources/", ip = "192.168.1.1", vlcPort = "-1")
    assert(!option.validateNetworkInfo)

  }

  it should "return false for an ip address that follows the correct format and a " +
    "networking port that is too high" in {
    val option = new LSConfigOptions(fileLocation = "src/test/resources/", ip = "192.168.1.1", vlcPort = "999999")
    assert(!option.validateNetworkInfo)

  }
  
  
  
  
}
