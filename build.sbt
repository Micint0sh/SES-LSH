name := "Sample Project"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "Repo at github.com/ankurdave/maven-repo" at "https://github.com/ankurdave/maven-repo/raw/master"

libraryDependencies += "com.ankurdave" %% "part" % "0.1"
libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.0"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "1.6.0"
libraryDependencies += "org.apache.hadoop" % "hadoop-core" % "1.2.1"
libraryDependencies += "org.apache.hbase" % "hbase" % "2.1.4"
libraryDependencies += "org.apache.hbase" % "hbase-client" % "2.1.4"
libraryDependencies += "org.apache.kafka" % "kafka_2.10" % "0.8.2.1"