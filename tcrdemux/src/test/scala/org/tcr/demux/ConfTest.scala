package org.tcr.demux

import scala.io.Source
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter



class ConfTest extends  FunSuite with BeforeAndAfter {
  val filename: String = "/test.yml"
  var barcode: Barcode = _

  before {
    barcode = BarcodeReader.parse( Source.fromURL(getClass.getResource(filename)).mkString )
  }

  test("Getting the tuple from the configuration is ('/test.yml', 'R1.tar.gz', '')") {
    val conf = new Conf(Seq("run", "-b", filename, "-f", "R1.tar.gz"))

    assert(conf.tuple == ("/test.yml", "R1.tar.gz", ""))
  }

}