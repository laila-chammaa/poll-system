package model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="poll")
public class Poll {
    @Id
    private String id;
    private String timeCreated;
    private String name;
    private String question;
    private PollStatus status;
    private String createdBy;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Choice> choices;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Vote> votes;

    public Poll(String id, String name, String question, List<Choice> choices, List<Vote> votes, String createdBy) {
        this.id = id;
        this.name = name;
        this.question = question;
        this.choices = choices;
        this.votes = votes;
        this.timeCreated = LocalDateTime.now().toString();
        this.createdBy = createdBy;
    }

    public Poll() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
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

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poll poll = (Poll) o;
        return Objects.equals(id, poll.id) &&
                Objects.equals(timeCreated, poll.timeCreated) &&
                Objects.equals(name, poll.name) &&
                Objects.equals(question, poll.question) &&
                status == poll.status &&
                Objects.equals(createdBy, poll.createdBy) &&
                Objects.equals(choices, poll.choices) &&
                Objects.equals(votes, poll.votes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeCreated, name, question, status, createdBy, choices, votes);
    }

    @Override
    public String toString() {
        return "Poll{" +
                "id='" + id + '\'' +
                ", timeCreated='" + timeCreated + '\'' +
                ", name='" + name + '\'' +
                ", question='" + question + '\'' +
                ", status=" + status +
                ", createdBy='" + createdBy + '\'' +
                ", choices=" + choices +
                ", votes=" + votes +
                '}';
    }
}
