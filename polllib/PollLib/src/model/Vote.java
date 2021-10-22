package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Vote {
    private String voterId; //sessionId
    private model.Choice choice;
    private LocalDateTime timestamp;

    public Vote(String voterId, model.Choice choice, LocalDateTime timestamp) {
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

    public model.Choice getChoice() {
        return choice;
    }

    public void setChoice(model.Choice choice) {
        this.choice = choice;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
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
        return "model.Vote {" +
                "\nvoterId = '" + voterId + '\'' +
                ", \nchoice = " + choice +
                ", \ntimestamp = " + timestamp +
                '}';
    }
}
