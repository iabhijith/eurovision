package com.eurovision.votingsystem.upload;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("streamProcessor")
/**
 * Processes the file and aggregates the results in a single thread using Streams API.
 */
public class VotesStreamProcessor implements VotesProcessor{

    @Override
    public boolean processVotes(String fileName, String year) {
        try (Stream<String> voteStream = Files.lines(Paths.get(fileName))) {
            Map<String, Map<String, Long>> results =
                    voteStream.map(line -> convert(line, Vote.class))
                            .collect(Collectors.groupingBy(Vote::getCountry,
                                    Collectors.groupingBy(Vote::getVotedFor, Collectors.counting())));
            this.writeResult(year, results);
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private  <T> T convert(String string, Class<T> pojo){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(string, pojo);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

}
