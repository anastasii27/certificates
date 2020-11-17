package com.epam.esm.converter;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.model.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverter {
    @Autowired
    private ModelMapper modelMapper;

    public OrderDto toDto(Order order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);

        ZonedDateTime purchaseDate = ZonedDateTime.of(order.getPurchaseDate(), order.getPurchaseTimeZone());
        orderDto.setPurchaseDate(purchaseDate);
        return orderDto;
    }

    public List<OrderDto> toDtoList(List<Order> entityList) {
       return entityList.stream()
               .map(this::toDto)
               .collect(Collectors.toList());
    }
}
