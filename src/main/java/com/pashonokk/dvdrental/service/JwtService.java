package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.util.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    public String getUserName(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = getUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = getAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        extraClaims.put("roles", roles);
        Date expiration = Date.from(LocalDateTime.now().plusMinutes(jwtProperties.getExpiration().toMinutes())
                .atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).isBefore(OffsetDateTime.now());
    }

    public OffsetDateTime getExpiration(String token) {
        Date date = getClaim(token, Claims::getExpiration);
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();

    }

    private byte[] getSigningKey() {
        return Base64.getDecoder().decode(jwtProperties.getSigning());
    }

}
