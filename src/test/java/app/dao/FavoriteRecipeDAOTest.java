package app.dao;

import app.config.HibernateConfig;
import app.entities.FavoriteRecipe;
import app.entities.User;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class FavoriteRecipeDAOTest {

    private static EntityManagerFactory emf;
    private static UserDAO userDAO;
    private static FavoriteRecipeDAO favoriteRecipeDAO;

    @BeforeAll
    static void setUpAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        userDAO = UserDAO.getInstance();
        favoriteRecipeDAO = FavoriteRecipeDAO.getInstance();
        userDAO.setEmf(emf);
        favoriteRecipeDAO.setEmf(emf); // Vigtigt: Sæt også EMF for denne DAO
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void createAndGetAllForUserAndDelete() {
        // Opret en testbruger
        User testUser = userDAO.createUser("favoriteTestUser", "pass", "user");

        // Opret en favorit
        FavoriteRecipe createdFavorite = favoriteRecipeDAO.create("52772", "Spaghetti Bolognese", testUser);
        assertNotNull(createdFavorite);
        assertThat(createdFavorite.getId(), is(1)); // Antager at det er den første i test-databasen

        // Hent alle favoritter for brugeren
        List<FavoriteRecipe> favorites = favoriteRecipeDAO.getAllForUser(testUser);
        assertThat(favorites, hasSize(1));
        assertEquals("Spaghetti Bolognese", favorites.get(0).getRecipeName());

        // Slet favoritten
        favoriteRecipeDAO.delete(createdFavorite.getId());

        // Hent alle favoritter igen for at bekræfte sletning
        List<FavoriteRecipe> emptyFavorites = favoriteRecipeDAO.getAllForUser(testUser);
        assertThat(emptyFavorites, hasSize(0));
    }
}
