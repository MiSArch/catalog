package org.misarch.catalog.persistence.repository

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.misarch.catalog.persistence.model.CategoryCharacteristicEntity
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for [CategoryCharacteristicEntity]s
 */
@Repository
interface CategoryCharacteristicRepository : QuerydslR2dbcRepository<CategoryCharacteristicEntity, UUID>