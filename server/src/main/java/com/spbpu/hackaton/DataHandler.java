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
                .option("header", "true")
                .load("resources/FAO.csv");
    }


    public static List<String> parseAreas(List<Row> stringList) {
        String pattern = "[\\[\\]]";
        List<String> areas = new ArrayList<>();

        for (Row src : stringList) {
            String[] parts = src.toString().split(pattern);
            areas.add(parts[1]);
        }
        Collections.sort(areas);

        return areas;
    }

    public static List<String> parseDataForChart(String stringList) {
        String pattern = "[\\[,+\\]]";
        List<String> areas = new ArrayList<>();
        String[] parts = stringList.split(pattern);

        int i = 1;
        for (Integer year = 1961; year != 2014; year++) {
            if (!parts[i].equals("null")) {
                areas.add(year.toString());
            }
            i++;
        }
        return areas;
    }

    public static Map<String, String> parseDataForChart(List<Row> sorted5){
        Map<String, String> map = new TreeMap<>();

        for (Row src : sorted5){
            String str = src.toString();
            String sum = str.substring(str.lastIndexOf(",") + 1, str.lastIndexOf("]"));
            if (sum.equals("null")) sum="0.0";
            String item = str.substring(1, str.lastIndexOf(","));
            map.put(item, sum);
        }
        return map;
    }

    public static List<String> parseDataForGraph(String stringList) {
        String pattern = "[\\[,+\\]]";
        List<String> areas = new ArrayList<>();
        String[] parts = stringList.split(pattern);

        for (int i = 1; i != parts.length; i++) {
            if (parts[i].equals("null")) {
                areas.add("0.0");
            } else {
                areas.add(parts[i]);
            }
        }
        return areas;
    }

    public String get() {
        return "";
    }

    public List<?> getCountries() {
        csv.createOrReplaceTempView("csv");

        Dataset<Row> sqlDF = sparkSession
                .sql("SELECT DISTINCT Area FROM csv");
        return parseAreas(sqlDF.collectAsList());
    }

    public List<String> getYears(String country) {
        Dataset<Row> sqlDF = csv.filter(col("Area").like(country.replace("_", " ")));
        sqlDF.createOrReplaceTempView("csv");

        String stringYear = "sum(CAST(Y1961 AS DOUBLE))";
        for (Integer year = 1962; year != 2014; year++) {
            stringYear += ", sum(CAST(Y" + year.toString() + " AS DOUBLE))";
        }
        Dataset<Row> yearDF = sparkSession.sql("SELECT " + stringYear + " FROM csv");

        return parseDataForChart(yearDF.collectAsList().get(0).toString());
    }

    public Map<?, ?> getDataForPie(String country, String year) {
        Dataset<Row> sqlDF = csv.filter(col("Area").like(country.replace("_", " ")));
        sqlDF.createOrReplaceTempView("csv");

        Dataset<Row> sorted5 = sparkSession.sql("SELECT Item, sum(CAST(Y" + year +
                " AS DOUBLE)) AS SumY FROM csv" +
                " GROUP BY Item").select("Item","SumY")
                .sort(col("SumY").desc()).limit(5);

        return parseDataForChart(sorted5.collectAsList());
    }

    public List<String> getDataForGraph(String country) {
        Dataset<Row> sqlDF = csv.filter(col("Area").like(country.replace("_", " ")));
        sqlDF.createOrReplaceTempView("csv");

        String stringYear = "sum(CAST(Y1961 AS DOUBLE))";
        for (Integer year = 1962; year != 2014; year++) {
            stringYear += ", sum(CAST(Y" + year.toString() + " AS DOUBLE))";
        }
        Dataset<Row> yearDF = sparkSession.sql("SELECT " + stringYear + " FROM csv");

        return parseDataForGraph(yearDF.collectAsList().get(0).toString());
    }
}