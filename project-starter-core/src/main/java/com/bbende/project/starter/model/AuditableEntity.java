package com.bbende.project.starter.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import java.util.Date;

@MappedSuperclass
public abstract class AuditableEntity<T> extends AbstractEntity<T> {

    @NotNull
    @Column(name = "CREATED")
    private Date created;

    @NotNull
    @Column(name = "UPDATED")
    private Date updated;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @PrePersist
    public void prePersist() {
        final long currentTime = System.currentTimeMillis();
        this.created = new Date(currentTime);
        this.updated = new Date(currentTime);
    }

    @PreUpdate
    public void preUpdate() {
        final long currentTime = System.currentTimeMillis();
        this.updated = new Date(currentTime);
    }

}
