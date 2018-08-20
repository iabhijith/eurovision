package com.eurovision.votingsystem.upload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

public class Producer implements Runnable {
    private String fileName;
    private final BlockingQueue<String> linesReadQueue;
    private final int consumerCount;

    /**
     *
     * @param fileName The location of the file to process votes
     * @param linesReadQueue The BlockingQueue into which the producer puts each line
     * @param consumerCount The number of  consumers
     */
    public Producer(String fileName, BlockingQueue<String> linesReadQueue, int consumerCount) {
        this.fileName = fileName;
        this.linesReadQueue = linesReadQueue;
        this.consumerCount = consumerCount;
    }

    /**
     *  The Producer reads from file stream one line at a time and puts into the blocking queue.
     *  When it completes reading the file, it puts poison pills for each consumer
     */
    @Override
    public void run() {
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            for (String line : (Iterable<String>) lines::iterator) {
                linesReadQueue.put(line);
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        try {
            //For each consumer, put a poison pill
            for (int i = 0; i < this.consumerCount; i++) {
                linesReadQueue.put("EOF");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
