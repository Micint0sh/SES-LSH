name := "Sample Project"

version := "1.0"

scalaVersion := "2.10.7"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.3"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "1.6.3"
libraryDependencies += "org.apache.hadoop" % "hadoop-core" % "1.2.1"
libraryDependencies += "org.apache.hbase" % "hbase" % "2.1.4"
libraryDependencies += "org.apache.hbase" % "hbase-client" % "2.1.4"
libraryDependencies += "org.apache.kafka" % "kafka_2.10" % "0.10.0.0"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.10" % "2.0.0"