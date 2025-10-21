package app.service;

import app.dao.UserDAO;
import app.dto.UserDTO;
import app.entities.User;
import app.exceptions.ApiException;
import app.utils.TokenUtils;

public class AuthService {
    private final UserDAO userDAO = UserDAO.getInstance();
    private final TokenUtils tokenUtils = new TokenUtils();

    public String login(String username, String password) throws ApiException {
        User user = userDAO.getVerifiedUser(username, password);
        if (user == null) {
            throw new ApiException(401, "Invalid username or password");
        }
        return tokenUtils.createToken(new UserDTO(user));
    }

    public User register(String username, String password) throws ApiException {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new ApiException(400, "Username and password are required");
        }
        return userDAO.createUser(username, password, "user");
    }
}
