package com.epam.esm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse{
    private long id;
    private String userName;
    private String userLogin;
    private Collection<? extends GrantedAuthority> authorities;
    private String jwt;
    private String tokenType;
}
