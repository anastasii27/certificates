package com.epam.esm.audit.listener;

import com.epam.esm.audit.AuditAction;
import com.epam.esm.audit.entity.UserHistory;
import com.epam.esm.model.User;
import javax.persistence.*;
import static com.epam.esm.audit.AuditAction.*;

public class UserListener {
    @PersistenceContext
    private EntityManager entityManager;

    @PostPersist
    public void postPersist(User user){
       insertIntoAuditTable(INSERT, user);
    }

    @PreUpdate
    public void preUpdate(User user) {
        insertIntoAuditTable(UPDATE, user);
    }

    @PreRemove
    public void preRemove(User user){
        insertIntoAuditTable(DELETE, user);
    }

    private void insertIntoAuditTable(AuditAction action, User user){
        entityManager.persist(UserHistory.builder()
                .entityId(user.getId())
                .operation(action)
                .name(user.getName())
                .login(user.getLogin())
                .password(user.getPassword())
                .role(user.getRole())
                .build()
        );
    }
}
