package kz.ipcorp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kz.ipcorp.model.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    private final Logger log = LogManager.getLogger(JWTService.class);

        public String generateToken(User user){
            log.info("IN generateToken - access token has been generated");
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().toString());
            claims.put("userId", user.getId().toString());
            log.info("role: {}", claims.get("role"));
            log.info("userId: {}", claims.get("userId"));
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getId().toString())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
            }
        public String generateRefreshToken(Map<String, Object> extraClaims, User user){
            log.info("IN generateRefreshToken - refresh token has been generated");
            return Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(user.getId().toString())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        private <T > T extractClaim(String token, Function < Claims, T > claimsResolver) {
            Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        private Claims extractAllClaims (String token){
            log.info("IN extractAllClaims - extract claims from a token");
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build().parseClaimsJws(token).getBody();
        }

        public String extractID (String token){
            return extractClaim(token, Claims::getSubject);
        }

        private Key getSignKey() {
            byte[] key = Decoders.BASE64.decode("aaecfe6f0be6d8cb425b27aff443ea6c78bca65576a4e487eeb98a473988691e");
            return Keys.hmacShaKeyFor(key);
        }

        public boolean isTokenExpired (String token){
            return extractClaim(token, Claims::getExpiration).before(new Date());
        }

}
