package com.eurovision.votingsystem.upload;

/**
 * Class representing each vote from the file
 */
public class Vote {
    private String country;
    private String votedFor;

    public Vote(){

    }

    public Vote(String country, String votedFor) {
        this.country = country;
        this.votedFor = votedFor;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVotedFor() {
        return votedFor;
    }

    public void setVotedFor(String votedFor) {
        this.votedFor = votedFor;
    }
}
