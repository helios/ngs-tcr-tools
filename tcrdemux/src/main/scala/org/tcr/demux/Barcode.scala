
package org.tcr.demux

import scala.beans.BeanProperty
import java.util.HashMap
import scala.collection.JavaConversions.mapAsScalaMap

/**
 * With the Snakeyaml Constructor approach shown in the main method,
 * this class must have a no-args constructor.
 */
class Barcode {
  @BeanProperty  var plate =  new HashMap[String,String]()
  @BeanProperty  var row   =  new HashMap[String,String]()
  @BeanProperty  var col   =  new HashMap[String,String]()

  def contains(splate:String, srow:String, scol:String): (Boolean, Boolean, Boolean) = (plate.contains(splate), row.contains(srow), col.contains(scol))

  def encode(splate:String, srow:String, scol:String): String = {
    contains(splate, srow, scol) match {
      case (true, true, true) => plate.get(splate)+row.get(srow)+col.get(scol)
      case (_, _, _) => ""
    }
  }

  // override def toString: String = {
  //   return format("acct (%s), user (%s), url (%s)", accountName, username, imapServerUrl)
  // }
}