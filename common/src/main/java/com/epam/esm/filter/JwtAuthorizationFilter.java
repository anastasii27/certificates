package com.epam.esm.filter;

import com.epam.esm.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component("jwtAuthorizationFilter")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {
        Claims claims = null;
        String jwt = extractJwt(request);

        if(!isEmpty(jwt)){
            claims = jwtUtils.decodeJwt(jwt);
        }

        if (claims != null) {
            String username = claims.getSubject();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                    )
            );
        }
        filterChain.doFilter(request, response);
    }

    private String extractJwt(HttpServletRequest request){
        String jwt = request.getHeader(AUTHORIZATION);

        if (jwt != null && jwt.startsWith(BEARER)) {
            return jwt.substring(BEARER.length());
        }
        return EMPTY;
    }
}
