import model.Choice;
import model.Poll;
import model.PollStatus;
import model.Vote;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PollManager {

    public static Poll createPoll(String name, String question, ArrayList<Choice> choices)
            throws PollException.TooFewChoices, PollException.DuplicateChoices {

        validateChoices(choices);
        //TODO: validate user?
        Poll newPoll = new Poll(name, question, choices, new ArrayList<>());
        newPoll.setStatus(PollStatus.CREATED);
        //TODO: add to db?
        return newPoll;
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

    public static void updatePoll(String pollId, String name, String question, ArrayList<Choice> choices)
            throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        //clearing poll results
        if (poll.getStatus() == PollStatus.RUNNING) {
            clearPoll(pollId);
        }
        if (poll.getStatus() == PollStatus.CREATED || poll.getStatus() == PollStatus.RUNNING) {
            poll.setStatus(PollStatus.CREATED);
            poll.setName(name);
            poll.setQuestion(question);
            poll.setChoices(choices);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is already released. Cannot update an already released poll", poll.getName()));
        }
    }

    public static Poll accessPoll(String pollId) {
        //TODO: get poll from db
        //TODO: throw exception if not found
        return null;
    }

    public static void clearPoll(String pollId) throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.RUNNING) {
            poll.getVotes().clear();
        } else if (poll.getStatus() == PollStatus.RELEASED) {
            poll.getVotes().clear();
            poll.setStatus(PollStatus.CREATED);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not running or released. No results to clear", poll.getName()));
        }
    }

    public static void closePoll(String pollId) throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.RELEASED) {
            poll = null;
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not released. Cannot close an unreleased poll", poll.getName()));
        }
    }

    public static void runPoll(String pollId) throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.CREATED) {
            poll.setStatus(PollStatus.RUNNING);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is already running or released", poll.getName()));
        }
    }

    public static void releasePoll(String pollId) throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.RUNNING) {
            poll.setStatus(PollStatus.RELEASED);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not running. Poll must be running to be released.", poll.getName()));
        }
    }

    public static void unreleasePoll(String pollId) throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.RELEASED) {
            poll.setStatus(PollStatus.RUNNING);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not released", poll.getName()));
        }
    }

    //TODO: change? pin should be checked before this?
    public static void vote(String pin, String pollId, Choice choice)
            throws PollException.IllegalPollOperation, PollException.ChoiceNotFound {
        Poll poll = accessPoll(pollId);
        if (!poll.getChoices().contains(choice)) {
            throw new PollException.ChoiceNotFound(String.format("'%s' is not a valid choice in the poll '%s'",
                    choice.getText(), poll.getName()));
        }
        if (poll.getStatus() == PollStatus.RUNNING) {
            String finalPin = pin;
            Optional<Vote> previousVote = poll.getVotes().stream()
                    .filter(v -> v.getPin().equals(finalPin)).findFirst();
            if (previousVote.isPresent()) {
                previousVote.get().setChoice(choice);
                previousVote.get().setTimestamp(LocalDateTime.now());
            } else {
                //TODO: 6-digit randomly generated PIN# returned to the user
                pin = null;
                Vote vote = new Vote(pin, choice, LocalDateTime.now());
                poll.getVotes().add(vote);
            }
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not running. Votes are allowed while the poll is running", poll.getName()));
        }
    }

    public static Hashtable<String, Integer> getPollResults(String pollId) throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.RELEASED) {
            Hashtable<String, Integer> results = new Hashtable<>();
            poll.getVotes().forEach(v -> {
                Integer count = results.get(v.getChoice().getText());
                if (count == null) {
                    count = 0;
                }
                results.put(v.getChoice().getText(), count + 1);
            });
            return results;
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not released. Not allowed to return poll results.", poll.getName()));
        }
    }

    public static void downloadPollDetails(String pollId, PrintWriter output, String filename) throws
            PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.RELEASED) {
            output.write(poll.toString());
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not released. Cannot download details of an unreleased poll", poll.getName()));
        }
    }
}
