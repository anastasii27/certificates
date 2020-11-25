package com.epam.esm.model;

import com.epam.esm.audit.listener.OrderListener;
import com.epam.esm.utils.ZoneIdConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(OrderListener.class)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime purchaseDate;
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId purchaseTimeZone;
    private BigDecimal cost;
    private long userId;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "orders")
    private List<Certificate> certificates;
    @ManyToOne
    @JoinColumn(name="userId", nullable=false, insertable = false, updatable = false)
    private User user;
}
