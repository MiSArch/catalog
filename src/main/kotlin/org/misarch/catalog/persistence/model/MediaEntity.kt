package org.misarch.catalog.persistence.model

import org.misarch.catalog.graphql.model.Media
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table
class MediaEntity(
    @Id
    override val id: UUID
) : BaseEntity<Media> {

    companion object {
        /**
         * Querydsl entity
         */
        val ENTITY = QMediaEntity.mediaEntity!!
    }

    override fun toDTO(): Media {
        return Media(id)
    }
}