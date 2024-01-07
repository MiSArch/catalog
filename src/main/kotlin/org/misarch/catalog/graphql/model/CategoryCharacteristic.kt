package org.misarch.catalog.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.reactor.awaitSingle
import org.misarch.catalog.graphql.dataloader.CategoryCharacteristicDataLoader
import org.misarch.catalog.graphql.dataloader.CategoryDataLoader
import org.misarch.catalog.persistence.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.concurrent.CompletableFuture

@GraphQLDescription("A characteristic of a Category.")
abstract class CategoryCharacteristic(
    id: UUID,
    @GraphQLDescription("The name of the CategoryCharacteristic.")
    val name: String,
    @GraphQLDescription("The description of the CategoryCharacteristic.")
    val description: String, private val categoryId: UUID
) : Node(id) {

    @GraphQLDescription("The Category this item belongs to.")
    fun category(
        dfe: DataFetchingEnvironment
    ): CompletableFuture<Category> {
        return dfe.getDataLoader<UUID, Category>(CategoryDataLoader::class.simpleName!!)
            .load(categoryId, dfe)
    }

}