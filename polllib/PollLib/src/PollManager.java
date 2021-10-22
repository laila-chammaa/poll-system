import model.Choice;
import model.Poll;
import model.PollStatus;
import model.Vote;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PollManager {

    private static Poll currentPoll = null;

    public static Poll createPoll(String name, String question, ArrayList<Choice> choices)
            throws PollException.IllegalPollOperation, PollException.TooFewChoices, PollException.DuplicateChoices {

        validateChoices(choices);

        if (currentPoll == null) {
            Poll newPoll = new Poll(name, question, choices, new ArrayList<>());
            newPoll.setStatus(PollStatus.CREATED);
            currentPoll = newPoll;
            return newPoll;
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "model.Poll %s is currently in the system. The system allows one poll at a time", currentPoll.getName()));
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

    public static void updatePoll(String name, String question, ArrayList<Choice> choices)
            throws PollException.IllegalPollOperation {
        //clearing poll results
        if (currentPoll.getStatus() == PollStatus.RUNNING) {
            clearPoll();
        }
        if (currentPoll.getStatus() == PollStatus.CREATED || currentPoll.getStatus() == PollStatus.RUNNING) {
            currentPoll.setStatus(PollStatus.CREATED);
            currentPoll.setName(name);
            currentPoll.setQuestion(question);
            currentPoll.setChoices(choices);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "model.Poll %s is already released. Cannot update an already released poll", currentPoll.getName()));
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
                    "model.Poll %s is not running or released. No results to clear", currentPoll.getName()));
        }
    }

    public static void closePoll() throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.RELEASED) {
            currentPoll = null;
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "model.Poll %s is not released. Cannot close an unreleased poll", currentPoll.getName()));
        }
    }

    public static void runPoll() throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.CREATED) {
            currentPoll.setStatus(PollStatus.RUNNING);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "model.Poll %s is already running or released", currentPoll.getName()));
        }
    }

    public static void releasePoll() throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.RUNNING) {
            currentPoll.setStatus(PollStatus.RELEASED);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "model.Poll %s is not running. model.Poll must be running to be released.", currentPoll.getName()));
        }
    }

    public static void unreleasePoll() throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.RELEASED) {
            currentPoll.setStatus(PollStatus.RUNNING);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "model.Poll %s is not released", currentPoll.getName()));
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
                previousVote.get().setTimestamp(LocalDateTime.now());
            } else {
                Vote vote = new Vote(participant, choice, LocalDateTime.now());
                currentPoll.getVotes().add(vote);
            }
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "model.Poll %s is not running. Votes are allowed while the poll is running", currentPoll.getName()));
        }
    }

    public static Hashtable<String, Integer> getPollResults() throws PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.RELEASED) {
            Hashtable<String, Integer> results = new Hashtable<>();
            currentPoll.getVotes().forEach(v -> {
                Integer count = results.get(v.getChoice().getText());
                if (count == null) {
                    count = 0;
                }
                results.put(v.getChoice().getText(), count + 1);
            });
            return results;
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "model.Poll %s is not released. Not allowed to return poll results.", currentPoll.getName()));
        }
    }

    public static void downloadPollDetails(PrintWriter output, String filename) throws
            PollException.IllegalPollOperation {
        if (currentPoll.getStatus() == PollStatus.RELEASED) {
            output.write(currentPoll.toString());
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "model.Poll %s is not released. Cannot download details of an unreleased poll", currentPoll.getName()));
        }
    }

    public static Poll getCurrentPoll() {
        return currentPoll;
    }
}
