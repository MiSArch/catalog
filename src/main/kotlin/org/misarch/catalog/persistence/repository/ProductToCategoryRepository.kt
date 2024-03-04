package org.misarch.catalog.persistence.repository

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.misarch.catalog.persistence.model.ProductToCategoryEntity
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for [ProductToCategoryEntity]s
 */
@Repository
interface ProductToCategoryRepository : QuerydslR2dbcRepository<ProductToCategoryEntity, UUID> {

    /**
     * Finds all product to category relations for a given product
     *
     * @param productId the id of the product
     * @return list of product to category relations
     */
    suspend fun findByProductId(productId: UUID): List<ProductToCategoryEntity>

}