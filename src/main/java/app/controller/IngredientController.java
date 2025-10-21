package app.controller;

import app.dao.IngredientDAO;
import app.dto.IngredientDTO;
import app.entities.Ingredient;
import app.entities.User;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

import java.util.Set;
import java.util.stream.Collectors;

public class IngredientController {

    private static final IngredientDAO ingredientDAO = IngredientDAO.getInstance();

    public static Handler getAllForUser() {
        return ctx -> {
            User user = ctx.attribute("user");
            if (user == null) {
                ctx.status(HttpStatus.UNAUTHORIZED);
                return;
            }

            Set<Ingredient> ingredients = user.getIngredients();
            // Convert the Set of entities to a List of DTOs
            Set<IngredientDTO> ingredientDTOS = ingredients.stream()
                    .map(ingredient -> new IngredientDTO(ingredient.getId(), ingredient.getName()))
                    .collect(Collectors.toSet());

            ctx.json(ingredientDTOS);
        };
    }

    public static Handler addForUser() {
        return ctx -> {
            User user = ctx.attribute("user");
            IngredientDTO ingredientInput = ctx.bodyAsClass(IngredientDTO.class); // Expects {"name": "..."}

            Ingredient ingredient = ingredientDAO.findByName(ingredientInput.getName());

            if (ingredient == null) {
                ctx.status(HttpStatus.NOT_FOUND).json("Ingredient not found in the predefined list.");
                return;
            }

            ingredientDAO.addIngredientToUser(user, ingredient);
            ctx.status(HttpStatus.NO_CONTENT); // Use 204 No Content for successful operations that don't return a body
        };
    }

    public static Handler removeFromUser() {
        return ctx -> {
            User user = ctx.attribute("user");
            int ingredientId = Integer.parseInt(ctx.pathParam("id"));

            Ingredient ingredient = ingredientDAO.findById(ingredientId);
            if(ingredient == null){
                ctx.status(HttpStatus.NOT_FOUND).json("Ingredient not found");
                return;
            }

            ingredientDAO.removeIngredientFromUser(user, ingredient);
            ctx.status(HttpStatus.NO_CONTENT);
        };
    }
}

