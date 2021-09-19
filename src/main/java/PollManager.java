import model.Choice;
import model.Poll;
import model.PollStatus;

import javax.xml.soap.Text;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.stream.Collectors;

public class PollManager {

    public static Poll currentPoll;

    public static Poll createPoll(String name, Text question, ArrayList<Choice> choices)
            throws PollException.IllegalPollOperation, PollException.TooFewChoices, PollException.DuplicateChoices {

        validateChoices(choices);

        Hashtable<Choice, Integer> choicesWithCount = new Hashtable<>();
        for (Choice choice : choices) {
            choicesWithCount.put(choice, 0);
        }
        if (currentPoll == null) {
            Poll newPoll = new Poll(name, question, choicesWithCount);
            currentPoll = newPoll;
            return newPoll;
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is currently in the system. The system allows one poll at a time", currentPoll.getName()));
        }
    }

    private static void validateChoices(ArrayList<Choice> choices)
            throws PollException.DuplicateChoices, PollException.TooFewChoices {
        if (choices.size() < 2) {
            throw new PollException.TooFewChoices("A poll must have at least 2 choices");
        }

        ArrayList<String> choicesNames = choices.stream()
                .map(Choice::getText)
                .collect(Collectors.toCollection(ArrayList::new));

        Set<String> set = new HashSet<>(choicesNames);

        if (set.size() < choicesNames.size()) {
            throw new PollException.DuplicateChoices("There are duplicate choices in the poll");
        }
    }

    public static void updatePoll(String name, Text question, ArrayList<Choice> choices)
            throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.CREATED || currentPoll.getStatus() == PollStatus.RUNNING) {
            currentPoll.setStatus(PollStatus.CREATED);
            currentPoll.setName(name);
            currentPoll.setQuestion(question);

            // updating a poll will clear its results
            Hashtable<Choice, Integer> choicesWithCount = new Hashtable<>();
            for (Choice choice : choices) {
                choicesWithCount.put(choice, 0);
            }
            currentPoll.setChoices(choicesWithCount);
            //TODO: how do u just update one of these?
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is already released. Cannot update an already released poll", currentPoll.getName()));
        }
    }

    public static void clearPoll() throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.RUNNING) {
            currentPoll.getChoices().clear();
        } else if (currentPoll.getStatus() == PollStatus.RELEASED) {
            currentPoll.getChoices().clear();
            currentPoll.setStatus(PollStatus.CREATED);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not running or released. No results to clear", currentPoll.getName()));
        }
    }

    public void closePoll() throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.RELEASED) {
            //TODO: how to close a poll?
            currentPoll = null;
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not released. Cannot close an unreleased poll", currentPoll.getName()));
        }
    }

    public static void runPoll() throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.CREATED) {
            currentPoll.setStatus(PollStatus.RUNNING);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is already running or released", currentPoll.getName()));
        }
    }

    public static void releasePoll() throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.RUNNING) {
            currentPoll.setStatus(PollStatus.RELEASED);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not running. Poll must be running to be released.", currentPoll.getName()));
        }
    }

    public static void unreleasePoll() throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.RELEASED) {
            currentPoll.setStatus(PollStatus.RUNNING);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not released", currentPoll.getName()));
        }
    }

    public static void vote(String participant, Choice choice)
            throws PollException.IllegalPollOperation, PollException.ChoiceNotFound {
        if (!currentPoll.getChoices().containsKey(choice)) {
            throw new PollException.ChoiceNotFound(String.format("'%s' is not a valid choice in the poll '%s'",
                    choice.getText(), currentPoll.getName()));
        }
        //TODO no clue about participants
        if (currentPoll.getStatus() == PollStatus.RUNNING) {
            int count = currentPoll.getChoices().get(choice);
            currentPoll.getChoices().put(choice, count + 1);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not running. Votes are allowed while the poll is running", currentPoll.getName()));
        }
    }

    public static Hashtable<Choice, Integer> getPollResults() {
        return currentPoll.getChoices();
    }

    public static void downloadPollDetails(PrintWriter output, String filename) throws FileNotFoundException,
            PollException.IllegalPollOperation {
        //TODO: what to do with filename? check that the filename has the current poll's name?
        if (currentPoll.getStatus() == PollStatus.RELEASED) {
            output = new PrintWriter(filename); //TODO: is this right? add try catch?
            output.write(currentPoll.toString());
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not released. Cannot download details of an unreleased poll", currentPoll.getName()));
        }
    }
}
