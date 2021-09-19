package model;

import javax.xml.soap.Text;

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
}
