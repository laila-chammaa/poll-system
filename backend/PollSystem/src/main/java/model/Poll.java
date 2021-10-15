package model;

import java.util.ArrayList;
import java.util.Objects;

public class Poll {
    private String name;
    private String question;
    private PollStatus status;
    private ArrayList<Choice> choices;
    private ArrayList<Vote> votes;

    public Poll(String name, String question, ArrayList<Choice> choices, ArrayList<Vote> votes) {
        this.name = name;
        this.question = question;
        this.choices = choices;
        this.votes = votes;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public PollStatus getStatus() {
        return status;
    }

    public void setStatus(PollStatus status) {
        this.status = status;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<Choice> choices) {
        this.choices = choices;
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, question, status, choices, votes);
    }

    @Override
    public String toString() {
        return "Poll{" +
                "name='" + name + '\'' +
                ", question=" + question +
                ", status=" + status +
                ", choices=" + choices +
                ", votes=" + votes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poll poll = (Poll) o;
        return Objects.equals(name, poll.name) &&
                Objects.equals(question, poll.question) &&
                status == poll.status &&
                Objects.equals(choices, poll.choices) &&
                Objects.equals(votes, poll.votes);
    }
}
