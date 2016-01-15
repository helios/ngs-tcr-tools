package org.tcr.demux.io

import htsjdk.samtools.fastq.FastqRecord
import htsjdk.samtools.fastq.FastqWriterFactory
import htsjdk.samtools.fastq.FastqWriter
import scala.collection.concurrent.TrieMap
import org.bio.ngs.PairedEndReads
import org.bio.ngs.SingleEndReads


//TODO Design must be fiexed, I want to create just one filePool independently on the type of data contained Paired or Single End Reads. 

class Dispatch {
  
  val filePoolDemuxPairedEnd  = TrieMap[String, (FastqWriter, FastqWriter)]() 
  val filePoolDemuxSingleEnd  = TrieMap[String, FastqWriter]()

  val fastqFactory = new FastqWriterFactory
  
  def dispatch( code: String, read:PairedEndReads ) = {
    val (fileR1, fileR2) = filePoolDemuxPairedEnd.getOrElseUpdate( code, 
                (fastqFactory.newWriter(new java.io.File(code + "_R1.fastq.gz")), fastqFactory.newWriter( new java.io.File(code + "_R2.fastq.gz")))
    ) 
    fileR1.write(read.r1)
    fileR2.write(read.r2)
  }

  
  def dispatch( code: String, read:SingleEndReads ) = {
    val fileR1 = filePoolDemuxSingleEnd.getOrElseUpdate( code, 
                fastqFactory.newWriter(new java.io.File(code + "_R1.fastq.gz"))
    ) 
    fileR1.write(read.r1)
  }
  
  def close() = {
    for ((code, (fileR1, fileR2)) <- filePoolDemuxPairedEnd){
      fileR1.close
      fileR2.close
    }
    
    // TODO very ugly code, reminder to above comment.
    for ((code, fileR1) <- filePoolDemuxSingleEnd){
      fileR1.close
    }
  }

}