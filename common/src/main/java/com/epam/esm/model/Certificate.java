package com.epam.esm.model;

import com.epam.esm.utils.ZoneIdConverter;
import com.epam.esm.audit.listener.CertificateListener;
import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(CertificateListener.class)
@Table(name = "certificates")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime createDate;
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId createDateTimezone;
    private LocalDateTime lastUpdateDate;
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId lastUpdateDateTimezone;
    private long duration;
    @ManyToMany
    @JoinTable(name = "tag_m2m_gift_certificate",
               joinColumns = { @JoinColumn(name = "giftCertificateId")},
               inverseJoinColumns = {@JoinColumn(name = "tagId")})
    private Set<Tag> tags;
    @ManyToMany
    @JoinTable(name = "orders_m2m_gift_certificate",
            joinColumns = { @JoinColumn(name = "giftCertificateId")},
            inverseJoinColumns = {@JoinColumn(name = "ordersId")})
    private Set<Order> orders;
}
