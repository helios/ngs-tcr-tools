package org.tcr.demux

import scala.io.Source
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter


class TcrDemuxTest extends  FunSuite with BeforeAndAfter {
  val filename: String = "/test.yml"
  var barcode: Barcode = _

  before {
    barcode = BarcodeReader.parse( Source.fromURL(getClass.getResource(filename)).mkString )

  }

  test("Passing from command line file R1.tar.gz, options is available") {
    val conf = new Conf(Seq("run", "-f", "R1.tar.gz"))
    assert(conf.run.forward.isSupplied == true)
  }

  test("Passing from command line only file R1.tar.gz, options reverse is not available") {
    val conf = new Conf(Seq("run", "-f", "R1.tar.gz"))
    assert(conf.run.reverse.isSupplied == false)
  }

  test("Passing from command line files R1.tar.gz and R2.tar.gz both options are availables") {
    val conf = new Conf(Seq("run", "-f", "R1.tar.gz", "-r", "R2.tar.gz"))
    assert(conf.run.forward.isSupplied == true)
    assert(conf.run.reverse.isSupplied == true)
  }

  test("Getting the tuple from the configuration is ('/test.yml', 'R1.tar.gz', ''), only forward") {
    val conf = new Conf(Seq("run", "-b", filename, "-f", "R1.tar.gz"))
    conf.tuple match {
      case ("", _ , _) => 
        assert(false)
      case (barcodeFileName, "", _) => 
        assert(false)
      case (barcodeFileName, forward, "") => 
        assert(true)
      case (barcodeFileName, forward, reverse) => 
        assert(false)
    }

  }

  test("Getting the tuple from the configuration is ('/test.yml', 'R1.tar.gz', 'R2.tar.gz'), both forward and reverse reads") {
    val conf = new Conf(Seq("run", "-b", filename, "-f", "R1.tar.gz", "-r", "R2.tag.gz"))
    conf.tuple match {
      case ("", _ , _) =>
        assert(false)
      case (barcodeFileName, "", _) =>
        assert(false)
      case (barcodeFileName, forward, "") =>
        assert(false)
      case (barcodeFileName, forward, reverse) =>
        assert(true)
    }
  }

  test("Getting the tuple from the configuration is ('', 'R1.tar.gz', 'XXXX'), Barcodes are not avaliable.") {
    val conf = new Conf(Seq("run", "-f", "R1.tar.gz"))
    conf.tuple match {
      case ("", _ , _) =>
        assert(true)
      case (barcodeFileName, "", _) =>
        assert(false)
      case (barcodeFileName, forward, "") =>
        assert(false)
      case (barcodeFileName, forward, reverse) =>
        assert(false)
    }

  }

  test("Getting the tuple from the configuration is ('/test.yml', '', 'xxxx'). Forward Reads are not available, this is strange.") {
    val conf = new Conf(Seq("run", "-b", filename, "-f", ""))
    conf.tuple match {
      case ("", _ , _) =>
        assert(false)
      case (barcodeFileName, "", _) =>
        assert(true)
      case (barcodeFileName, forward, "") =>
        assert(false)
      case (barcodeFileName, forward, reverse) =>
        assert(false)
    }

  }


}