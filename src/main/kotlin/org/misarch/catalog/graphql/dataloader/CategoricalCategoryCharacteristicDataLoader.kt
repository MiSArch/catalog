package org.misarch.catalog.graphql.dataloader

import org.misarch.catalog.graphql.model.CategoricalCategoryCharacteristic
import org.misarch.catalog.persistence.model.CategoryCharacteristicEntity
import org.misarch.catalog.persistence.repository.CategoryCharacteristicRepository
import org.springframework.stereotype.Component

/**
 * Data loader for [CategoricalCategoryCharacteristic]s
 *
 * @param repository repository for [CategoryCharacteristicEntity]s
 */
@Component
class CategoricalCategoryCharacteristicDataLoader(
    repository: CategoryCharacteristicRepository
) : BaseIdDataLoader<CategoricalCategoryCharacteristic, CategoryCharacteristicEntity>(repository) {
    override fun toDTO(entity: CategoryCharacteristicEntity): CategoricalCategoryCharacteristic {
        return entity.toDTO() as CategoricalCategoryCharacteristic
    }
}