package org.misarch.catalog.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.federation.directives.FieldSet
import com.expediagroup.graphql.generator.federation.directives.KeyDirective
import graphql.schema.DataFetchingEnvironment
import org.misarch.catalog.graphql.dataloader.ProductDataLoader
import org.misarch.catalog.graphql.dataloader.ProductVariantVersionDataLoader
import org.misarch.catalog.graphql.model.connection.ProductVariantVersionConnection
import org.misarch.catalog.graphql.model.connection.ProductVariantVersionOrder
import org.misarch.catalog.persistence.model.ProductVariantVersionEntity
import org.misarch.catalog.persistence.repository.ProductVariantVersionRepository
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.concurrent.CompletableFuture

@GraphQLDescription("A variant of a Product.")
@KeyDirective(fields = FieldSet("id"))
class ProductVariant(
    id: UUID,
    @property:GraphQLDescription("If true, the ProductVariant is visible to customers.")
    val isPubliclyVisible: Boolean, private val productId: UUID, private val currentVersion: UUID
) : Node(id) {

    @GraphQLDescription("The Product belonging to this variant.")
    fun product(
        dfe: DataFetchingEnvironment
    ): CompletableFuture<Product> {
        return dfe.getDataLoader<UUID, Product>(ProductDataLoader::class.simpleName!!)
            .load(productId, dfe)
    }

    @GraphQLDescription("The current version of the ProductVariant.")
    fun currentVersion(
        dfe: DataFetchingEnvironment
    ): CompletableFuture<ProductVariantVersion> {
        return dfe.getDataLoader<UUID, ProductVariantVersion>(ProductVariantVersionDataLoader::class.simpleName!!)
            .load(currentVersion, dfe)
    }

    @GraphQLDescription("Get all associated versions")
    suspend fun versions(
        @GraphQLDescription("Number of items to return")
        first: Int? = null,
        @GraphQLDescription("Number of items to skip")
        skip: Int? = null,
        @GraphQLDescription("Ordering")
        orderBy: ProductVariantVersionOrder? = null,
        @GraphQLIgnore
        @Autowired
        productVariantVersionRepository: ProductVariantVersionRepository
    ): ProductVariantVersionConnection {
        return ProductVariantVersionConnection(
            first,
            skip,
            ProductVariantVersionEntity.ENTITY.productVariantId.eq(id),
            orderBy,
            productVariantVersionRepository
        )
    }

}