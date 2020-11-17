package com.epam.esm.converter;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderCertificateDto;
import com.epam.esm.model.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class OrderCertificateDtoConverter {
    @Autowired
    private ModelMapper modelMapper;

    public Order toEntity(OrderCertificateDto orderCertificateDto) {
        Order order = modelMapper.map(orderCertificateDto, Order.class);

        order.setCost(orderPrice(orderCertificateDto));
        order.setPurchaseDate(getLocalDateTime(orderCertificateDto));
        order.setPurchaseTimeZone(getZoneId(orderCertificateDto));
        return order;
    }

    private BigDecimal orderPrice(OrderCertificateDto orderCertificateDto){
        return orderCertificateDto.getOrderedCertificates().stream()
                .map(CertificateDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private ZoneId getZoneId(OrderCertificateDto orderCertificateDto){
        ZonedDateTime dateTime = orderCertificateDto.getPurchaseDate();
        return dateTime.getZone();
    }

    private LocalDateTime getLocalDateTime(OrderCertificateDto orderCertificateDto){
        ZonedDateTime dateTime = orderCertificateDto.getPurchaseDate();
        return dateTime.toLocalDateTime();
    }
}
