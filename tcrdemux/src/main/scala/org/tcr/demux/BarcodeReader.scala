package org.tcr.demux

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import scala.collection.JavaConversions._


object BarcodeReader {

  def parse(yamlData: String): Barcode = {
    val yaml =  new Yaml(new Constructor(classOf[Barcode]))
    val barcode = yaml.load(yamlData).asInstanceOf[Barcode]
    return barcode
  }
}