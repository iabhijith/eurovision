package com.eurovision.votingsystem.upload;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class Consumer implements Callable<Map<String, Map<String, Long>>> {

    private final BlockingQueue<String> linesReadQueue;
    private Map<String, Map<String, Long>> result = new HashMap<>();

    /**
     *
     * @param linesReadQueue The BlockingQueue from which each consumer reads.
     */
    public Consumer(BlockingQueue<String> linesReadQueue) {
        this.linesReadQueue = linesReadQueue;
    }

    /**
     *  Each consumer reads from the blocking queue until it gets a poison pill
     * @return Return the partial results for the votes processed
     */
    @Override
    public Map<String, Map<String, Long>> call() {
        ObjectMapper objectMapper = new ObjectMapper();
        Vote vote;
        Map<String, Long> votesByCountry;
        while (true) {
            try {
                String lineToProcess = linesReadQueue.take();
                //If the queue element is a poison pill thread returns the partial results
                if(lineToProcess.equals("EOF")){
                    return result;
                }
                vote = objectMapper.readValue(lineToProcess, Vote.class);
                votesByCountry = result.getOrDefault(vote.getCountry(), new HashMap<>());
                //Increase the count of votes for the votedCountry
                votesByCountry.put(vote.getVotedFor(), votesByCountry.getOrDefault(vote.getVotedFor(), 0L) + 1);
                result.put(vote.getCountry(), votesByCountry);

            } catch (InterruptedException | IOException ex) {
                ex.printStackTrace();
            }


        }

    }


}
