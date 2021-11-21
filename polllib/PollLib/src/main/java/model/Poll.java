package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.persistence.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.PrintWriter;
import java.io.StringWriter;
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

    public String toStringJSON() {
        //Initializing the JSON package
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        JSONArray list2 = new JSONArray();
        //Adding the choice in a JSON array
        list.add(choices);
        list2.add(votes);

        //Adding to a JSONobject
        obj.put("Poll", id);
        obj.put("Time created", timeCreated);
        obj.put("Name", name);
        obj.put("Question ", question);
        obj.put("Created by", createdBy);
        obj.put("Choices", list);
        obj.put("Votes", list2);

        String jsonFormat = obj.toString();

        return jsonFormat;
    }

    public String toStringXML() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String output = "";
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.newDocument();
            //Create root node
            Element root = doc.createElement("Poll");
            doc.appendChild(root);
            //Create poll
            Element poll = doc.createElement("Poll");
            root.appendChild(poll);
            //Create pollID
            Element pollID = doc.createElement("PollID");
            pollID.appendChild(doc.createTextNode(id));
            poll.appendChild(pollID);
            //Create time creation
            Element timeCreation = doc.createElement("TimeCreated");
            timeCreation.appendChild(doc.createTextNode(timeCreated));
            poll.appendChild(timeCreation);
            //Create name
            Element namePoll = doc.createElement("Name");
            namePoll.appendChild(doc.createTextNode(name));
            poll.appendChild(namePoll);
            //Create name
            Element questionPoll = doc.createElement("Question");
            questionPoll.appendChild(doc.createTextNode(question));
            poll.appendChild(questionPoll);
            //Create name
            Element statusPoll = doc.createElement("Status");
            statusPoll.appendChild(doc.createTextNode(status.toString()));
            poll.appendChild(statusPoll);
            //Create userCreation
            Element userCreation = doc.createElement("CreatedBy");
            userCreation.appendChild(doc.createTextNode(createdBy));
            poll.appendChild(userCreation);
            //Create choices
            Element choicePoll = doc.createElement("Choices");
            choicePoll.appendChild(doc.createTextNode(choices.toString()));
            poll.appendChild(choicePoll);
            //Create votes
            Element votesPoll = doc.createElement("Votes");
            votesPoll.appendChild(doc.createTextNode(votes.toString()));
            poll.appendChild(votesPoll);

            //Write temp to file
            DOMSource source = new DOMSource(doc);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(source, new StreamResult(writer));
            output = writer.getBuffer().toString();


        } catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }

        return output;
    }
}
