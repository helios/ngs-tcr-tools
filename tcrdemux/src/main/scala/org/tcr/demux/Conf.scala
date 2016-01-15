package org.tcr.demux

import org.rogach.scallop.ScallopConf
import org.rogach.scallop.Subcommand


class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  version("TCR Reads Demultiplexing 0.1 Copyright(c) Raoul Jean Pierre Bonnal")
  val run = new Subcommand("run") {
    val barcodes = opt[String](name="barcodes",short='b',descr="YAML file with the barcodes used to multiplex tcr redas", default=Some("/barcodes.yml"))
    val forward = opt[String](name="forward",short='f',descr="File name for the forward/R1 reads", required = true)
    val reverse = opt[String](name="reverse",short='r',descr="File name for the reverse/R2 reads")
  }

  def tuple(): (String, String, String) = {
    val barcodes:String = this.run.barcodes.isSupplied match {
      case true => this.run.barcodes()
      case false => ""
    }

    val forward:String = this.run.forward.isSupplied match {
      case true => this.run.forward()
      case false => ""
    }

    val reverse:String = this.run.reverse.isSupplied match {
      case true => this.run.reverse()
      case false => ""
    }


    return new Tuple3( barcodes, forward, reverse)
  }
  // val jobs = new Subcommand("jobs") {
  //   val list = opt[Boolean](name="list",short='l',descr="List running jobs")
  //   val delete = opt[List[String]](name="delete",short='d',descr="Delete submitted jobs ('all' to erase everything or type one or more job IDs)")
  // }
  // val create = opt[List[String]](name="create",short='c',descr="Create samples.yml file from a Sample directory (only for Illumina folders)")
}