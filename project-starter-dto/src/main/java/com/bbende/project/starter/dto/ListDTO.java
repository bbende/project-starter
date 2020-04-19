package com.bbende.project.starter.dto;

import java.util.List;

public class ListDTO<T> {

    private List<T> elements;

    public ListDTO() {
    }

    public ListDTO(final List<T> elements) {
        this.elements = elements;
    }

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

}
