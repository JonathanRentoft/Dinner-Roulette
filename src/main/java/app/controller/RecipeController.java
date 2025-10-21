package app.controller;

import io.javalin.http.Handler;

public class RecipeController {

    public static Handler findRecipesBasedOnIngredients() {
        return ctx -> {
            // TODO: 1. Get user from ctx.attribute("user")
            // TODO: 2. Get user's ingredients from DB
            // TODO: 3. Call TheMealDB API with one of the ingredients
            // TODO: 4. Return the result from TheMealDB as JSON
            ctx.json("{\"message\": \"Finding recipes...\"}");
        };
    }

    public static Handler getRecipeDetailsById() {
        return ctx -> {
            // TODO: 1. Get recipe ID from ctx.pathParam("id")
            // TODO: 2. Call TheMealDB's lookup.php endpoint with the ID
            // TODO: 3. Return the full recipe details as JSON
            ctx.json("{\"message\": \"Getting recipe details\"}");
        };
    }

    // You will also need controllers for Favorite Recipes (get, add, delete)
}
