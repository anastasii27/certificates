package com.epam.esm.audit.entity;

import com.epam.esm.audit.AuditAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
