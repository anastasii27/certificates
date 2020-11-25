package com.epam.esm.service;

import com.epam.esm.model.User;
import com.epam.esm.model.UserDetailsImpl;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final String EXCEPTION_KEY  = "exception.auth.not_authorized";
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElseThrow(
                ()->  new UsernameNotFoundException(EXCEPTION_KEY));
        return toUserDetails(user);
    }

    private UserDetails toUserDetails(User user){
        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getValue()));
        return new UserDetailsImpl(user.getId(), user.getName(), user.getLogin(), user.getPassword(), authorities);
    }
}
