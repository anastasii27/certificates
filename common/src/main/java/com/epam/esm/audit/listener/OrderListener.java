package com.epam.esm.audit.listener;

import com.epam.esm.audit.AuditAction;
import com.epam.esm.audit.entity.OrderHistory;
import com.epam.esm.model.Order;
import javax.persistence.*;
import static com.epam.esm.audit.AuditAction.*;

public class OrderListener {
    @PersistenceContext
    private EntityManager entityManager;

    @PostPersist
    public void postPersist(Order order){
        insertIntoAuditTable(INSERT, order);
    }

    @PreUpdate
    public void preUpdate(Order order) {
        insertIntoAuditTable(UPDATE, order);
    }

    @PreRemove
    public void preRemove(Order order){
        insertIntoAuditTable(DELETE, order);
    }

    private void insertIntoAuditTable(AuditAction action, Order order){
        entityManager.persist(new OrderHistory(0, order.getId(), action,
                order.getPurchaseDate(), order.getPurchaseTimeZone(), order.getCost(), order.getUserId()));
    }
}
