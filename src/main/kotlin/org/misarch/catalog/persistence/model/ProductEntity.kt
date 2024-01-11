package org.misarch.catalog.persistence.model

import org.misarch.catalog.event.model.ProductDTO
import org.misarch.catalog.graphql.model.Product
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

/**
 * Entity for products
 *
 * @property internalName internal name of the product
 * @property isPubliclyVisible whether the product is publicly visible
 * @property defaultVariantId id of the default variant of the product
 * @property id unique identifier of the product
 */
@Table
class ProductEntity(
    var internalName: String,
    var isPubliclyVisible: Boolean,
    var defaultVariantId: UUID?,
    @Id
    val id: UUID?
) : BaseEntity<Product> {

    companion object {
        /**
         * Querydsl entity
         */
        val ENTITY = QProductEntity.productEntity!!
    }

    override fun toDTO(): Product {
        return Product(
            id = id!!,
            internalName = internalName,
            isPubliclyVisible = isPubliclyVisible,
            defaultVariantId = defaultVariantId!!
        )
    }

    /**
     * Converts the entity to an event DTO
     *
     * @return event DTO
     */
    fun toEventDTO(): ProductDTO {
        return ProductDTO(
            id = id!!,
            internalName = internalName,
            isPubliclyVisible = isPubliclyVisible,
            defaultVariantId = defaultVariantId!!
        )
    }

}