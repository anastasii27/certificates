package com.epam.esm.audit.entity;

import com.epam.esm.audit.AuditAction;
import com.epam.esm.utils.ZoneIdConverter;
import lombok.Builder;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Builder
@Entity
@Table(name = "certificates_aud")
public class CertificateHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long entityId;
    @Enumerated(EnumType.STRING)
    private AuditAction operation;
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
}
