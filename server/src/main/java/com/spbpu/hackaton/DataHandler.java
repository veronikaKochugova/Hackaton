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
            areas.add(parts[1].replace(" ", "_"));
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

    public static Map<String, Double> parseDataForChart(List<Row> sorted5, List<Row> sumRest){
        Map<String, Double> map = new TreeMap<>();

        for (Row src : sorted5){
            String str = src.toString();
            String sum = str.substring(str.lastIndexOf(",") + 1, str.lastIndexOf("]")-1);
            String item = str.substring(1, str.lastIndexOf(","));
            map.put(item, Double.parseDouble(sum));
        }
        String rest = sumRest.toString();
        map.put("Others",Double.parseDouble(rest.substring(2,rest.length()-2)));

        for (Map.Entry<String, Double> entry : map.entrySet()){
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        return map;
    }

    public String get() {

        return "";
    }

    public String getDataForMap(String year) {

        return year;
    }

    public String getDataForChart(String country) {

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
        Dataset<Row> sqlDF = csv.filter(col("Area").like(country.replace("_"," ")));
        sqlDF.createOrReplaceTempView("csv");

        String stringYear = "sum(CAST(Y1961 AS DOUBLE))";

        for (Integer year = 1962; year != 2014; year++) {
            stringYear += ", sum(CAST(Y" + year.toString() + " AS DOUBLE))";
        }

        Dataset<Row> yearDF = sparkSession
                .sql("SELECT " + stringYear + " FROM csv");

        return parseDataForChart(yearDF.collectAsList().get(0).toString());
    }

    public Map<?, ?> getDataForPie(String country, String year) {
        Dataset<Row> sqlDF = csv.filter(col("Area").like(country.replace("_"," ")));
        sqlDF.createOrReplaceTempView("csv");

        Dataset<Row> sql2 = sparkSession.sql("SELECT Item, sum(CAST(Y" + year +
                " AS DOUBLE)) AS SumY FROM csv" +
                " GROUP BY Item");
        Dataset<Row> sorted5 = sql2.select("Item","SumY")
                .sort(col("SumY").desc()).limit(10);

        Dataset<Row> sumRest = sql2.except(sorted5);
        sumRest.createOrReplaceTempView("sumRest");
        sumRest = sparkSession.sql("SELECT sum(SumY) FROM sumRest");

        return parseDataForChart(sorted5.collectAsList(), sumRest.collectAsList());
    }

    public List<String> getDataForGraph(String country) {
        List list = new ArrayList();
        for (Integer i = 0; i < 30; ++i) list.add(String.valueOf(i));
        return list;
    }
}