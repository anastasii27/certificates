package com.epam.esm.security;

import com.epam.esm.rest.DeniedAccessHandler;
import com.epam.esm.rest.SecurityEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static com.epam.esm.model.Role.ADMIN;
import static com.epam.esm.model.Role.USER;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private SecurityEntryPoint securityEntryPoint;
    @Autowired
    private DeniedAccessHandler deniedAccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,"/users/login", "/users/signup").permitAll()
                .antMatchers(HttpMethod.GET,"/certificates").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/tags", "/tags/{\\d+}",
                        "/users/tags", "/users")
                .hasAnyRole(ADMIN.name(), USER.name())
                .antMatchers(HttpMethod.GET,
                        "/users/{userId}/orders/{\\d+}",
                        "/users/{userId}/orders",
                        "/users/{userId}")
                .access("@userSecurity.hasUserId(authentication, #userId) or hasRole('"+ADMIN.name()+"')")
                .antMatchers(HttpMethod.POST, "/users/{userId}/orders")
                .access("@userSecurity.hasUserId(authentication, #userId) or hasRole('"+ADMIN.name()+"')")
                .antMatchers(HttpMethod.DELETE, "/certificates/{\\d+}", "/tags/{\\d+}").hasRole(ADMIN.name())
                .antMatchers(HttpMethod.PATCH, "/certificates/{\\d+}").hasRole(ADMIN.name())
                .antMatchers(HttpMethod.POST, "/certificates", "/tags").hasRole(ADMIN.name());

        http.exceptionHandling().authenticationEntryPoint(securityEntryPoint)
                .and()
                .exceptionHandling().accessDeniedHandler(deniedAccessHandler);

        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}