package com.bbende.project.starter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class ListDTO<T> {

    private List<T> elements;

    public ListDTO() {
    }

    public ListDTO(final List<T> elements) {
        this.elements = elements;
    }

    @ApiModelProperty
    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

}
