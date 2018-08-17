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
            votesProcessor.processVotes(input, year);
            return "uploading complete!";
        } else if (option.equals("results")){
            results.queryResults(input, year);
            return "--End--";
        }
        return "Invalid options";
    }


}
