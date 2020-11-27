package com.epam.esm.audit.listener;

import com.epam.esm.audit.AuditAction;
import com.epam.esm.audit.entity.CertificateHistory;
import com.epam.esm.model.Certificate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import static com.epam.esm.audit.AuditAction.*;

public class CertificateListener {
    @PersistenceContext
    private EntityManager entityManager;

    @PostPersist
    @Transactional
    public void postPersist(Certificate certificate){
       insertIntoAuditTable(INSERT, certificate);
    }

    @PreUpdate
    public void preUpdate(Certificate certificate){
        insertIntoAuditTable(UPDATE, certificate);
    }

    @PreRemove
    public void preRemove(Certificate certificate){
        insertIntoAuditTable(DELETE, certificate);
    }

    private void insertIntoAuditTable(AuditAction action, Certificate certificate){
        entityManager.persist(new CertificateHistory(0, certificate.getId(), action, certificate.getName(),
                certificate.getDescription(), certificate.getPrice(), certificate.getCreateDate(), certificate.getCreateDateTimezone(),
                certificate.getLastUpdateDate(), certificate.getLastUpdateDateTimezone(), certificate.getDuration()));
    }
}
