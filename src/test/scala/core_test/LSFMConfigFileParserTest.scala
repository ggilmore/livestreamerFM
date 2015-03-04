package core_test

import java.io.{File, FileNotFoundException}

import core._
import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by gmgilmore on 3/3/15.
 */
class LSFMConfigFileParserTest extends FlatSpec with Matchers {

  "LSFMConfigFileParserTest.parseConfigFile" should "be able to parse a configuration file with all 4 options " +
    "correctly defined (ideal case)" in {
    val options = LSFMConfigFileParser.parseConfigFile("src/test/resources/LSFMConfigTestIdeal")
    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with all 4 options " +
    "in mixedCase" in {
    val options = LSFMConfigFileParser.parseConfigFile("src/test/resources/LSFMConfigTestMixedCase")
    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with extra leading and trailing whitespace around [OPTION] and " +
    "leading and trailing whitespace around [VALUE] (but not inside of value)" in {
    val options = LSFMConfigFileParser.parseConfigFile("src/test/resources/LSFMConfigTestWhitespace.txt")
    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with extra, unnecessary newlines before or after [OPTION] = " +
    "[VALUE] " +
    "pairs" in {
    val options = LSFMConfigFileParser.parseConfigFile("src/test/resources/LSFMConfigTestNewlines.txt")
    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with every option besides player present" in {
    val options = LSFMConfigFileParser.parseConfigFile("src/test/resources/LSFMConfigTestPlayerMissing")
    val correctOptions = new LSFMConfigOptions(OperatingSystem.getOS.getDefaultVLCLocation, "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with every option besides delay present" in {
    val options = LSFMConfigFileParser.parseConfigFile("src/test/resources/LSFMConfigTestDelayMissing.txt")
    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with every option besides ipAndPort present" in {
    val options = LSFMConfigFileParser.parseConfigFile("src/test/resources/LSFMConfigTestIpMissing")
    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with every option besides livestreamerConfigLocation present" in {
    val options = LSFMConfigFileParser.parseConfigFile("src/test/resources/LSFMConfigTestConfigLocMissing.txt")

    val path = ArgParser.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
    val fixedPath = path.subSequence(0, path.lastIndexOf(File.separator) + 1)


    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      fixedPath.toString)
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a blank configuration file and just use all the default options" in {
    val options = LSFMConfigFileParser.parseConfigFile("src/test/resources/LSFMConfigTestBlank")

    val path = ArgParser.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
    val fixedPath = path.subSequence(0, path.lastIndexOf(File.separator) + 1)

    val correctOptions = new LSFMConfigOptions(OperatingSystem.getOS.getDefaultVLCLocation, "localhost:9999", "5000",
      fixedPath.toString)
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "complain when the given path to the config file is incorrect" in {

    
    val options = LSFMConfigFileParser.parseConfigFile("src/test/resources/boguspath")
    assert(options.isLeft)
  }
  
  

}
