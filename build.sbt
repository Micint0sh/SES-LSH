name := "Sample Project"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.1"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.1.1"
libraryDependencies += "org.apache.hadoop" % "hadoop-core" % "1.2.1"
libraryDependencies += "org.apache.hbase" % "hbase" % "2.1.4"
libraryDependencies += "org.apache.hbase" % "hbase-client" % "2.1.4"
libraryDependencies += "org.apache.kafka" % "kafka_2.10" % "0.8.2.1"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.10" % "2.0.0"