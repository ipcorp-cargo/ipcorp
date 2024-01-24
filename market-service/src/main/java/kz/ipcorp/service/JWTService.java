package kz.ipcorp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kz.ipcorp.model.entity.Seller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JWTService {
    private final Logger log = LogManager.getLogger(JWTService.class);

    public String generateToken(Seller seller) {
        log.info("IN generateToken - access token has been generated");
        return Jwts.builder()
                .setSubject(seller.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, Seller seller) {
        log.info("IN generateRefreshToken - refresh token has been generated");
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(seller.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        log.info("IN extractAllClaims - extract claims from a token");
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build().parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UUID extractID(String token) {
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    private Key getSignKey() {
        byte[] key = Decoders.BASE64.decode("aaecfe6f0be6d8cb425b27aff443ea6c78bca65576a4e487eeb98a473988691e");
        return Keys.hmacShaKeyFor(key);
    }

    public boolean isTokenValid(String token) {
        boolean status = isTokenExpired(token);
        if(!status){
            log.info("IN isTokenValid - token has not expired");
        }else{
            log.info("IN isTokenValid - token has expired");
        }
        return !status;
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }


}