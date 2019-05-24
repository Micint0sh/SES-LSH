// Read the dataset

package run

import lsh.LSH

val startTime = System.currentTimeMillis()

lshModel = LSH.train(dataset, dim, numHashFunctions, numHashTables, binLength)

println(s"cost ${(System.currentTimeMillis() - startTime) / 1000}s to train")

queries.foreach(query => LSH.kNNSearch(lshModel, dataset, query, k))

