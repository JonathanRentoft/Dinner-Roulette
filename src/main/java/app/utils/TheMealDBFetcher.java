package app.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TheMealDBFetcher {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

  // Searches for recipes containing a specific main ingredient.
    public static String searchByIngredient(String ingredient) throws IOException, InterruptedException {
        String url = BASE_URL + "filter.php?i=" + ingredient;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

     //Looks up the full details for a specific meal by its ID. returns a JSON string with the full meal details.
    public static String lookupById(String id) throws IOException, InterruptedException {
        String url = BASE_URL + "lookup.php?i=" + id;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String fetchRecipesByIngredient(String firstIngredient) throws IOException, InterruptedException {
        String url = BASE_URL + "recipes.php?i=" + firstIngredient;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public String fetchRecipeDetailsById(int recipeId) throws IOException, InterruptedException {
        String url = BASE_URL + "details.php?i=" + recipeId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response =client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
