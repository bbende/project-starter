package com.bbende.project.starter.component.user;

import com.bbende.project.starter.common.persistence.Entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Objects;

@javax.persistence.Entity
@Table(name = "ps_authority")
public class Authority extends Entity<Long> {

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    public AuthorityName getName() {
        return name;
    }

    public void setName(AuthorityName name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Authority authority = (Authority) o;
        return Objects.equals(id, authority.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
