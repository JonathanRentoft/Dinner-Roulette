package app.dao;

import app.config.HibernateConfig;
import app.entities.Ingredient;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class IngredientDAO {

    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static IngredientDAO instance;

    public static IngredientDAO getInstance() {
        if (instance == null) {
            instance = new IngredientDAO();
        }
        return instance;
    }

    // Finder en ingrediens i databasen ud fra dens navn. Returnerer null hvis ingrediensen ikke findes i den prædefinerede liste.
    public Ingredient findByName(String name) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Ingredient> query = em.createQuery("SELECT i FROM Ingredient i WHERE i.name = :name", Ingredient.class);
            query.setParameter("name", name.toLowerCase());
            try {
                return query.getSingleResult();
            } catch (jakarta.persistence.NoResultException e) {
                // Ingrediensen findes ikke.
                return null;
            }
        }
    }

    public void addIngredientToUser(User user, Ingredient ingredient) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            // em.merge for at den nye EntityManager kender til user og ingrediens for at ungå detached entity.
            User managedUser = em.merge(user);
            Ingredient managedIngredient = em.merge(ingredient);

            managedUser.addIngredient(managedIngredient);
            em.getTransaction().commit();
        }
    }

    public void removeIngredientFromUser(User user, Ingredient ingredient) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User managedUser = em.merge(user);
            Ingredient managedIngredient = em.merge(ingredient);

            managedUser.removeIngredient(managedIngredient);
            em.getTransaction().commit();
        }
    }

    public Ingredient findById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Ingredient.class, id);
        }
    }
}

