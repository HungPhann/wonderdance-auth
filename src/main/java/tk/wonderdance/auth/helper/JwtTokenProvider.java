package tk.wonderdance.auth.helper;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {


    @Value("${app.jwtSecret}")
    private String secret;

    @Value("${app.authorizationCodePeriod}")
    private long authorizationCodePeriod;

    @Value("${app.accessTokenPeriod}")
    private long accessTokenPeriod;

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);


    public String generateAccessToken(long user_id, String email) {
        String base64SecretBytes = Base64.getEncoder().encodeToString(secret.getBytes());
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (accessTokenPeriod)); //day

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date())
                .setIssuer("Wonder Dance")
                .setExpiration(exp)
                .claim("user_id", user_id)
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS256, base64SecretBytes)
                .compact();

        return token;
    }

    public Claims verifyAccessToken(String token) throws ExpiredJwtException, SignatureException, Exception{
        String base64SecretBytes = Base64.getEncoder().encodeToString(secret.getBytes());
        Claims claims = Jwts.parser()
                .setSigningKey(base64SecretBytes)
                .parseClaimsJws(token).getBody();
        return claims;
    }

}