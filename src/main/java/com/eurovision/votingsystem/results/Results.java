package com.eurovision.votingsystem.results;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
/**
 * Reads from year.json file
 * Outputs the results for the country specified
 */
public class Results {
    Map<String, Map<String, Long>> results;
    public String queryResults(String country, String year) {
        try {
            results = new ObjectMapper().readValue(new File(year+".json"), Map.class);
            Map<String, Long> countryResults = results.getOrDefault(country, new HashMap<>());
            //If the country have no votes
            if(countryResults.isEmpty()) {
                return String.format("No results for %s found", country);
            }
            //Collect the results for the country for the given year
            return countryResults.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10)
                    .map(stringLongEntry -> stringLongEntry.getValue()+ " points goes to "+ stringLongEntry.getKey())
                    .collect(Collectors.joining("\n"));

        } catch (IOException e) {
            return "Couldn't fetch the results for the given query!";
        }

    }
}
