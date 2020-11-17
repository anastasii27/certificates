package com.epam.esm.service;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.OrderCertificateDtoConverter;
import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.model.Order;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Pagination;
import com.epam.esm.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    private static final long ORDER_ERROR_CODE_NOT_FOUND = 40404;
    private static final long USER_ERROR_CODE_NOT_FOUND = 40403;
    private static final String ORDER_NOT_FOUND_EXCEPTION_KEY = "exception.order.not_found";
    private static final String USER_NOT_FOUND_EXCEPTION_KEY = "exception.user.not_found";
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderConverter orderConverter;
    @Autowired
    private OrderCertificateDtoConverter orderCertificateDtoConverter;
    @Autowired
    private CertificateConverter certificateConverter;

    @Override
    public OrderDto getUserOrder(long userId, long orderId) {
        Order order = orderRepository.getOrderInfo(userId, orderId)
                .orElseThrow(()->
                        new EntityNotFoundException(ORDER_NOT_FOUND_EXCEPTION_KEY, orderId, ORDER_ERROR_CODE_NOT_FOUND));

        return orderConverter.toDto(order);
    }

    @Override
    public List<OrderDto> getUserOrders(long userId, Pagination pagination) {
        if(!orderRepository.doesUserExist(userId)){
            throw new EntityNotFoundException(USER_NOT_FOUND_EXCEPTION_KEY, userId, USER_ERROR_CODE_NOT_FOUND);
        }
        return orderConverter.toDtoList(
                orderRepository.getUserOrders(userId, pagination)
        );
    }

    @Override
    @Transactional
    public OrderDto order(OrderCertificateDto orderCertificateDto, long userId) {
        if(!orderRepository.doesUserExist(userId)){
            throw new EntityNotFoundException(USER_NOT_FOUND_EXCEPTION_KEY, userId, USER_ERROR_CODE_NOT_FOUND);
        }
        List<CertificateDto> certificates = orderCertificateDto.getOrderedCertificates();

        Order orderToCreate = orderCertificateDtoConverter.toEntity(orderCertificateDto);
        orderToCreate.setUserId(userId);

        Optional<Order> createdOrder = orderRepository.order(orderToCreate,
                                                                certificateConverter.toEntityList(certificates));
        if(createdOrder.isPresent()){
            return orderConverter.toDto(createdOrder.get());
        }else {
            return new OrderDto();
        }
    }
}
