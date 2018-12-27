package com.bbende.project.starter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PERSON")
public class Person extends AuditableEntity<String> {

    @NotBlank
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotBlank
    @Column(name = "LAST_NAME")
    private String lastName;

    @NotNull
    @Min(1)
    @Column(name = "AGE")
    private Integer age;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
