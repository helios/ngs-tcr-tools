package org.tcr.demux

import scala.io.Source
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import htsjdk.samtools.reference.FastaSequenceIndex
import htsjdk.samtools.reference.FastaSequenceIndexEntry
import org.bio.io.FastaReader

class TcrIndexFastaTest extends  FunSuite with BeforeAndAfter {
  val filename: String = "/barcodes.yml"
  val sequences: String = "/Han-Glanvill.fa"
  var barcode: Barcode = _

  before {
    barcode = BarcodeReader.parse( Source.fromURL(getClass.getResource(filename)).mkString )
  }

  test("Detect barcodes in FastqReads") {
    val reader = new FastaReader(getClass.getResource(sequences).getPath)
    var list = List[String]()
    for (read <- reader){
      read match {
        case (id, tag, sequence) => 
          list = list:+ TcrIndex.detect(barcode, sequence)
      }
      
    }
    // this does not work properly need to study more.
    // list = reads.scanLeft(list)((acc, read) =>
    //   acc.append(TcrIndex.detect(barcode, read))
    // )
    assert(list == List("11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1", "11A1"))
  }


}