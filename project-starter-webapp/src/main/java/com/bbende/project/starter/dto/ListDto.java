package com.bbende.project.starter.dto;

import java.util.List;

public class ListDto<T> {

    private List<T> elements;

    public ListDto() {
    }

    public ListDto(final List<T> elements) {
        this.elements = elements;
    }

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

}
