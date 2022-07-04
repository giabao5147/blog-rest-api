package com.example.blog.security;

import com.example.blog.exception.BadRequestException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.duration}")
    private int jwtDuration;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtDuration);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        return token;
    }

    public String getUserNameFromJWT(String token) {
        if (token == null) {
            return null;
        }
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            throw new BadRequestException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new BadRequestException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new BadRequestException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new BadRequestException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("JWT claims string is empty.");
        }
    }

}
