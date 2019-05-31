name := "SES-LSH-RUN"

version := "1.0"

scalaVersion := "2.11.7"

//resolvers += "Spark Packages Repo" at "https://dl.bintray.com/spark-packages/maven"
resolvers += "Repo at github.com/ankurdave/maven-repo" at "https://github.com/ankurdave/maven-repo/raw/master"

libraryDependencies += "com.ankurdave" % "part_2.10" % "0.1"
libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.3"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "1.6.3"
libraryDependencies += "org.apache.hadoop" % "hadoop-core" % "1.2.1"
libraryDependencies += "org.apache.hbase" % "hbase" % "2.1.4"
libraryDependencies += "org.apache.hbase" % "hbase-client" % "2.1.4"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-8_2.10" % "2.1.0"