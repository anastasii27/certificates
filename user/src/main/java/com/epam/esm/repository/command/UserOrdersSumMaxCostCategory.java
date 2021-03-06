package com.epam.esm.repository.command;

import com.epam.esm.model.User;
import javax.persistence.EntityManager;
import java.util.List;

public class UserOrdersSumMaxCostCategory implements UserCategoryCommand {
    private static final String USERS_WITH_THE_HIGHEST_COST_OF_ALL_ORDERS  = "SELECT t.userId AS `id`, t.`name`, t.login, " +
            "t.`password`, t.role FROM (\n" +
            "\tSELECT userId, `name`, login, `password`, role, sum(cost) AS totalCost FROM `gift-certificates`.orders\n" +
            "\tJOIN `gift-certificates`.users ON userId = `gift-certificates`.users.id\n" +
            "\tGROUP BY userId) t \n" +
            "WHERE t.totalCost = (\n" +
            "\tSELECT max(t1.totalCost) FROM (SELECT userId, sum(cost) AS totalCost FROM `gift-certificates`.orders\n" +
            "\tGROUP BY userId) t1)";

    @Override
    public List<User> getUsers(EntityManager entityManager) {
        return entityManager
                .createNativeQuery(USERS_WITH_THE_HIGHEST_COST_OF_ALL_ORDERS, User.class)
                .getResultList();
    }
}
