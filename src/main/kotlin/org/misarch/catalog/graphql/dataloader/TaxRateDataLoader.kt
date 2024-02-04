package org.misarch.catalog.graphql.dataloader

import org.misarch.catalog.graphql.model.TaxRate
import org.misarch.catalog.persistence.model.TaxRateEntity
import org.misarch.catalog.persistence.repository.TaxRateRepository
import org.springframework.stereotype.Component

/**
 * Data loader for [TaxRate]s
 *
 * @param repository repository for [TaxRateEntity]s
 */
@Component
class TaxRateDataLoader(
    repository: TaxRateRepository
) : IdDataLoader<TaxRate, TaxRateEntity>(repository)