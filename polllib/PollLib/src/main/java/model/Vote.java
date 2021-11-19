package model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="vote")
public class Vote {
    @Id
    private String pin; //6-digit randomly generated PIN (voterId)

    @OneToOne
    @JoinColumn(name = "choice", nullable = false)
    private Choice choice;

    private String timestamp;

    public Vote(String pin, Choice choice, String timestamp) {
        this.pin = pin;
        this.choice = choice;
        this.timestamp = timestamp;
    }

    public Vote() {
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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
