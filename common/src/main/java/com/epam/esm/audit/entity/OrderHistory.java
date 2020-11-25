package com.epam.esm.audit.entity;

import com.epam.esm.audit.AuditAction;
import com.epam.esm.utils.ZoneIdConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders_aud")
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long entityId;
    @Enumerated(EnumType.STRING)
    private AuditAction operation;
    private LocalDateTime purchaseDate;
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId purchaseTimeZone;
    private BigDecimal cost;
    private long userId;
}
