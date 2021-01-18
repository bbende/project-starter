package com.bbende.project.starter.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractEntity<T> {

    @Id
    @NotNull
    @Column(name = "id")
    private T id;

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

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof AbstractEntity)) {
            return false;
        }

        final AbstractEntity other = (AbstractEntity) obj;
        return Objects.equals(id, other.id);
    }
}
