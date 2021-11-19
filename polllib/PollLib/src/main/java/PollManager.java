import data.PollRepository;
import model.Choice;
import model.Poll;
import model.PollStatus;
import model.Vote;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PollManager {

    private PollRepository pollRepository = new PollRepository();

    public Poll createPoll(String name, String question, ArrayList<Choice> choices, String createdBy)
            throws PollException.TooFewChoices, PollException.DuplicateChoices {

        validateChoices(choices);
        String id = generateID();
        Poll newPoll = new Poll(id, name, question, choices, new ArrayList<>(), createdBy);
        newPoll.setStatus(PollStatus.CREATED);
        pollRepository.save(newPoll);
        return newPoll;
    }

    private String generateID() {
        //uppercase 10-char long random string, containing A-Z (excluding [ILOU]) and digits 0-9
        String SALTCHARS = "ABCDEFGHJKMNPQRSTVWXYZ0123456789";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    private void validateChoices(ArrayList<Choice> choices)
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

    public void updatePoll(String pollId, String name, String question, ArrayList<Choice> choices)
            throws PollException.IllegalPollOperation, PollException.TooFewChoices, PollException.DuplicateChoices {
        Poll poll = accessPoll(pollId);
        validateChoices(choices);
        //clearing poll results
        if (poll.getStatus() == PollStatus.RUNNING) {
            clearPoll(pollId);
        }
        if (poll.getStatus() == PollStatus.CREATED || poll.getStatus() == PollStatus.RUNNING) {
            poll.setStatus(PollStatus.CREATED);
            poll.setName(name);
            poll.setQuestion(question);
            poll.setChoices(choices);
            pollRepository.update(poll);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is already released. Cannot update an already released poll", poll.getName()));
        }
    }

    public Poll accessPoll(String pollId) {
        String upperCasePollId = pollId.toUpperCase(); //not case sensitive
        Poll newPoll = pollRepository.findById(pollId).orElseThrow(
                () -> new IllegalStateException(String.format("No poll found for the ID: %s.", upperCasePollId)));
        newPoll.setChoices(new ArrayList<>(newPoll.getChoices()));
        newPoll.setVotes(new ArrayList<>(newPoll.getVotes()));
        return newPoll;
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public List<Poll> getAllPollsByUser(String creator) {
        return pollRepository.findByCreator(creator);
    }

    public List<Poll> getAllArchivedPollsByUser(String creator) {
        return pollRepository.findByCreator(creator).stream()
                .filter(p -> p.getStatus() == PollStatus.ARCHIVED).collect(Collectors.toList());
    }

    public void clearPoll(String pollId) throws PollException.IllegalPollOperation {
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
        pollRepository.update(poll);
    }

    public void closePoll(String pollId) throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.RELEASED) {
            poll.setStatus(PollStatus.ARCHIVED);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not released. Cannot close an unreleased poll", poll.getName()));
        }
    }

    public void deletePoll(String pollId) throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getVotes().isEmpty()) {
            pollRepository.delete(poll);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s has been voted on. Cannot delete a poll with votes", poll.getName()));
        }
    }

    public void runPoll(String pollId) throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.CREATED) {
            poll.setStatus(PollStatus.RUNNING);
            pollRepository.update(poll);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is already running or released", poll.getName()));
        }
    }

    public void releasePoll(String pollId) throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.RUNNING) {
            poll.setStatus(PollStatus.RELEASED);
            pollRepository.update(poll);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not running. Poll must be running to be released.", poll.getName()));
        }
    }

    public void unreleasePoll(String pollId) throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.RELEASED) {
            poll.setStatus(PollStatus.RUNNING);
            pollRepository.update(poll);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not released", poll.getName()));
        }
    }

    public String vote(String pin, String pollId, Choice choice)
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
                previousVote.get().setTimestamp(LocalDateTime.now().toString());
                pollRepository.update(previousVote.get());
            } else {
                // pin not found in the system or null, generating new pin and voting
                pin = generatePIN();
                Vote vote = new Vote(pin, choice, LocalDateTime.now().toString());
                poll.getVotes().add(vote);
                pollRepository.save(vote);
            }
            pollRepository.update(poll);
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not running. Votes are allowed while the poll is running", poll.getName()));
        }
        return pin;
    }

    private String generatePIN() {
        // this will generate 6 digit random Number from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert it into a string
        return String.format("%06d", number);
    }

    public Hashtable<String, Integer> getPollResults(String pollId, String creator) throws PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.RELEASED ||
                (poll.getStatus() == PollStatus.ARCHIVED && creator.equals(poll.getCreatedBy()))) {
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

    public void downloadPollDetails(String pollId, PrintWriter output, String creator) throws
            PollException.IllegalPollOperation {
        Poll poll = accessPoll(pollId);
        if (poll.getStatus() == PollStatus.RELEASED ||
                (poll.getStatus() == PollStatus.ARCHIVED && creator.equals(poll.getCreatedBy()))) {
            output.write(poll.toString());
        } else {
            throw new PollException.IllegalPollOperation(String.format(
                    "Poll %s is not released. Cannot download details of an unreleased poll", poll.getName()));
        }
    }
}
