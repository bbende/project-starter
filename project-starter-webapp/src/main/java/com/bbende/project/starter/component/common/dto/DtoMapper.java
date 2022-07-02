package com.bbende.project.starter.component.common.dto;

/**
 * Mapper that converts back and forth between a DTO and Entity.
 *
 * @param <D> the type of DTO
 * @param <E> the type of Entity
 */
public interface DtoMapper<D, E> {

    /**
     * Converts an entity to a DTO.
     *
     * @param entity the entity
     * @return the dto
     */
    D toDto(E entity);

}
