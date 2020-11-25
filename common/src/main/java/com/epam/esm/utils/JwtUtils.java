package com.epam.esm.utils;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@Component
public class JwtUtils {
    private static final String SECURITY_KEY = "VCSEDNVe1vsJqUjjj2C819e6DcVuZ8NZeuzaYlMSz51OJgWnWXnyjXMv7er3Z46";

    public String generateJwt(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SECURITY_KEY)
                .compact();
    }

    public Claims decodeJwt(String jwt) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECURITY_KEY))
                    .parseClaimsJws(jwt).getBody();
        }catch (MalformedJwtException e){

        }catch (SignatureException e){

        }
        return claims;
    }
}
