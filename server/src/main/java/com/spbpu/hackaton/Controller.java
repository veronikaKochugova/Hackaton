package com.spbpu.hackaton;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class Controller {
    DataHandler dataHandler = new DataHandler();

    @RequestMapping(value = "/graph", method = RequestMethod.GET)
    public List<String> getGraphData(final @RequestParam(value = "country") String country) {
        return dataHandler.getDataForGraph(country);
    }

    @RequestMapping(value = "/pie", method = RequestMethod.GET)
    public Map<?,?> getPieChartData(final @RequestParam(value = "country") String country,
                                  final @RequestParam(value = "year") String year) {
        return dataHandler.getDataForPie(country, year);
    }

    @RequestMapping(value = "/years", method = RequestMethod.GET)
    public List<String> getYears(final @RequestParam(value = "country") String country) {
        return dataHandler.getYears(country);
    }

    @RequestMapping(value = "/allCountries", method = RequestMethod.GET)
    public List<?> getSet() {
        return dataHandler.getCountries();
    }

}
