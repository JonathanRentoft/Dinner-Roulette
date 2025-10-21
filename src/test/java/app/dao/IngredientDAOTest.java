package app.dao;

import app.config.HibernateConfig;
import app.config.Populator;
import app.entities.Ingredient;
import app.entities.User;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class IngredientDAOTest {
    private static EntityManagerFactory emf;
    private static UserDAO userDAO;
    private static IngredientDAO ingredientDAO;

    @BeforeAll
    static void setUpAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        userDAO = UserDAO.getInstance();
        ingredientDAO = IngredientDAO.getInstance();
        userDAO.setEmf(emf);
        ingredientDAO.setEmf(emf); // Vigtigt: Sæt også EMF for denne DAO

        // Kør populator for at have ingredienser at teste med
        Populator.populateDatabase();
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void findByName() {
        // Test at vi kan finde en eksisterende ingrediens
        Ingredient foundIngredient = ingredientDAO.findByName("chicken");
        assertNotNull(foundIngredient);
        assertEquals("chicken", foundIngredient.getName());

        // Test at den returnerer null for en ikke-eksisterende ingrediens
        Ingredient notFoundIngredient = ingredientDAO.findByName("unicorn meat");
        assertNull(notFoundIngredient);
    }

    @Test
    void addAndRemoveIngredientFromUser() {
        // Opret en testbruger
        User testUser = userDAO.createUser("ingredientTestUser", "pass", "user");

        // Find en ingrediens
        Ingredient chicken = ingredientDAO.findByName("chicken");
        Ingredient rice = ingredientDAO.findByName("rice");
        assertNotNull(chicken);
        assertNotNull(rice);

        // Tilføj ingredienser til bruger
        ingredientDAO.addIngredientToUser(testUser, chicken);
        ingredientDAO.addIngredientToUser(testUser, rice);

        // Hent brugeren igen for at se opdateringer
        User updatedUser = userDAO.findByUsername("ingredientTestUser");
        assertThat(updatedUser.getIngredients().size(), is(2));
        assertThat(updatedUser.getIngredients(), containsInAnyOrder(chicken, rice));

        // Fjern en ingrediens
        ingredientDAO.removeIngredientFromUser(updatedUser, chicken);

        // Hent brugeren igen for at se opdateringer
        User finalUser = userDAO.findByUsername("ingredientTestUser");
        assertThat(finalUser.getIngredients().size(), is(1));
        assertThat(finalUser.getIngredients(), containsInAnyOrder(rice));
    }
}
