package data;

import model.Poll;
import model.Vote;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class PollRepository {
    Connection connection;

    @PersistenceContext
    private EntityManager entityManager;

    public PollRepository() {
        {
            try {
                Properties props = new Properties();
                try(InputStream is = this.getClass().getResourceAsStream("/createDb/createDb.properties")) {
                    props.load(is);
                }
                String DB_URL = (String) props.get("DB_URL");
                String DB_DRIVER = (String) props.get("DB_DRIVER");
                String DB_USER = (String) props.get("DB_USER");
                String DB_PASS = (String) props.get("DB_PASS");
                String DB_NAME = (String) props.get("DB_NAME");

                Class.forName(DB_DRIVER);
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + DB_NAME);
            } catch (
                    SQLException | ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }

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
        if (!entityManager.getTransaction().isActive())
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
            if (!entityManager.getTransaction().isActive())
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
            if (!entityManager.getTransaction().isActive())
                entityManager.getTransaction().begin();
            Poll pollToDelete = entityManager.find(Poll.class, poll.getId());
            entityManager.remove(pollToDelete);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace(); //TODO: better handling
        }
        return false;
    }

    public boolean save(Vote vote) {
        try {
            if (!entityManager.getTransaction().isActive())
                entityManager.getTransaction().begin();
            entityManager.persist(vote);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace(); //TODO: better handling
        }
        return false;
    }

    public Optional<Vote> findByPIN(String pin) {
        Vote vote = entityManager.find(Vote.class, pin);
        return vote != null ? Optional.of(vote) : Optional.empty();
    }

    public void update(Vote vote) {
        if (!entityManager.getTransaction().isActive())
            entityManager.getTransaction().begin();
        Vote voteToUpdate = findByPIN(vote.getPin()).orElseThrow(
                () -> new IllegalStateException(String.format("No vote found for the PIN: %s.", vote.getPin())));
        voteToUpdate.setChoice(vote.getChoice());
        entityManager.merge(voteToUpdate);
        entityManager.getTransaction().commit();
    }
}
