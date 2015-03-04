package core_test

/**
 * Created by gmgilmore on 3/1/15.
 */
import core.{LivestreamerConfigFileWriter, LSConfigOptions}
import java.io.{PrintWriter, File}

import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

class LSConfigFileWriterTest extends FlatSpec with Matchers {

  "LSConfigFileWriter" should "create a new file if none exists in the given path" in {
    val config = new File("src/test/resources/livestreamerconfig.txt")
    assert (!config.exists)
    val options = LSConfigOptions(fileLocation ="src/test/resources/", ip = "localhost" )
    LivestreamerConfigFileWriter.writeNewConfigFile(options)
    val written_config = new File("src/test/resources/livestreamerconfig.txt")
    assert (written_config.exists)
    written_config.delete
  }
  
  it should "overwrite a new file if one exists in the given path" in {
    //write new file
    val garbageFile = new File("src/test/resources/livestreamerconfig.txt")
    val writer = new PrintWriter(garbageFile)
    writer.write("GARBAGE")
    writer.close
    assert(garbageFile.exists)
    
    //write garbage to the file 
    val garbageLines = Source.fromFile(garbageFile).getLines.toList
    var foundGarbage = false
    for(line <- garbageLines) if (line.contains("GARBAGE")) foundGarbage = true
    assert(foundGarbage)
    
    
    //hopefully overwrite file 
    val options = new LSConfigOptions(fileLocation ="src/test/resources/", ip = "localhost" )
    LivestreamerConfigFileWriter.writeNewConfigFile(options)
    
    //no line in the file contains garbage 
    assert(!Source.fromFile(garbageFile).getLines.forall(line => line.contains("GARBAGE")))

    assert(!Source.fromFile(garbageFile).getLines.forall(x => !x.contains("player")))
    
    garbageFile.delete

  }
  
  it should "return an error if the filepath was unable to be accessed for whatever reason" in {
    
    
  }
}
