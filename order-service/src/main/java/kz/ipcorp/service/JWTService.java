package kz.ipcorp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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

    private <T > T extractClaim(String token, Function<Claims, T > claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims (String token){
        log.info("IN extractAllClaims - extract claims from a token");
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build().parseClaimsJws(token).getBody();
    }
    public Map<String, String> getClaims(String token){
        Map<String, String> map = new HashMap<>();
        Claims claims = extractAllClaims(token);
        map.put("role", claims.get("role").toString());
        map.put("userId", claims.get("userId").toString());
        return map;
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
