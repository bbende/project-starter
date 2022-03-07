package com.bbende.project.starter.common.dto;

import com.bbende.project.starter.common.persistence.Entity;

/**
 * Mapper that converts back and forth between a DTO and Entity.
 *
 * @param <D> the type of DTO
 * @param <E> the type of Entity
 */
public interface DtoMapper<D extends Dto, E extends Entity> {

    /**
     * Converts an entity to a DTO.
     *
     * @param entity the entity
     * @return the dto
     */
    D toDto(E entity);

    /**
     * Converts a DTO to an entity.
     *
     * @param dto the dto
     * @return the entity
     */
    E toEntity(D dto);

}
