package org.bio.io
import java.io._

class FastaReader(val filename: String) extends Iterator[Tuple3[String,String,String]] {
  private lazy val reader = new BufferedReader(new FileReader(filename))

  class FastaReadException(string: String) extends Exception(string)
  def hasNext() = reader.ready
  def next(): Tuple3[String,String,String] = {
    // Read the tag line
    val tag = reader.readLine
    if (tag(0) != '>')
      throw new FastaReadException("record start expected")
    var sequencelist = ""
    // Read the sequence body
    do {
      reader.mark(512)  // 512 is sufficient for a single tag line
      val line = reader.readLine
      if (line(0) != '>') sequencelist += line
      if (!reader.ready || line(0) == '>') {
        // Reached the end of the sequence
        if (reader.ready) reader.reset
        // Remove prepending '>'
        val tag2 = tag.drop(1).trim
        val id = tag2.split(Array(' ','\t'))(0)
        return (id,tag2,sequencelist)
      }
    } while (reader.ready)
    // should never reach this...
    throw new FastaReadException("Error in file "+filename+" (tag="+tag+")")
  }
}