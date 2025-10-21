package app.config;

import app.dao.UserDAO;
import app.entities.Ingredient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Populator {
    public static void populateDatabase() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Opret præ-definerede ingredienser
            String[] ingredients = {"chicken", "rice", "tomato", "pasta", "cheese", "beef", "onion", "garlic", "salt", "pepper"};
            for (String name : ingredients) {
                // Tjek om ingrediensen allerede eksisterer for at undgå dubletter ved genstart
                if (em.createQuery("SELECT i FROM Ingredient i WHERE i.name = :name", Ingredient.class)
                        .setParameter("name", name)
                        .getResultList().isEmpty()) {
                    em.persist(new Ingredient(name));
                }
            }

            // Opret en admin bruger hvis den ikke findes
            UserDAO userDAO = UserDAO.getInstance();
            if (userDAO.findByUsername("admin") == null) {
                userDAO.createUser("admin", "admin", "ADMIN");
            }


            em.getTransaction().commit();
        }
    }
}

