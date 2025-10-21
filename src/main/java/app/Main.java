package app;

import app.config.Populator;
import app.routes.Routes;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Main {
    public static void main(String[] args) {
        // Initialize Javalin app
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.routing.contextPath = "/api"; // base path for all routes
        });

        // Setup all routes
        app.routes(Routes.getRoutes());

        // Enable built-in Javalin route overview (as required by the assignment)
        // This makes it available at http://localhost:7070/api/routes
        app.get("/routes", ctx -> ctx.json(app.unsafeConfig().router.getPaths()));

        // Run DB Populator
        Populator.populateDatabase();

        // Start the server
        app.start(7070);
    }
}

