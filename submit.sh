spark-submit \
    --class lsh.Main \
    --master proj99:38888 \
    --deploy-mode client \
    /data/sydeng/repositary/hnsw-repo/SES-LSH/target/scala-2.10/ses-lsh-run_2.10-1.0.jar \
    hdfs://master:9000/sydeng7/sift100m/sift100m_base.idvecs \
    /data/sydeng/repositary/hnsw-repo/data/sift100m/sift100m_query.fvecs \
    /data/sydeng/repositary/hnsw-repo/data/sift100m/SES-LSH-output \
    128 4 10 4.0 10