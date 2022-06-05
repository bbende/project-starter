package com.bbende.project.starter.common.persistence;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class Entity<T extends Serializable> {

    @Id
    @NotNull
    @Column(name = "id")
    protected T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " { id = " + getId() + " }";
    }

}
