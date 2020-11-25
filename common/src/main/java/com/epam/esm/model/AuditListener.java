package com.epam.esm.model;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class AuditListener {
    @PreRemove
    @PrePersist
    @PreUpdate
    public void beforeAnyAction(){

    }
}
