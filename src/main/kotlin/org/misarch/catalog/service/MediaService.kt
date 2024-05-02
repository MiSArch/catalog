package org.misarch.catalog.service

import org.misarch.catalog.event.model.CreateMediaDTO
import org.misarch.catalog.persistence.model.MediaEntity
import org.misarch.catalog.persistence.repository.MediaRepository
import org.springframework.stereotype.Service

/**
 * Service for [MediaEntity]s
 *
 * @param repository the provided repository
 */
@Service
class MediaService(
    repository: MediaRepository
) : BaseService<MediaEntity, MediaRepository>(repository) {

    /**
     * Registers a TaxRate
     *
     * @param mediaDTO the TaxRate to register
     */
    suspend fun registerMedia(mediaDTO: CreateMediaDTO) {
        repository.createMedia(mediaDTO.id)
    }

}