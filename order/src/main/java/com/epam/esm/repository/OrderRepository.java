package com.epam.esm.repository;

import com.epam.esm.model.Order;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Pagination;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    /**
     * This method is used to return user order from database.
     *
     * @param userId the id of a user whose order to return.
     * @param orderId the id of an order to be returned.
     * @return an Optional with the value of Order or empty Optional
     *          if Order does not exist.
     */
    Optional<Order> getOrderInfo(long userId, long orderId);
    /**
     * This method is used to return user orders from database
     * limited by pagination.
     *
     * @param userId the id of a user whose order to return.
     * @param pagination is used to limit amount of orders.
     * @return List of orders or empty List if orders do not exist.
     */
    List<Order> getUserOrders(long userId, Pagination pagination);
    /**
     * This method is used to create an order in database.
     *
     * @param order the order to create.
     * @param certificates the List of certificates which user wants to buy.
     * @return an Optional with the value of created Order or empty Optional
     *          if no Order was created.
     */
    Optional<Order> order(Order order, List<Certificate> certificates);
    /**
     * This method is used to check if user exists in database.
     *
     * @param userId the id of a user to check.
     * @return true if user exists, false if it does not.
     */
    boolean doesUserExist(long userId);
}
