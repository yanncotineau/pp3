package ca.uqac.loginservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class TokenManager {

    private static final String issuer = "pp3";
    public static final String secret = "pp3-secret";
    public static final Algorithm algorithm = Algorithm.HMAC256(secret);

    public static String getTokenFromUser(String username, String password) {
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .withClaim("username", username)
                .withClaim("password", password)
                .sign(algorithm);
    }

    public static String getUserFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer(issuer)
                .build().verify(token);
        return decodedJWT.getClaim("username").toString().replace("\"", ""); // toString() gives "string" instead of string
    }
}
