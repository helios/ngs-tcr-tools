//package org.tcr.demux

import scala.io.Source

import htsjdk.samtools.fastq.FastqRecord
import scala.collection.JavaConversions.asScalaIterator
import org.bio.io
import org.tcr.demux.Conf
import org.tcr.demux.BarcodeReader
import org.tcr.demux.TcrIndex
import org.tcr.demux.io.Dispatch 
import org.bio.ngs.PairedEndStream
import org.bio.ngs.SingleEndStream
import org.bio.ngs.SingleEndReads
import org.bio.ngs.PairedEndReads


object TcrDemux extends App {

  val params = new Conf(args)
  val barcode = BarcodeReader.parse( Source.fromURL(getClass.getResource(params.run.barcodes())).mkString )
  val fastqDispatcher = new Dispatch()

  // barcodes.run
  params.tuple match {
    case ("", _ , _) => println("Barcodes are not avaliable.")
    case (barcodeFileName, "", _) => println("Forward Reads are not available, this is strange.")
    
    case (barcodeFileName, forward, "") => {
      val data: SingleEndStream = new SingleEndStream(forward)
      for(read <- data.reads){
        TcrIndex.detect(barcode, read.r1) match {
          case "" => println("Error this reads can not be classified")
          case code => println("Found") //save file[s] on disk depending on the code
        }
      }
    }
    
    case (barcodeFileName, forward, reverse) =>{
      val data: PairedEndStream = new PairedEndStream(forward, reverse)
      for ( read <- data.reads) {
        TcrIndex.detect(barcode, read.r1, read.r2) match {
          case "" =>{ 
            //This reads can not be classified
            fastqDispatcher.dispatch("Unkown", read)
          }
          
          case code =>  {
          //save file[s] on disk depending on the code
            fastqDispatcher.dispatch(code, read)
          }
        }
      }
      fastqDispatcher.close // close all opened files.
      // TODO possible issue related with how many files OS can open simultaneously
    }
  }
  // println(params.run.readsforward())

}