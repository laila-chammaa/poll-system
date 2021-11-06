package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Choice {
    @Id
    private String id;
    private String text;
    private String description;

    public Choice(String text, String description) {
        this.text = text;
        this.description = description;
    }

    public Choice() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Choice choice = (Choice) o;
        return Objects.equals(text, choice.text) &&
                Objects.equals(description, choice.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, description);
    }

    @Override
    public String toString() {
        return "Choice {" +
                "\ntext = '" + text + '\'' +
                ", \ndescription = " + description +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
