package com.epam.esm.rest;

import com.epam.esm.dto.LogInDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserTagDto;
import com.epam.esm.model.JwtResponse;
import com.epam.esm.model.Pagination;
import com.epam.esm.model.UserDetailsImpl;
import com.epam.esm.service.UserService;
import com.epam.esm.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final String BEARER = "Bearer";
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id){
        UserDto user  = userService.findById(id);
        user.add(
                linkTo(methodOn(UserController.class).getUser(id)).withSelfRel()
        );
        return user;
    }

    @GetMapping
    public List<UserDto> getUsers(@Valid Pagination pagination){
        List<UserDto> users = userService.findAll(pagination);
        users.forEach(
                user-> user.add(linkTo(methodOn(UserController.class)
                        .getUser(user.getId()))
                        .withSelfRel())
        );
        return users;
    }

    @GetMapping("/tags")
    public CollectionModel<UserTagDto> getUserTags(@RequestParam String type,
                                                   @RequestParam String category){
        List<UserTagDto> userTags = userService.findUserTags(type, category);

        if(!userTags.isEmpty()) {
            Link link = linkTo(methodOn(UserController.class).getUserTags(type, category)).withSelfRel();
            return new CollectionModel<>(userTags, link);
        }else {
            return CollectionModel.empty();
        }
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse logIn(@RequestBody @Valid LogInDto user){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails  = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwt(userDetails);

        return new JwtResponse(userDetails.getId(),
                userDetails.getName(),
                userDetails.getUsername(),
                userDetails.getAuthorities(),
                jwt, BEARER);
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody @Valid UserDto user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userService.create(user);
    }
}
