package org.bio.ngs

import htsjdk.samtools.fastq.FastqRecord
import htsjdk.samtools.fastq.FastqReader

case class PairedEndReads( r1: FastqRecord, r2: FastqRecord)
case class SingleEndReads( r1: FastqRecord)

class PairedEndStream(filenameForward: String, filenameReverse: String) {
  
   val readerForward = new FastqReader(new java.io.File(filenameForward))
   val readerReverse = new FastqReader(new java.io.File(filenameReverse))

   val reads: Stream[PairedEndReads]= {
        def reads(readerR1: FastqReader, readerR2: FastqReader): Stream[PairedEndReads] = {
          readerR1.hasNext() && readerR2.hasNext()  match {
            case true => PairedEndReads(readerR1.next(), readerR2.next()) #:: reads(readerR1, readerR2)
            case _ => Stream.empty
          }
        }
       reads(readerForward, readerReverse)
      }
}

class SingleEndStream(filenameForward: String) {
  
   val readerForward = new FastqReader(new java.io.File(filenameForward))
   
   val reads: Stream[SingleEndReads]= {
        def reads(readerR1: FastqReader): Stream[SingleEndReads] = {
          readerR1.hasNext()  match {
            case true => SingleEndReads(readerR1.next()) #:: reads(readerR1)
            case _ => Stream.empty
          }
        }
       reads(readerForward)
      }
}