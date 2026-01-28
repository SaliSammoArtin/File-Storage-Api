package se.sali.webbapplikation.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class JWTService {

    private static final Algorithm algorithm = Algorithm.HMAC256("nkdsjbnsöldclöaåsrowj4940fkoes");
    private final JWTVerifier verifier = JWT.require(algorithm).build();

    public String generateToken(UUID userId) {
        return JWT.create()
                .withSubject(userId.toString())
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .sign(algorithm);
    }

    public UUID validateToken(String token) {
        DecodedJWT jwt = verifier.verify(token);
        return UUID.fromString(jwt.getSubject());
    }
}
