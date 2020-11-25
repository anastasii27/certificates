package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Pagination;
import com.epam.esm.dto.UserTagDto;
import com.epam.esm.model.User;

import java.util.List;

public interface UserService {
    /**
     * This method is used to return a user by id.
     *
     * @param id the id of user to return.
     * @return searched user.
     * @throws EntityNotFoundException if user does not exist.
     */
    UserDto findById(long id);
    /**
     * This method is used to return the List of users
     * limited by pagination.
     *
     * @param pagination is used to limit amount of users.
     * @return List of users or empty List if no users were found.
     */
    List<UserDto> findAll(Pagination pagination);
    /**
     * This method is used to return user tags by category and
     * search type.
     *
     * @param searchType the type of tags to find.
     * @param category the category of tags to find.
     * @return List of tags or empty List if no tags were found.
     */
    List<UserTagDto> findUserTags(String searchType, String category);
    UserDto create(UserDto user);
}
