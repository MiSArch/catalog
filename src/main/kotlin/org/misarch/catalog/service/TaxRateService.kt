package org.misarch.catalog.service

import org.misarch.catalog.event.model.CreateTaxRateDTO
import org.misarch.catalog.persistence.model.TaxRateEntity
import org.misarch.catalog.persistence.repository.TaxRateRepository
import org.springframework.stereotype.Service

/**
 * Service for [TaxRateEntity]s
 *
 * @param repository the provided repository
 */
@Service
class TaxRateService(
    repository: TaxRateRepository
) : BaseService<TaxRateEntity, TaxRateRepository>(repository) {

    /**
     * Registers a TaxRate
     *
     * @param taxRateDTO the TaxRate to register
     */
    suspend fun registerUser(taxRateDTO: CreateTaxRateDTO) {
        repository.createTaxRate(taxRateDTO.id)
    }

}