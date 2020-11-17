package com.epam.esm.service;

import com.epam.esm.dto.OrderCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Pagination;
import java.util.List;

public interface OrderService {
    /**
     * This method is used to return user order. If user does not
     * have this other EntityNotFoundException will be thrown.
     *
     * @param userId the id of a user whose order to return.
     * @param orderId the id of an order to be returned.
     * @return user Order.
     * @throws EntityNotFoundException if order does not exist.
     */
    OrderDto getUserOrder(long userId, long orderId);
    /**
     * This method is used to return user orders limited
     * by pagination.
     *
     * @param userId the id of a user whose order to return.
     * @param pagination is used to limit amount of orders.
     * @return List of orders or empty List if orders do not exist.
     * @throws EntityNotFoundException if user does not exist.
     */
    List<OrderDto> getUserOrders(long userId, Pagination pagination);
    /**
     * This method is used to make an order and then
     * return created one. If order was not created the method would
     * return an empty Order.
     *
     * @param orderCertificateDto order created by user.
     * @param userId the id of a user who has made an order.
     * @return created Order or empty Order if it was not created.
     * @throws EntityNotFoundException if user does not exist.
     */
    OrderDto order(OrderCertificateDto orderCertificateDto, long userId);
}
