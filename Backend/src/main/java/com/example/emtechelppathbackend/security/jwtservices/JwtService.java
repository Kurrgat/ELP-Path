package com.example.emtechelppathbackend.security.jwtservices;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${spring.application.secret-key}")
    private String SECRET_KEY ;

    public String extractUsername(String jwtToken) {
        try {
            return extractClaim(jwtToken, Claims::getSubject);
        } catch (ExpiredJwtException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (JwtException e) {
            System.out.println(e.getMessage());
            return null; // or throw a custom exception, depending on your application's logic
        }
    }

    //extracting singleclaim(user Email)
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final  Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    //generating a token without extracting details(from user details itself)
    public String generateToken(UserDetails userDetails){
       return generateToken(new HashMap<>(), userDetails);
    }


    //generating a token
    public String generateToken(Map<String, Object> extractClaims,
                                UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10*60*60*1000))
                .signWith(getSignIngKey(), SignatureAlgorithm.HS256).compact();
    }


    //extracting all claims
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignIngKey())
                .build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSignIngKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    //validating a token
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //checking whether the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //extracting expiration date
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
