package com.eurovision.votingsystem.results;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
/**
 * Reads from year.json file
 * Outputs the results for the country specified
 */
public class Results {
    Map<String, Map<String, Long>> results;
    public void queryResults(String country, String year) {
        try {
            results = new ObjectMapper().readValue(new File(year+".json"), Map.class);
            Map<String, Long> countryResults = results.getOrDefault(country, new HashMap<>());
            //If the country have no votes
            if(countryResults.isEmpty()) {
                System.out.println("No results for the country found");
                return;
            }
            //Output the results for the country for the given year.
            System.out.println(String.format("%s %s voting results:", country, year));
            countryResults.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10).forEach(stringLongEntry ->
                    System.out.println(stringLongEntry.getValue()+ " points goes to "+ stringLongEntry.getKey()));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
