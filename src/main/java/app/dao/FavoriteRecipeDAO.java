package app.dao;

import app.config.HibernateConfig;
import app.entities.FavoriteRecipe;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class FavoriteRecipeDAO {

    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static FavoriteRecipeDAO instance;

    public static FavoriteRecipeDAO getInstance() {
        if (instance == null) {
            instance = new FavoriteRecipeDAO();
        }
        return instance;
    }

    public List<FavoriteRecipe> getAllForUser(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<FavoriteRecipe> query = em.createQuery("SELECT f FROM FavoriteRecipe f WHERE f.user.id = :userId", FavoriteRecipe.class);
            query.setParameter("userId", user.getId());
            return query.getResultList();
        }
    }

    public FavoriteRecipe create(String externalRecipeId, String recipeName, User user) {
        FavoriteRecipe newFavorite = new FavoriteRecipe();
        newFavorite.setExternalRecipeId(externalRecipeId);
        newFavorite.setRecipeName(recipeName);
        newFavorite.setUser(user);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            // Vi bruger merge på user for at sikre, at den er "managed" i den nuværende session
            User managedUser = em.merge(user);
            newFavorite.setUser(managedUser);

            em.persist(newFavorite);
            em.getTransaction().commit();
        }
        return newFavorite;
    }

    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            FavoriteRecipe favoriteToDelete = em.find(FavoriteRecipe.class, id);
            if (favoriteToDelete != null) {
                em.remove(favoriteToDelete);
            }
            em.getTransaction().commit();
        }
    }

    public FavoriteRecipe findById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(FavoriteRecipe.class, id);
        }
    }

    //Til test
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}

