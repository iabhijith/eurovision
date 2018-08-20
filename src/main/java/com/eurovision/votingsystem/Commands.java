package com.eurovision.votingsystem;

import com.eurovision.votingsystem.results.Results;
import com.eurovision.votingsystem.upload.VotesProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;


@ShellComponent
public class Commands {

    @Autowired
    @Qualifier("queueProcessor")
    VotesProcessor votesProcessor;

    @Autowired
    Results results;

    /**
     *
     * @param option option to run load/results
     * @param input filename in case of load and country name in case of results
     * @param year year of eurovision contest
     * @return
     * @throws IOException
     */
    @ShellMethod(value = "Upload votes for counting", key = "eurovision")
    public String euroVisionCommands(String option, String input, String year) {
        if(option.equals("load")) {
            if(votesProcessor.processVotes(input, year)){
                return "Processing complete!";
            } else {
                return "Failed to process!";
            }

        } else if (option.equals("results")){
            String resultHeader = String.format("%s %s voting results:\n", input, year);
            String result = results.queryResults(input, year);
            return resultHeader + result;
        }
        return "Invalid options";
    }


}
