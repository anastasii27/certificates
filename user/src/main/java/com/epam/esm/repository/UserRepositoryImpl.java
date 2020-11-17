package com.epam.esm.repository;

import com.epam.esm.model.User;
import com.epam.esm.repository.command.UserCategoryType;
import com.epam.esm.model.Pagination;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository("userRepository")
public class UserRepositoryImpl implements UserRepository {
    private static final String USERS_LIST  = "SELECT u FROM User u";
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(
                sessionFactory.getCurrentSession().find(User.class, id)
        );
    }

    @Override
    public List<User> findAll(Pagination pagination) {
        return sessionFactory.getCurrentSession()
                .createQuery(USERS_LIST, User.class)
                .setFirstResult(pagination.getOffset())
                .setMaxResults(pagination.getLimit())
                .list();
    }

    @Override
    public List<User> findAll(String category) {
        return UserCategoryType.getCommand(category).getUsers(sessionFactory);
    }
}

