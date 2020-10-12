// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.app.tools

// This is a little piece of ModelsLibraryDialog I wanted to write in Scala without having to
// convert the whole thing to Scala. - ST 2/27/11

import java.nio.file.{ Files, Path }
import java.io.File
import java.util.{ ArrayList => JArrayList, List => JList }

import com.typesafe.config.{ Config, ConfigException, ConfigFactory, ConfigParseOptions, ConfigSyntax }

import org.nlogo.app.infotab.InfoFormatter
import org.nlogo.workspace.ModelsLibrary

object ModelsLibraryIndexReader {

  // Most of `getWhatIsIt` is taken from ModelsLibrary.scala - used to pre-generated the info tab blurbs for the models
  // library display with `index.conf`.  I couldn't find an easy/simple way to share code between the build definition
  // and the project itself, so it's copied.  Since extension sample models cannot pre-generate their blurbs,
  // we do it live.  -Jeremy B October 2020
  def getWhatIsIt(path: Path): Option[String] = {
    import scala.collection.JavaConverters._
    val info = try {
      Files.readAllLines(path).asScala.mkString("\n").split("\\@\\#\\$\\#\\@\\#\\$\\#\\@\n")(2)
    } catch {
      case e: Exception => ""
    }

    val whatIsItPattern = "(?s).*## WHAT IS IT\\?\\s*\\n"
    if (info.matches(whatIsItPattern + ".*")) {
      val firstParagraph = info.replaceFirst(whatIsItPattern, "").split('\n').head
      val formattedFirstParagraph = InfoFormatter(firstParagraph)
      Some(formattedFirstParagraph)
    } else {
      None
    }
  }

  def readInfoMap: Map[String, String] = {
    import scala.collection.JavaConverters._

    val parsingConfiguration = ConfigParseOptions.defaults.setSyntax(ConfigSyntax.CONF)
    val index = ConfigFactory.parseFile(new File(ModelsLibrary.modelsRoot, "index.conf"), parsingConfiguration)
    val indexItems: JList[_ <: Config] =
      try {
        index.getConfigList("models.indexes")
      } catch {
        case missing: ConfigException.Missing =>
          new JArrayList[Config]()
      }

    indexItems.asScala.foldLeft(Map.empty[String, String]) {
      case (acc: Map[String, String], c: Config) =>
        try {
          val modelPath = c.getString("path")
          val modelInfo = c.getString("info")
          acc + (modelPath -> modelInfo)
        } catch {
          case missing: ConfigException.Missing => acc
        }
    }
  }
}
