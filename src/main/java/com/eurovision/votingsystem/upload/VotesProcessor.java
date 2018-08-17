package com.eurovision.votingsystem.upload;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public interface VotesProcessor {


    /**
     *
     * @param fileName The location of the file to be processed
     * @param year The year of eurovision contest
     * @return Returns true on successful processing.
     */
    boolean processVotes(String fileName, String year);

    /**
     *
     * @param year Write results in to year.json
     * @param result The result to be written to file
     */
    default void writeResult(String year, Map<String, Map<String, Long>> result){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(year+".json"),result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
