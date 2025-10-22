package app.service;

import app.dao.FavoriteRecipeDAO;
import app.entities.FavoriteRecipe;
import app.entities.User;
import app.exceptions.ApiException;
import app.utils.TheMealDBFetcher;

import java.io.IOException;
import java.util.List;

public class RecipeService {

    private final FavoriteRecipeDAO favoriteRecipeDAO = FavoriteRecipeDAO.getInstance();
    private final TheMealDBFetcher mealDBFetcher = new TheMealDBFetcher();

    public String findRecipesForUser(User user) throws ApiException, IOException, InterruptedException {
        if (user.getIngredients().isEmpty()) {
            throw new ApiException(400, "User has no ingredients. Add some to find recipes.");
        }
        //For now, we just use one ingredient.
        String firstIngredient = user.getIngredients().iterator().next().getName();
        return mealDBFetcher.fetchRecipesByIngredient(firstIngredient);
    }

    public String findRecipeDetailsById(int recipeId) throws ApiException, IOException, InterruptedException {
        return mealDBFetcher.fetchRecipeDetailsById(recipeId);
    }

    public List<FavoriteRecipe> getFavoritesForUser(User user) {
        return favoriteRecipeDAO.getAllForUser(user);
    }

    public FavoriteRecipe addFavorite(User user, String externalRecipeId, String recipeName) throws ApiException {
        if (externalRecipeId == null || recipeName == null) {
            throw new ApiException(400, "recipeId and recipeName are required.");
        }
        return favoriteRecipeDAO.create(externalRecipeId, recipeName, user);
    }

    public void removeFavorite(User user, int favoriteId) throws ApiException {
        FavoriteRecipe favorite = favoriteRecipeDAO.findById(favoriteId);
        if (favorite == null || !favorite.getUser().getId().equals(user.getId())) {
            throw new ApiException(404, "Favorite recipe not found or does not belong to user.");
        }
        favoriteRecipeDAO.delete(favoriteId);
    }
}
