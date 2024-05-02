package org.misarch.catalog.persistence.repository

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.misarch.catalog.persistence.model.MediaEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for [MediaEntity]s
 */
@Repository
interface MediaRepository : QuerydslR2dbcRepository<MediaEntity, UUID> {

    @Modifying
    @Query("INSERT INTO MediaEntity (id) VALUES (:id)")
    suspend fun createMedia(
        @Param("id") id: UUID
    )

}