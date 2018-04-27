package com.spbpu.hackaton;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;



public class PrintRDD {

    public String get() {
        // configure spark
        SparkConf sparkConf = new SparkConf().setMaster("local")
                .setAppName("Spark")
                .set("spark.testing.memory", "2147480000");
        // start a spark context
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        // provide path to input text file
        String path = "resources/sample.txt";

        // read text file to RDD
        JavaRDD<String> lines = sc.textFile(path);

        // flatMap each line to words in the line
        JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(s.split(" ")).iterator());

        //count words which contain letters A and C
        long numAs = words.filter(s -> s.contains("a")).count();
        long numCs = words.filter(s -> s.contains("c")).count();

        // collect RDD for printing
//        for(String word:words.collect()){
//            System.out.println(word);
//        }

        //System.out.println("words with a: " + numAs + ", words with c: " + numCs);
        //return numAs;
        return new String("words with a: " + numAs + ", words with c: " + numCs);
        //return (long)1;
    }
}