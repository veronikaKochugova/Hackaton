package com.spbpu.hackaton;

import java.util.*;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;

public class DataHandler {
    private SparkSession sparkSession;
    Dataset<Row> csv;

    public DataHandler() {
        sparkSession = SparkSession.builder()
                .master("local")
                .appName("Spark Session Example")
                .config("spark.driver.memory", "471859200")
                .config("spark.testing.memory", "2147480000")
                .getOrCreate();
        csv = sparkSession.read()
                .format("csv")
                .option("header","true")
                .load("resources/FAO.csv");
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

        Dataset<Row> csv = sparkSession.read()
                .format("csv")
                .option("header","true")
                .load("resources/FAO.csv");

        csv.createOrReplaceTempView("csv");

        Dataset<Row> sqlDF = sparkSession
                .sql("SELECT DISTINCT Area FROM csv");

        //sqlDF.groupBy().sum("1961");

        return country;
    }

    public Set<?> getCountries() {
        csv.createOrReplaceTempView("csv");

        Dataset<Row> sqlDF = sparkSession
                .sql("SELECT DISTINCT Area FROM csv");
        return parseAreas(sqlDF.collectAsList());
    }

    public List<String> getYears(String country) {
        Dataset<Row> sqlDF = csv.filter(col("Area").like(country));
        sqlDF.createOrReplaceTempView("csv");

        List<String> years = new ArrayList<>();

        for (Integer year = 1961; year != 2014; year++){
            Dataset<Row> yearDF = sparkSession
                    .sql("SELECT sum(CAST(Y" + year.toString() +
                            " AS DOUBLE)) FROM csv");
            try {
                yearDF.first().getDouble(0);
            } catch (java.lang.NullPointerException j) {
                // data for year is null - skip
                continue;
            }
            years.add(year.toString());
        }

        Collections.sort(years);
        return years;
    }

    public List<?> getDataForPie(String country, String year) {
        List list = new ArrayList();
        list.add("product + value");
        list.add("other product + value");
        return list;
    }
}