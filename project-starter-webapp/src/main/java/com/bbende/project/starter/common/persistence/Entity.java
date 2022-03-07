package com.bbende.project.starter.common.persistence;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
public abstract class Entity<T> {

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

        if (!(obj instanceof Entity)) {
            return false;
        }

        final Entity other = (Entity) obj;
        return Objects.equals(id, other.id);
    }
}
