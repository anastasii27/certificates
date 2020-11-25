package com.epam.esm.repository;

import com.epam.esm.model.Pagination;
import com.epam.esm.model.User;
import com.epam.esm.repository.command.UserCategoryType;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository("userRepository")
public class UserRepositoryImpl implements UserRepository {
    private static final String USERS_LIST  = "SELECT u FROM User u";
    private static final String USER_BY_LOGIN  = "SELECT u FROM User u WHERE u.login=?1";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(
                entityManager.find(User.class, id)
        );
    }

    @Override
    public List<User> findAll(Pagination pagination) {
        return entityManager.createQuery(USERS_LIST, User.class)
                .setFirstResult(pagination.getOffset())
                .setMaxResults(pagination.getLimit())
                .getResultList();
    }

    @Override
    public List<User> findAll(String category) {
        return UserCategoryType.getCommand(category).getUsers(entityManager);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        User user = entityManager.createQuery(USER_BY_LOGIN, User.class)
                .setParameter(1, login)
                .getResultList().stream().findFirst().orElse(null);

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> create(User user) {
        entityManager.persist(user);
        return findById(user.getId());
    }
}

