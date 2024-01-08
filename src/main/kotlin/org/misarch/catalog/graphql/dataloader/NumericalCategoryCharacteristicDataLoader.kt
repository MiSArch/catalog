package org.misarch.catalog.graphql.dataloader

import org.misarch.catalog.graphql.model.NumericalCategoryCharacteristic
import org.misarch.catalog.persistence.model.CategoryCharacteristicEntity
import org.misarch.catalog.persistence.repository.CategoryCharacteristicRepository
import org.springframework.stereotype.Component

/**
 * Data loader for [NumericalCategoryCharacteristic]s
 *
 * @param repository repository for [CategoryCharacteristicEntity]s
 */
@Component
class NumericalCategoryCharacteristicDataLoader(
    repository: CategoryCharacteristicRepository
) : BaseIdDataLoader<NumericalCategoryCharacteristic, CategoryCharacteristicEntity>(repository) {
    override fun toDTO(entity: CategoryCharacteristicEntity): NumericalCategoryCharacteristic {
        return entity.toDTO() as NumericalCategoryCharacteristic
    }
}