package app.routes;

import app.controller.AuthController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    public static EndpointGroup getRoutes() {
        return () -> {
            // Auth routes
            path("/auth", () -> {
                post("/login", AuthController.login());
                post("/register", AuthController.register());
            });

            // TODO: Add other controllers and endpoints here later
            // Example:
            // path("/me", () -> {
            //     before(SecurityController.authenticate()); // Secure all /me endpoints
            //     get("/ingredients", UserController.getIngredients());
            //     post("/ingredients", UserController.addIngredient());
            // });
        };
    }
}
