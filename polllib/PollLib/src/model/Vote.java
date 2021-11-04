package model;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Vote {
    private String pin; //6-digit randomly generated PIN (voterId)
    private Choice choice;
    private LocalDateTime timestamp;

    public Vote(String pin, Choice choice, LocalDateTime timestamp) {
        this.pin = pin;
        this.choice = choice;
        this.timestamp = timestamp;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
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
        return Objects.equals(pin, vote.pin) &&
                Objects.equals(choice, vote.choice) &&
                Objects.equals(timestamp, vote.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pin, choice, timestamp);
    }

    @Override
    public String toString() {
        return "Vote {" +
                "\nvoterId = '" + pin + '\'' +
                ", \nchoice = " + choice +
                ", \ntimestamp = " + timestamp +
                '}';
    }
}
