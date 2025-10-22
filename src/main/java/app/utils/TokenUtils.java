package app.utils;

import app.dto.UserDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import app.entities.User;

import java.util.Date;

public class TokenUtils {

    private static String getSecretKey() {
        String secret = System.getenv("JWT_SECRET_KEY");
        if (secret == null) {
            secret = Utils.getPropertyValue("JWT_SECRET_KEY", "config.properties");
        }
        return secret;
    }

    private static final String SECRET_KEY = getSecretKey();
    private static final String ISSUER = "DinnerRouletteAPI";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    private static final int TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1 time (i millisekunder)

    public static String createToken(UserDTO user) {
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getUsername())
                    .withClaim("username", user.getUsername())
                    .withClaim("role", user.getRole())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRE_TIME))
                    .sign(ALGORITHM);
        } catch (JWTCreationException exception){
            exception.printStackTrace();
            throw new RuntimeException("Error creating token", exception);
        }
    }

    public static DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception){
            // Token er ugyldig
            return null;
        }
    }
}

