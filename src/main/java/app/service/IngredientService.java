package app.service;

import app.dao.IngredientDAO;
import app.dao.UserDAO;
import app.entities.Ingredient;
import app.entities.User;
import app.exceptions.ApiException;

import java.util.Set;

public class IngredientService {
    private final IngredientDAO ingredientDAO = IngredientDAO.getInstance();

    public Set<Ingredient> getIngredientsForUser(User user) {
        return user.getIngredients();
    }

    public User addIngredientToUser(User user, String ingredientName) throws ApiException {
        Ingredient ingredient = ingredientDAO.findByName(ingredientName.toLowerCase());
        if (ingredient == null) {
            throw new ApiException(404, "Ingredient not found: " + ingredientName);
        }
        ingredientDAO.addIngredientToUser(user, ingredient);
        return user;
    }

    public User removeIngredientFromUser(User user, int ingredientId) throws ApiException {
        Ingredient ingredient = ingredientDAO.findById(ingredientId);
        if (ingredient == null || !user.getIngredients().contains(ingredient)) {
            throw new ApiException(404, "Ingredient not found in user's list");
        }
        ingredientDAO.removeIngredientFromUser(user, ingredient);
        user.removeIngredient(ingredient);
        return user;
    }
}
