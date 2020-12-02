package com.epam.esm.audit.listener;

import com.epam.esm.audit.AuditAction;
import com.epam.esm.audit.entity.CertificateHistory;
import com.epam.esm.model.Certificate;
import javax.persistence.*;
import static com.epam.esm.audit.AuditAction.*;

public class CertificateListener {
    @PersistenceContext
    private EntityManager entityManager;

    @PostPersist
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
        entityManager.persist(CertificateHistory.builder()
                .entityId(certificate.getId())
                .operation(action)
                .name(certificate.getName())
                .description(certificate.getDescription())
                .price(certificate.getPrice())
                .createDate(certificate.getCreateDate())
                .createDateTimezone(certificate.getCreateDateTimezone())
                .lastUpdateDate(certificate.getLastUpdateDate())
                .lastUpdateDateTimezone(certificate.getLastUpdateDateTimezone())
                .duration(certificate.getDuration())
                .build()
        );
    }
}
