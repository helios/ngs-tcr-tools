package org.tcr.demux

import htsjdk.samtools.fastq.FastqRecord

object TcrIndex {

/*
 *   pattern is 5' NN[plate 5mer]NN[row 5mer]NNNNN[sequence ~250-310bp]NNN[col 5mer]NN 3'
 *   12345678901234567890
 *   NN.....NN.....NNNNNN
 *   Return the code encoded by the string[s]
 *  
 */
  def detect(codec:Barcode, sequence:String): String = {
    val length = sequence.length
    val plate = sequence.substring(2,7)
    val row = sequence.substring(9,14)
    val col = sequence.substring(length-7, length-2)

    codec.encode(plate, row, col)
  }

  /* 
   * sequenceReverse is not reversed and complemented is just the R2 from the Illumina Sequences.
   * So to extract the last code (the col) from this kind of sequence we extract the code from the beginning 
   * of the sequence and later on revert and complement it without using BioJava.
   * In the original script provided by Jacob Glanville ref. fasta-hiseq-join.pl 
   * the code revert and complement the whole sequence because they were fusing both the sequences R1 and R2 in just one
   * sequence assuming (after check) an overlap. We want to keep the sequence separated but at the same time detect from 
   * which original barcode are originated.  
   */
  def detect(codec:Barcode, sequenceForward:String, sequenceReverse:String): String = {
    val length = sequenceForward.length
    val plate = sequenceForward.substring(2,7)
    val row = sequenceForward.substring(9,14)
    val col = complement(sequenceReverse.substring(2,7).reverse)
    codec.encode(plate, row, col)
  }

  
  def detect(codec:Barcode, read:FastqRecord): String =
    detect(codec, read.getReadString)

  def detect(codec:Barcode, readForward:FastqRecord, readReverse:FastqRecord): String =
    detect(codec, readForward.getReadString, readReverse.getReadString)

  //candidate to be places in a different package org.bio.sequence
    // TODO those are the supported characters, if any other char is present in the string Scala will raise an exception (useful to check bugs)
  def complement(sequence: String): String =
    sequence map {
      case 'A' => 'T'
      case 'C' => 'G'
      case 'G' => 'C'
      case 'T' => 'A'
      case 'a' => 't'
      case 'c' => 'g'
      case 'g' => 'c'
      case 't' => 'a'
      case 'N' => 'N'
      case 'n' => 'n'
    }
}