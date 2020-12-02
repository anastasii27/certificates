package com.epam.esm.audit.listener;

import com.epam.esm.audit.AuditAction;
import com.epam.esm.audit.entity.TagHistory;
import com.epam.esm.model.Tag;
import javax.persistence.*;
import static com.epam.esm.audit.AuditAction.*;

public class TagListener {
    @PersistenceContext
    private EntityManager entityManager;

    @PostPersist
    public void postPersist(Tag tag){
        insertIntoAuditTable(INSERT, tag);
    }

    @PreUpdate
    public void preUpdate(Tag tag) {
        insertIntoAuditTable(UPDATE, tag);
    }

    @PreRemove
    public void preRemove(Tag tag){
        insertIntoAuditTable(DELETE, tag);
    }

    private void insertIntoAuditTable(AuditAction action, Tag tag){
        entityManager.persist(TagHistory.builder()
                .entityId(tag.getId())
                .operation(action)
                .name(tag.getName())
                .build()
        );
    }
}
