package com.epam.esm.service;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.OrderCertificateDtoConverter;
import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Order;
import com.epam.esm.model.Pagination;
import com.epam.esm.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(SpringConfig.class)
@SpringBootTest
@EnableConfigurationProperties
@ActiveProfiles("test")
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @SpyBean
    private OrderConverter orderConverter;
    @SpyBean
    private OrderCertificateDtoConverter orderCertificateDtoConverter;
    @SpyBean
    private CertificateConverter certificateConverter;
    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();
    private Order order;
    private OrderDto orderDto;
    private Pagination pagination;
    private List<Order> orderList;
    private List<OrderDto> orderDtoList;
    private ZonedDateTime date;

    @BeforeEach
    void setUp() {
        date = ZonedDateTime.of(
                LocalDateTime.of(2020, 12, 7, 12, 12),
                ZoneId.of("+03:00")
        );
        order = new Order(1, date.toLocalDateTime(), date.getZone(),
                BigDecimal.valueOf(12), 1, null, null);
        orderDto = new OrderDto(1, date,
                BigDecimal.valueOf(12));
        pagination = new Pagination(100, 0);
        orderList = Collections.singletonList(order);
        orderDtoList = Collections.singletonList(orderDto);
    }

    @Test
    void getUserOrder_whenUserDoesNotExist_thenEntityNotFoundException() {
        when(orderRepository.getOrderInfo(1, 1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()-> orderService.getUserOrder(1,1));
    }

    @Test
    void getUserOrder_whenOrderDoesNotExist_thenEntityNotFoundException() {
        when(orderRepository.getOrderInfo(1, 1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()-> orderService.getUserOrder(1,1));
    }

    @Test
    void getUserOrder_whenOrderExists_thenReturnOrder() {
        when(orderRepository.getOrderInfo(1, 1)).thenReturn(Optional.ofNullable(order));
        assertEquals(orderDto, orderService.getUserOrder(1, 1));
    }

    @Test
    void getUserOrders_whenUserDoesNotExist_thenReturnEmptyList() {
        when(orderRepository.doesUserExist(1)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, ()-> orderService.getUserOrders(1,pagination));
    }

    @Test
    void getUserOrders_whenUserExists_thenReturnOrderList() {
        when(orderRepository.getUserOrders(1, pagination)).thenReturn(orderList);
        assertIterableEquals(orderDtoList, orderService.getUserOrders(1, pagination));
    }
}