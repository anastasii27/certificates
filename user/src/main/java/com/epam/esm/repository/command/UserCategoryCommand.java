package com.epam.esm.repository.command;

import com.epam.esm.model.User;
import org.hibernate.SessionFactory;
import java.util.List;

public interface UserCategoryCommand {
    List<User> getUsers(SessionFactory sessionFactory);
}
