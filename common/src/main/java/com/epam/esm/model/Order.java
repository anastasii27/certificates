package com.epam.esm.model;

import com.epam.esm.converter.ZoneIdConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Entity
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
    @NotAudited
    private List<Certificate> certificates;
    @ManyToOne
    @JoinColumn(name="userId", nullable=false, insertable = false, updatable = false)
    @NotAudited
    private User user;
}
