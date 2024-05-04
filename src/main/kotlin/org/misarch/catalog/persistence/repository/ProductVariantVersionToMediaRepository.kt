package org.misarch.catalog.persistence.repository

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.misarch.catalog.persistence.model.ProductVariantVersionToMediaEntity
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for [ProductVariantVersionToMediaEntity]s
 */
@Repository
interface ProductVariantVersionToMediaRepository : QuerydslR2dbcRepository<ProductVariantVersionToMediaEntity, UUID>