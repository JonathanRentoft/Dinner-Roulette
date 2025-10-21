package app.controller;

import app.entities.User;
import io.javalin.http.Handler;
// TODO: Import your Token/JWT utility class here

public class SecurityController {

    public static Handler authenticate() {
        // TODO: Replace with your actual JWT validation logic
        return ctx -> {
            String token = ctx.header("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                ctx.status(401).json("No token provided");
                return;
            }
            String tokenValue = token.substring(7);

            // Dummy validation - replace this!
            if (tokenValue.equals("VALID_DUMMY_TOKEN")) {
                // In a real implementation, you would decode the token to get user info
                User dummyUser = new User();
                dummyUser.setUsername("testuser");
                dummyUser.setRole("USER");
                ctx.attribute("user", dummyUser); // Make user object available for next handlers
            } else {
                ctx.status(401).json("Invalid token");
            }
        };
    }
}
