package com.epam.esm.model;

import com.epam.esm.converter.ZoneIdConverter;
import lombok.*;
import org.hibernate.envers.NotAudited;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
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
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "tag_m2m_gift_certificate",
               joinColumns = { @JoinColumn(name = "giftCertificateId")},
               inverseJoinColumns = {@JoinColumn(name = "tagId")})
    @NotAudited
    private Set<Tag> tags;
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "orders_m2m_gift_certificate",
            joinColumns = { @JoinColumn(name = "giftCertificateId")},
            inverseJoinColumns = {@JoinColumn(name = "ordersId")})
    @NotAudited
    private Set<Order> orders;
}
