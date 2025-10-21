package app.dao;

import app.config.HibernateConfig;
import app.entities.User;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private static EntityManagerFactory emf;
    private static UserDAO userDAO;

    @BeforeAll
    static void setUpAll() {
        // Brug test-databasen konfigureret i HibernateConfig
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        userDAO = UserDAO.getInstance();
        // Sørg for at DAO'en også bruger test-databasen
        userDAO.setEmf(emf);
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void createUserAndVerifyUser() {
        // Opret en ny bruger
        User createdUser = userDAO.createUser("testuser", "testpass", "user");
        assertNotNull(createdUser);
        assertThat(createdUser.getId(), notNullValue());

        // Verificer brugeren med korrekt password
        User verifiedUser = userDAO.getVerifiedUser("testuser", "testpass");
        assertNotNull(verifiedUser);
        assertEquals("testuser", verifiedUser.getUsername());

        // Test at verificering fejler med forkert password
        User nonVerifiedUser = userDAO.getVerifiedUser("testuser", "wrongpass");
        assertNull(nonVerifiedUser);
    }
}
