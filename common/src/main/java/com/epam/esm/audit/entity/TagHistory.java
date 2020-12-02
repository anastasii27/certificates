package com.epam.esm.audit.entity;

import com.epam.esm.audit.AuditAction;
import lombok.Builder;
import lombok.Data;
import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "tag_aud")
public class TagHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long entityId;
    @Enumerated(EnumType.STRING)
    private AuditAction operation;
    private String name;
}
