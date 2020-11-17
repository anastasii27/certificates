package com.epam.esm.rest;

import com.epam.esm.dto.OrderCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.model.Pagination;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/users/{userId}/orders/{orderId}")
    public OrderDto getUserOrder(@PathVariable long userId, @PathVariable long orderId){
        OrderDto orderDto = orderService.getUserOrder(userId, orderId);
        orderDto.add(
                linkTo(methodOn(OrderController.class).getUserOrder(userId, orderId)).withSelfRel()
        );
        return orderDto;
    }

    @GetMapping("/users/{userId}/orders")
    public List<OrderDto> getUserOrders(@PathVariable long userId, @Valid Pagination pagination){
        List<OrderDto> ordersDto =  orderService.getUserOrders(userId, pagination);
        ordersDto.forEach(
                order->order.add(linkTo(methodOn(OrderController.class)
                        .getUserOrder(userId, order.getId()))
                        .withSelfRel())
        );
        return ordersDto;
    }

    @PostMapping("/users/{userId}/orders")
    public OrderDto order(@RequestBody @Valid OrderCertificateDto orderCertificateDto, @PathVariable long userId){
        OrderDto orderDto = orderService.order(orderCertificateDto, userId);
        orderDto.add(
                linkTo(methodOn(OrderController.class).order(orderCertificateDto, userId)).withSelfRel()
        );
        return orderDto;
    }
}
