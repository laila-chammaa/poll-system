package model;

import javax.xml.soap.Text;
import java.util.Objects;

public class Choice {
    private String text;
    private Text description;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Text getDescription() {
        return description;
    }

    public void setDescription(Text description) {
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
        return "Choice{" +
                "text='" + text + '\'' +
                ", description=" + description +
                '}';
    }
}
