package com.epam.esm.repository;

import com.epam.esm.model.User;
import com.epam.esm.model.Pagination;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    /**
     * This method is used to return a user by id from database.
     *
     * @param id the id of a user to return.
     * @return an Optional with the value of User or empty Optional
     *          if User does not exist.
     */
    Optional<User> findById(long id);
    /**
     * This method is used to return users from database
     * limited by pagination.
     *
     * @param pagination is used to limit amount of orders.
     * @return List of users or empty List if users do not exist.
     */
    List<User> findAll(Pagination pagination);
    /**
     * This method is used to return the List of users from database
     * by category.
     *
     * @param category the category of users to return.
     * @return List of users or empty List if users do not exist.
     */
    List<User> findAll(String category);
}
