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

    // privat konstruktør for at undgå instansiering udefra
    private UserDAO() {}

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    // Metode til at injecte en test-EMF
    public void setEmf(EntityManagerFactory emf) {
        UserDAO.emf = emf;
    }

    public User getVerifiedUser(String username, String password) {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, username);
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public User createUser(String username, String password, String role) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, hashedPassword, role);
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

