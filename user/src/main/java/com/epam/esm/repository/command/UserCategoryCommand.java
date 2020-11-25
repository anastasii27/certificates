package com.epam.esm.repository.command;

import com.epam.esm.model.User;
import javax.persistence.EntityManager;
import java.util.List;

public interface UserCategoryCommand {
    List<User> getUsers(EntityManager entityManager);
}
