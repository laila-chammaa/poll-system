package data;

import model.Poll;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

public class PollRepository {

    private EntityManager entityManager;

    public PollRepository() {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");

        this.entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
    }

    public Optional<Poll> findById(String id) {
        Poll poll = entityManager.find(Poll.class, id);
        return poll != null ? Optional.of(poll) : Optional.empty();
    }

    public List<Poll> findAll() {
        return entityManager.createQuery("Select t from Poll t").getResultList();
    }

    public void update(Poll poll) {
        entityManager.getTransaction().begin();
        Poll pollToUpdate = findById(poll.getId()).orElseThrow(
                () -> new IllegalStateException(String.format("No poll found for the ID: %d.", poll.getId())));
        pollToUpdate.setStatus(poll.getStatus());
        pollToUpdate.setName(poll.getName());
        pollToUpdate.setChoices(poll.getChoices());
        pollToUpdate.setQuestion(poll.getQuestion());
        entityManager.merge(pollToUpdate);
        entityManager.getTransaction().commit();
    }

    public boolean save(Poll poll) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(poll);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace(); //TODO: better handling
        }
        return false;
    }

    public List<Poll> findByCreator(String creator) {
        return entityManager.createQuery("SELECT b FROM Poll b WHERE b.createdBy = :createdBy", Poll.class)
                .setParameter("createdBy", creator).getResultList();
    }

    public boolean delete(Poll poll) {
        try {
            entityManager.getTransaction().begin();
            entityManager.detach(poll);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace(); //TODO: better handling
        }
        return false;
    }
}
