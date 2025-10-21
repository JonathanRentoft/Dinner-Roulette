package app.controller;

import app.dao.UserDAO;
import app.dto.TokenDTO;
import app.dto.UserDTO;
import app.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class AuthController {

    private static UserDAO userDAO = UserDAO.getInstance();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Handler login() {
        return ctx -> {
            ObjectNode returnNode = objectMapper.createObjectNode();
            try {
                UserDTO user = ctx.bodyAsClass(UserDTO.class);
                System.out.println("USER: " + user);

                User verifiedUser = userDAO.getVerifiedUser(user.getUsername(), user.getPassword());
                if (verifiedUser == null) {
                    ctx.status(HttpStatus.UNAUTHORIZED).json(returnNode.put("msg", "Wrong username or password"));
                    return;
                }

                // TODO: Implement JWT token generation here
                String token = "DUMMY_TOKEN_REPLACE_ME"; // Placeholder for token
                ctx.status(HttpStatus.OK).json(new TokenDTO(token, user.getUsername()));

            } catch (Exception e) {
                ctx.status(HttpStatus.UNAUTHORIZED);
                ctx.json(returnNode.put("msg", e.getMessage()));
            }
        };
    }

    public static Handler register() {
        return ctx -> {
            ObjectNode returnNode = objectMapper.createObjectNode();
            try {
                UserDTO userInput = ctx.bodyAsClass(UserDTO.class);
                User createdUser = userDAO.createUser(userInput.getUsername(), userInput.getPassword());

                String token = "DUMMY_TOKEN_REPLACE_ME"; // Placeholder for token
                ctx.status(HttpStatus.CREATED).json(new TokenDTO(token, userInput.getUsername()));

            } catch (Exception e) {
                ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
                ctx.json(returnNode.put("msg", "User could not be created: " + e.getMessage()));
            }
        };
    }
}
