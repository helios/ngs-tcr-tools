package org.tcr.demux

import scala.io.Source

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import htsjdk.samtools.fastq.FastqReader
import htsjdk.samtools.fastq.FastqRecord
import scala.collection.JavaConversions.asScalaIterator // I just explicitly select the conversion class

import org.tcr.demux.io.Dispatch 
import org.bio.ngs.PairedEndReads
import org.bio.ngs.SingleEndReads
import org.bio.ngs.PairedEndStream
import org.bio.ngs.SingleEndStream


class TcrIndexFastqTest extends  FunSuite with BeforeAndAfter {
  val filename: String = "/test.yml"
  val r1: String = "/R1.fastq.gz"
  val r2: String = "/R2.fastq.gz"
  var barcode: Barcode = _

  before {
    barcode = BarcodeReader.parse( Source.fromURL(getClass.getResource(filename)).mkString )
  }

  test("Detect barcodes in FastqReads") {
    val reads = new FastqReader(new java.io.File(getClass.getResource("/R1.fastq.gz").getPath))
    var list = List[String]()
    for (read <- reads){
      list = list:+ TcrIndex.detect(barcode, read)
    }
    // this does not work properly need to study more.
    // list = reads.scanLeft(list)((acc, read) =>
    //   acc.append(TcrIndex.detect(barcode, read))
    // )
    assert(list == List("1A1","1B4","" ,"" ,"" ,"" ,"" ,"" ,"" ,"" ,""))
  }


  test("Detect barcodes in forward and reverse reads in Fastq format") {
    val filepathR1 = getClass.getResource(r1).getPath
    val filepathR2 = getClass.getResource(r2).getPath
    var list = List[String]()
    val fastqDispatcher = new Dispatch()
    val data: PairedEndStream = new PairedEndStream(filepathR1, filepathR2)
        
    for ( read <- data.reads) {
        TcrIndex.detect(barcode, read.r1, read.r2) match {
          case "" => {
            //println("Error this reads can not be classified")
            // save data on disk, Unkown container plus R1 or R2
            fastqDispatcher.dispatch("Unkown", read)
          }
          case code =>  {
            list = list:+ code
            // save data on disk each file is named with the name of the decoded index plus R1 or R2
            fastqDispatcher.dispatch(code, read)
            
          }
        }
      }
      fastqDispatcher.close // close all opened files.
    
    assert(list == List("1A1","1B4"))
  }

}