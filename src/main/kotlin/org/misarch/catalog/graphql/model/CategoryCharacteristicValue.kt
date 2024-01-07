package org.misarch.catalog.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.reactor.awaitSingle
import org.misarch.catalog.graphql.dataloader.CategoryCharacteristicDataLoader
import org.misarch.catalog.graphql.dataloader.ProductVariantVersionDataLoader
import org.misarch.catalog.persistence.repository.CategoryCharacteristicRepository
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.concurrent.CompletableFuture

@GraphQLDescription("A value for a CategoryCharacteristic.")
abstract class CategoryCharacteristicValue(
    private val characteristicId: UUID
) {

    @GraphQLDescription("The associated CategoryCharacteristic this is a value of.")
    fun characteristic(
        dfe: DataFetchingEnvironment
    ): CompletableFuture<CategoryCharacteristic> {
        return dfe.getDataLoader<UUID, CategoryCharacteristic>(CategoryCharacteristicDataLoader::class.simpleName!!)
            .load(characteristicId, dfe)
    }

}