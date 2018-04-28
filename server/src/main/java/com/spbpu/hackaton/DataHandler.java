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


    public static List<String> parseAreas(List<Row> stringList){
        String pattern = "[\\[\\]]";
        List<String> areas = new ArrayList<>();

        for (Row src : stringList) {
            String[] parts = src.toString().split(pattern);
            areas.add(parts[1]);
        }
        Collections.sort(areas);

        return areas;
    }
    public static List<String> parseDataForChart(String stringList){
        String pattern = "[\\[,+\\]]";
        List<String> areas = new ArrayList<>();
        String[] parts = stringList.split(pattern);

        int i=1;
        for (Integer year = 1961; year != 2014; year++){
            if (!parts[i].equals("null")){
                areas.add(year.toString());
            }
            i++;
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

        csv.createOrReplaceTempView("csv");

        Dataset<Row> sqlDF = sparkSession
                .sql("SELECT DISTINCT Area FROM csv");

        //sqlDF.groupBy().sum("1961");

        return country;
    }

    public List<?> getCountries() {
        csv.createOrReplaceTempView("csv");

        Dataset<Row> sqlDF = sparkSession
                .sql("SELECT DISTINCT Area FROM csv");
        return parseAreas(sqlDF.collectAsList());
    }

    public List<String> getYears(String country) {
        Dataset<Row> sqlDF = csv.filter(col("Area").like(country));
        sqlDF.createOrReplaceTempView("csv");

        String stringYear = "sum(CAST(Y1961 AS DOUBLE))";

        for (Integer year = 1962; year != 2014; year++){
            stringYear += ", sum(CAST(Y" + year.toString() + " AS DOUBLE))";
        }

        Dataset<Row> yearDF = sparkSession
                .sql("SELECT " + stringYear + " FROM csv");

        return parseDataForChart(yearDF.collectAsList().get(0).toString());
    }

    public List<?> getDataForPie(String country, String year) {
        List list = new ArrayList();
        list.add("product + value");
        list.add("other product + value");
        return list;
    }
}