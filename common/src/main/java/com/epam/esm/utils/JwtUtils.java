package com.epam.esm.utils;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret-key}")
    private String securityKey;
    private Logger log =  LoggerFactory.getLogger(JwtUtils.class);

    public String generateJwt(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, securityKey)
                .compact();
    }

    public Claims decodeJwt(String jwt) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(securityKey))
                    .parseClaimsJws(jwt).getBody();
        }catch (MalformedJwtException e){
            log.error("The token was constructed incorrectly");
        }catch (SignatureException e){
            log.error("Signature calculating or verifying was failed");
        }
        return claims;
    }
}
