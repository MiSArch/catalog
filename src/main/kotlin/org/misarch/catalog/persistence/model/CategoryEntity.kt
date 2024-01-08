package org.misarch.catalog.persistence.model

import org.misarch.catalog.graphql.model.Category
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

/**
 * Entity for categories
 *
 * @property name name of the category
 * @property description description of the category
 * @property id unique identifier of the category
 */
@Table
class CategoryEntity(
    val name: String,
    val description: String,
    @Id
    val id: UUID?
) : BaseEntity<Category> {

    companion object {
        /**
         * Querydsl entity
         */
        val ENTITY = QCategoryEntity.categoryEntity!!
    }

    override fun toDTO(): Category {
        return Category(
            id = id!!,
            name = name,
            description = description
        )
    }

}