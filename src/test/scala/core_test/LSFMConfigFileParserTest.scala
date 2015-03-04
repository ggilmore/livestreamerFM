package core_test

import java.io.{File, FileNotFoundException}

import core._
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

/**
 * Created by gmgilmore on 3/3/15.
 */
class LSFMConfigFileParserTest extends FlatSpec with Matchers {

  "LSFMConfigFileParserTest.parseConfigFile" should "be able to parse a configuration file with all 4 options " +
    "correctly defined (ideal case)" in {
    val lines = Source.fromFile(new File("src/test/resources/LSFMConfigTestIdeal")).getLines.toList
    val options = LSFMConfigFileParser.parseConfigFile(lines)
    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with all 4 options " +
    "in mixedCase" in {
    val lines = Source.fromFile(new File("src/test/resources/LSFMConfigTestMixedCase")).getLines.toList
    val options = LSFMConfigFileParser.parseConfigFile(lines)
    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with extra leading and trailing whitespace around [OPTION] and " +
    "leading and trailing whitespace around [VALUE] (but not inside of value)" in {
    val lines = Source.fromFile(new File("src/test/resources/LSFMConfigTestMixedCase")).getLines.toList
    val options = LSFMConfigFileParser.parseConfigFile(lines)
    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with extra, unnecessary newlines before or after [OPTION] = " +
    "[VALUE] " +
    "pairs" in {
    val lines = Source.fromFile(new File("src/test/resources/LSFMConfigTestNewlines.txt")).getLines.toList
    val options = LSFMConfigFileParser.parseConfigFile(lines)
    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with every option besides player present" in {
    val lines = Source.fromFile(new File("src/test/resources/LSFMConfigTestPlayerMissing")).getLines.toList
    val options = LSFMConfigFileParser.parseConfigFile(lines)
    val correctOptions = new LSFMConfigOptions(OperatingSystem.getOS.getDefaultVLCLocation, "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with every option besides delay present" in {
    val lines = Source.fromFile(new File("src/test/resources/LSFMConfigTestDelayMissing.txt")).getLines.toList
    val options = LSFMConfigFileParser.parseConfigFile(lines)
    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with every option besides ipAndPort present" in {
    val lines = Source.fromFile(new File("src/test/resources/LSFMConfigTestIpMissing")).getLines.toList
    val options = LSFMConfigFileParser.parseConfigFile(lines)
    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      "/Users/gmgilmore/")
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a configuration file with every option besides livestreamerConfigLocation present" in {
    val lines = Source.fromFile(new File("src/test/resources/LSFMConfigTestConfigLocMissing.txt")).getLines.toList
    val options = LSFMConfigFileParser.parseConfigFile(lines)
    val path = ArgParser.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
    val fixedPath = path.subSequence(0, path.lastIndexOf(File.separator) + 1)


    val correctOptions = new LSFMConfigOptions("/Applications/VLC.app/Contents/MacOS/VLC", "localhost:9999", "5000",
      fixedPath.toString)
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  it should "be able to parse a blank configuration file and just use all the default options" in {
    val lines = Source.fromFile(new File("src/test/resources/LSFMConfigTestBlank")).getLines.toList
    val options = LSFMConfigFileParser.parseConfigFile(lines)
    val path = ArgParser.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
    val fixedPath = path.subSequence(0, path.lastIndexOf(File.separator) + 1)

    val correctOptions = new LSFMConfigOptions(OperatingSystem.getOS.getDefaultVLCLocation, "localhost:9999", "5000",
      fixedPath.toString)
    assert(options.isRight)
    assert(options.right.get == correctOptions)
  }

  
  

}
