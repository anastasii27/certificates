package com.epam.esm.audit.entity;

import com.epam.esm.audit.AuditAction;
import com.epam.esm.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_aud")
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long entityId;
    @Enumerated(EnumType.STRING)
    private AuditAction operation;
    private String name;
    private String login;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
