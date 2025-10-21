package app.dao;

import app.config.HibernateConfig;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {

    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static UserDAO instance;

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    //SOC - her slår vi op i databasen efter en verified user.
    public User getVerifiedUser(String username, String password) {
        try (EntityManager em = emf.createEntityManager()) {
            //Find user by username
            User user = findByUsername(username);

            //If user exists, check if the provided password matches the hashed password
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return user; // Passwords match
            }
        } catch (Exception e) {
            // Handle exceptions
        }
        return null; // User not found or password incorrect
    }

    public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);

        // Hashing the password before saving it to the database
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(hashedPassword);
        user.setRole("USER"); // Alle nye brugere får rollen USER

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
        return user;
    }

    public User findByUsername(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}

