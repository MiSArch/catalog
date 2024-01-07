package org.misarch.catalog.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.federation.directives.FieldSet
import com.expediagroup.graphql.generator.federation.directives.KeyDirective
import graphql.schema.DataFetchingEnvironment
import org.misarch.catalog.graphql.dataloader.ProductVariantDataLoader
import org.misarch.catalog.graphql.model.connection.CategoryCharacteristicValueConnection
import org.misarch.catalog.graphql.model.connection.CategoryCharacteristicValueOrder
import org.misarch.catalog.persistence.model.ProductVariantVersionEntity
import org.misarch.catalog.persistence.repository.CategoryCharacteristicValueRepository
import org.springframework.beans.factory.annotation.Autowired
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.CompletableFuture

@GraphQLDescription("A version of a ProductVariant.")
@KeyDirective(fields = FieldSet("id"))
class ProductVariantVersion(
    id: UUID,
    @GraphQLDescription("The name of the ProductVariantVersion.")
    val name: String,
    @GraphQLDescription("The description of the ProductVariantVersion.")
    val description: String,
    @GraphQLDescription("The version of the ProductVariantVersion.")
    val version: Int,
    @GraphQLDescription("The retail price of the ProductVariantVersion.")
    val retailPrice: Int,
    @GraphQLDescription("The date when the ProductVariantVersion version was created.")
    val createdAt: OffsetDateTime,
    @GraphQLDescription("The amount of days for which an instance of the ProductVariantVersion can be returned after purchase")
    val canBeReturnedForDays: Double?,
    private val productVariantId: UUID
) : Node(id) {

    @GraphQLDescription("The ProductVariant this is a version of.")
    fun productVariant(
        dfe: DataFetchingEnvironment
    ): CompletableFuture<ProductVariant> {
        return dfe.getDataLoader<UUID, ProductVariant>(ProductVariantDataLoader::class.simpleName!!)
            .load(productVariantId, dfe)
    }

    @GraphQLDescription("Get all associated CategoryCharacteristicValues")
    suspend fun characteristicValues(
        @GraphQLDescription("Number of items to return")
        first: Int? = null,
        @GraphQLDescription("Number of items to skip")
        skip: Int? = null,
        @GraphQLDescription("Ordering")
        orderBy: CategoryCharacteristicValueOrder? = null,
        @GraphQLIgnore
        @Autowired
        categoryCharacteristicValueRepository: CategoryCharacteristicValueRepository
    ): CategoryCharacteristicValueConnection {
        return CategoryCharacteristicValueConnection(
            first,
            skip,
            ProductVariantVersionEntity.ENTITY.productVariantId.eq(id),
            orderBy,
            categoryCharacteristicValueRepository
        )
    }

}