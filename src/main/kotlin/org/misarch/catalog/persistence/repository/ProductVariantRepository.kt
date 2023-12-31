package org.misarch.catalog.persistence.repository

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.misarch.catalog.persistence.model.ProductVariantEntity
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for [ProductVariantEntity]s
 */
@Repository
interface ProductVariantRepository : QuerydslR2dbcRepository<ProductVariantEntity, UUID>