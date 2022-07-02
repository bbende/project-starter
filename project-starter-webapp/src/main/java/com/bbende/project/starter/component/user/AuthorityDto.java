package com.bbende.project.starter.component.user;

import java.util.Objects;

public class AuthorityDto {

    private final AuthorityName name;

    private AuthorityDto() {
        this.name = null;
    }

    public AuthorityDto(final AuthorityName name) {
        this.name = name;
    }

    public AuthorityName getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AuthorityDto that = (AuthorityDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
