package com.spbpu.hackaton;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class DataHandler {
    private SparkSession sparkSession;

    public DataHandler() {
        sparkSession = SparkSession.builder()
                .master("local")
                .appName("Spark Session Example")
                .config("spark.driver.memory", "471859200")
                .config("spark.testing.memory", "2147480000")
                .getOrCreate();
    }


    public static HashSet<String> parseAreas(List<Row> stringList){
        String pattern = "[\\[\\]]";
        HashSet<String> areas = new HashSet<>();

        for (Row src : stringList) {
            String[] parts = src.toString().split(pattern);
            areas.add(parts[1]);
        }
        return areas;
    }

    public String get() {

        return "";
    }

    public String getDataForMap(String year){

        return year;
    }

    public String getDataForChart(String country){

        return country;
    }

    public Set<?> getCountries() {
        Dataset<Row> csv = sparkSession.read()
                .format("csv")
                .option("header","true")
                .load("resources/FAO.csv");

        csv.createOrReplaceTempView("csv");

        Dataset<Row> sqlDF = sparkSession
                .sql("SELECT DISTINCT Area FROM csv");

        return parseAreas(sqlDF.collectAsList());
    }
}