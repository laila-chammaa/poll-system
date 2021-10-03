package model;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Vote {
    private String voterId; //sessionId
    private Choice choice;
    private ZonedDateTime timestamp; //TODO separate into date, timestamp?

    public Vote(String voterId, Choice choice, ZonedDateTime timestamp) {
        this.voterId = voterId;
        this.choice = choice;
        this.timestamp = timestamp;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(voterId, vote.voterId) &&
                Objects.equals(choice, vote.choice) &&
                Objects.equals(timestamp, vote.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voterId, choice, timestamp);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "voterId='" + voterId + '\'' +
                ", choice=" + choice +
                ", timestamp=" + timestamp +
                '}';
    }
}
