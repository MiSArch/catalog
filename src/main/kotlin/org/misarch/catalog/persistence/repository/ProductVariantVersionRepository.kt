package org.misarch.catalog.persistence.repository

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.misarch.catalog.persistence.model.ProductVariantVersionEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


/**
 * Repository for [ProductVariantVersionEntity]s
 */
@Repository
interface ProductVariantVersionRepository : QuerydslR2dbcRepository<ProductVariantVersionEntity, UUID> {

    /**
     * Finds the maximum version of a product variant
     *
     * @param productVariantId the id of the product variant
     * @return the maximum version of the product variant, null if there are no versions
     */
    @Query("SELECT MAX(p.version) FROM ProductVariantVersionEntity p WHERE p.productVariantId = :productVariantId")
    suspend fun findMaxVersionByProductVariantId(
        @Param("productVariantId")
        productVariantId: UUID
    ): Int?

}