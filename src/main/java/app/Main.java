package app;

import app.config.Populator;
import app.routes.Routes;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        // Initialize Javalin app
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.router.contextPath = "/api"; // base path for all routes
        });

        // Setup all routes
        app.routes(Routes.getRoutes());

        // Enable built-in Javalin route overview (as required by the assignment)
        // This makes it available at http://localhost:7070/api/routes
        app.get("/routes", ctx -> ctx.json(app.javalinServlet().getMatcher().getRoutes()));

        // Run DB Populator
        Populator.populateDatabase();

        // Start the server
        app.start(7070);
    }
}

