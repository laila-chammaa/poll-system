import model.Choice;
import model.Poll;
import model.PollStatus;
import model.Vote;

import javax.xml.soap.Text;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PollManager {

    private static Poll currentPoll = null;

    public static Poll createPoll(String name, Text question, ArrayList<Choice> choices)
            throws PollException.IllegalPollOperation, PollException.TooFewChoices, PollException.DuplicateChoices {

        validateChoices(choices);

        if (currentPoll == null) {
            Poll newPoll = new Poll(name, question, choices, new ArrayList<>());
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
            currentPoll.setChoices(choices);
            //TODO: how do you just update one of these?
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is already released. Cannot update an already released poll", currentPoll.getName()));
        }
    }

    public static void clearPoll() throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.RUNNING) {
            currentPoll.getVotes().clear();
        } else if (currentPoll.getStatus() == PollStatus.RELEASED) {
            currentPoll.getVotes().clear();
            currentPoll.setStatus(PollStatus.CREATED);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not running or released. No results to clear", currentPoll.getName()));
        }
    }

    public void closePoll() throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.RELEASED) {
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
        if (!currentPoll.getChoices().contains(choice)) {
            throw new PollException.ChoiceNotFound(String.format("'%s' is not a valid choice in the poll '%s'",
                    choice.getText(), currentPoll.getName()));
        }
        if (currentPoll.getStatus() == PollStatus.RUNNING) {
            Optional<Vote> previousVote = currentPoll.getVotes().stream()
                    .filter(v -> v.getVoterId().equals(participant)).findFirst();
            if (previousVote.isPresent()) {
                previousVote.get().setChoice(choice);
                previousVote.get().setTimestamp(ZonedDateTime.now());
            } else {
                Vote vote = new Vote(participant, choice, ZonedDateTime.now());
                currentPoll.getVotes().add(vote);
            }
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not running. Votes are allowed while the poll is running", currentPoll.getName()));
        }
    }

    public static Hashtable<Choice, Integer> getPollResults() {
        Hashtable<Choice, Integer> results = new Hashtable<>();
        currentPoll.getVotes().forEach(v -> {
            int count = results.get(v.getChoice());
            results.put(v.getChoice(), count + 1);
        });
        return results;
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

    public static Poll getCurrentPoll() {
        return currentPoll;
    }
}
