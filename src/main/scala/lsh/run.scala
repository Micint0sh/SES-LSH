// Read the dataset

package lsh

import java.io._
import java.lang.System.currentTimeMillis
import java.nio.file.{Files, Paths}
import java.nio.{ByteBuffer, ByteOrder}

import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object loadDataset {

  val SIZE_INT = 4

  private def parseIdvecs(expectedDim: Int)(bytes: Array[Byte]) : (Long, Vector) = {
    val dim = ByteBuffer.wrap(bytes.slice(0, SIZE_INT)).order(ByteOrder.LITTLE_ENDIAN).getInt
    assert(dim == expectedDim, s"Dimensions in idvecs file not uniform, expected $expectedDim, got $dim")
    val id = ByteBuffer.wrap(bytes.slice((dim+1)*SIZE_INT, (dim+2)*SIZE_INT)).order(ByteOrder.LITTLE_ENDIAN).getInt
    var indexArray = Array.empty[Int]
    var dataArray = Array.empty[Double]
    for(i <- 1 to dim) {
      val thisData: Double = ByteBuffer.wrap(bytes.slice(i*SIZE_INT, (i+1)*SIZE_INT)).order(ByteOrder.LITTLE_ENDIAN).getFloat
      if (thisData != 0) {
        indexArray = indexArray :+ (i-1)
        dataArray = dataArray :+ thisData
      }
    }
    val result = Vectors.sparse(indexArray.length, indexArray, dataArray)
    (id, result)
  }

  def loadIdvecsDataSet(sc: SparkContext, path:String, dimension:Int) : RDD[(Long, Vector)] = {
    val bin_record = sc.binaryRecords(path, 4*(dimension+2))
    val data = bin_record.map(parseIdvecs(dimension))
    data
  }

  private def parseFvecs(expectedDim: Int)(bytes: Array[Byte]) : Vector = {
    val dim = ByteBuffer.wrap(bytes.slice(0, SIZE_INT)).order(ByteOrder.LITTLE_ENDIAN).getInt
    assert(dim == expectedDim, s"Dimensions in fvecs file not uniform, expected $expectedDim, got $dim")
    var indexArray = Array.empty[Int]
    var dataArray = Array.empty[Double]
    for(i <- 1 to dim) {
      val thisData:Double = ByteBuffer.wrap(bytes.slice(i*SIZE_INT, (i+1)*SIZE_INT)).order(ByteOrder.LITTLE_ENDIAN).getFloat
      if (thisData != 0) {
        indexArray = indexArray :+ (i-1)
        dataArray = dataArray :+ thisData
      }
    }
    val result = Vectors.sparse(indexArray.length, indexArray, dataArray)
    result
  }

  def loadFvecsLocal(path: String): Array[Vector] = {
    val bytes = Files.readAllBytes(Paths.get(path))
    val dim = ByteBuffer.wrap(bytes.slice(0, SIZE_INT)).order(ByteOrder.LITTLE_ENDIAN).getInt
    val lenPerEntry = SIZE_INT * (dim + 1)
    assert(bytes.length % lenPerEntry == 0, s"File length (${bytes.length}) not divisible by entry length ($lenPerEntry).")
    val result = Array.ofDim[Vector](bytes.length/lenPerEntry)
    for (i <- 0 until bytes.length/lenPerEntry) {
      result(i) = parseFvecs(dim)(bytes.slice(i*lenPerEntry, (i+1)*lenPerEntry))
    }
    result
  }

  /*
   * File format:
   * first line contains two int: #queries and k, separated by a space
   * each of the rest lines contains the result for one query, in the format of:
   * id1 dist1 id2 dist2 id3 dist3 ...
   */
  def writeKNNResult(path: String, results:Array[Array[(Long, Double)]]): Unit = {
    val writer = new PrintWriter(path)
    writer.print(results.length)
    writer.print(" ")
    writer.println(results(0).length)
    results.foreach(
      res => {
        res.foreach( tup => {
          writer.print(tup._1)
          writer.print(" ")
          writer.print(tup._2)
          writer.print(" ")
        }
        )
        writer.println()
      }
    )
  }
}

object Main extends App {
    if (args.length != 8) {
      println("Got " + args.length.toString + " arguments. Expected 8.")
      println(
        """Usage: [path to dataset file] [path to query file] [output path] [dimension]
                  |[num hash func] [num hash table] [bin length] [k]""".stripMargin)
      System.exit(0)
    }

    val dataSetPath = args(0)
    val queryPath = args(1)
    val oututPath = args(2)
    val dimension = args(3).toInt
    val numHashFunctions = args(4).toInt
    val numHashTables = args(5).toInt
    val binLength = args(6).toDouble
    val k = args(7).toInt

    val startTime = currentTimeMillis()

    // read dataset
    val sc = new SparkContext(new SparkConf().setAppName("SES-LSH-RUN"))
    val dataset = loadDataset.loadIdvecsDataSet(sc, dataSetPath, dimension)

    // train lsh model
    val lshModel = LSH.train(dataset, dimension, numHashFunctions, numHashTables, binLength)
    println(s"cost ${(currentTimeMillis() - startTime) / 1000}s to train")

    //load queries
    val queries = loadDataset.loadFvecsLocal(queryPath)
    val answer = queries.map(query => LSH.kNNSearch(lshModel, dataset, query, k)) // type Array[Array[Long, Double]]
    loadDataset.writeKNNResult(oututPath, answer)
}

