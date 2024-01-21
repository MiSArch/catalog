package org.misarch.catalog.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.federation.directives.FieldSet
import com.expediagroup.graphql.generator.federation.directives.KeyDirective
import org.misarch.catalog.graphql.model.connection.CategoricalCategoryCharacteristicValueConnection
import org.misarch.catalog.graphql.model.connection.CategoricalCategoryCharacteristicValueOrder
import org.misarch.catalog.graphql.model.connection.ProductVariantVersionConnection
import org.misarch.catalog.graphql.model.connection.ProductVariantVersionOrder
import org.misarch.catalog.persistence.model.ProductVariantVersionEntity
import org.misarch.catalog.persistence.repository.CategoryCharacteristicValueRepository
import org.misarch.catalog.persistence.repository.ProductVariantVersionRepository
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@GraphQLDescription("A categorical characteristic of a category.")
@KeyDirective(fields = FieldSet("id"))
class CategoricalCategoryCharacteristic(
    id: UUID,
    name: String,
    description: String,
    categoryId: UUID
) : CategoryCharacteristic(id, name, description, categoryId) {

    @GraphQLDescription("Get all associated values")
    suspend fun values(
        @GraphQLDescription("Number of items to return")
        first: Int? = null,
        @GraphQLDescription("Number of items to skip")
        skip: Int? = null,
        @GraphQLDescription("Ordering")
        orderBy: CategoricalCategoryCharacteristicValueOrder? = null,
        @GraphQLIgnore
        @Autowired
        repository: CategoryCharacteristicValueRepository
    ): CategoricalCategoryCharacteristicValueConnection {
        return CategoricalCategoryCharacteristicValueConnection(
            first,
            skip,
            orderBy,
            repository,
            id
        )
    }

}