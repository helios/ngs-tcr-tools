package org.tcr.demux

import scala.io.Source
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter


class BarcodeReaderTest extends  FunSuite with BeforeAndAfter {
  val filename: String = "/test.yml"
  var barcode: Barcode = _

  before {
    barcode = BarcodeReader.parse( Source.fromURL(getClass.getResource(filename)).mkString )
  }

  test("Barcode has these three plates GCAGA, TCGAA, AACAA") {
    assert(barcode.plate.get("GCAGA") == "1")
    assert(barcode.plate.get("TCGAA") == "2")
    assert(barcode.plate.get("AACAA") == "3")
  }

  test("Barcode has these tow rows   TAAGC, TGCAC") {
    assert(barcode.row.get("TAAGC") == "A")
    assert(barcode.row.get("TGCAC") == "B")
  }

  test("Barcode has these four columns TGAAC, TCCTG, TATAA, ACAGG") {
    assert(barcode.col.get("TGAAC") == "1")
    assert(barcode.col.get("TCCTG") == "2")
    assert(barcode.col.get("TATAA") == "3")
    assert(barcode.col.get("ACAGG") == "4")
  }

  test("Barcode contains plate GCAGA, row TAAGC and col TGAAC") {
    assert(barcode.contains("GCAGA", "TAAGC", "TGAAC") == (true, true, true))
  }

}