package data;

import model.Poll;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class PollRepository {
    String DB_USER = "root";
    String DB_NAME = "polldb";
    String DB_PASS = "Autodesk2020";

    Connection connection;

    private EntityManager entityManager;

    public PollRepository() {
        {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", DB_USER, DB_PASS);
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + DB_NAME);
            } catch (
                    SQLException | ClassNotFoundException e) {
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
        if(!entityManager.getTransaction().isActive())
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
            if(!entityManager.getTransaction().isActive())
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
            if(!entityManager.getTransaction().isActive())
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
