package com.eurovision.votingsystem.upload;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("queueProcessor")
/**
 * Processes the votes using a blocking queue. A Single producer reads from the file.
 * Variable consumers read from the queue to process partial results.
 * Results are merged and written to a year.json file.
 */
public class VotesQueueProcessor implements VotesProcessor{

    public static final int CONSUMER_COUNT = 5;
    private final BlockingQueue<String> linesReadQueue = new ArrayBlockingQueue<>(30);

    @Override
    public boolean processVotes(String fileName, String year) {

        //A single producer for reading the file and writing to blocking queue
        ExecutorService producerPool = Executors.newFixedThreadPool(1);
        producerPool.submit(new Producer(fileName, linesReadQueue, CONSUMER_COUNT));

        // N Consumers to read from the blocking queue and process each line
        ExecutorService consumerPool = Executors.newFixedThreadPool(CONSUMER_COUNT);
        List<Future<Map<String, Map<String, Long>>>> futureResults = new ArrayList<>();
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            Future<Map<String, Map<String, Long>>> future = consumerPool.submit(new Consumer(linesReadQueue));
            futureResults.add(future);
        }

        producerPool.shutdown();
        consumerPool.shutdown();

        List<Map<String, Map<String, Long>>> results = new ArrayList<>();

        // Wait for the results from all the consumers
        try {
            for (Future<Map<String, Map<String, Long>>> futureResult: futureResults
                    ) {
                results.add(futureResult.get());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        // Merge the results from the consumers
        Map<String, Map<String,Long>> finalResults = results.stream().parallel()
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b)-> Stream.of(a, b).parallel().map(Map::entrySet).flatMap(Collection::stream)
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,Long::sum))));
        // Write the result to year.json file
        this.writeResult(year,finalResults);
        return true;
    }


}