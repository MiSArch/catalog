package org.misarch.catalog.persistence.model

import java.util.UUID

/**
 * An entity that can be converted to a DTO
 *
 * @param T the DTO type
 */
interface BaseEntity<T> {

    /**
     * Converts the entity to the DTO
     *
     * @return the converted DTO
     */
    fun toDTO(): T

    /**
     * The primary key of the entity
     */
    val id: UUID?

}