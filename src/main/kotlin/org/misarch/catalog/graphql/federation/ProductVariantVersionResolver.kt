package org.misarch.catalog.graphql.federation

import com.expediagroup.graphql.generator.federation.execution.FederatedTypePromiseResolver
import graphql.schema.DataFetchingEnvironment
import org.misarch.catalog.graphql.dataloader.ProductVariantVersionDataLoader
import org.misarch.catalog.graphql.model.ProductVariantVersion
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Federated resolver for [ProductVariantVersion]s.
 */
@Component
class ProductVariantVersionResolver : FederatedTypePromiseResolver<ProductVariantVersion> {
    override val typeName: String
        get() = ProductVariantVersion::class.simpleName!!

    override fun resolve(
        environment: DataFetchingEnvironment, representation: Map<String, Any>
    ): CompletableFuture<ProductVariantVersion?> {
        val id = representation["id"] as String?
        return if (id == null) {
            CompletableFuture.completedFuture(null)
        } else {
            environment.getDataLoader<UUID, ProductVariantVersion>(ProductVariantVersionDataLoader::class.simpleName!!)
                .load(UUID.fromString(id))
        }
    }
}