package com.epam.esm.rest;

import com.epam.esm.dto.UserDto;
import com.epam.esm.model.Pagination;
import com.epam.esm.dto.UserTagDto;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

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
}
