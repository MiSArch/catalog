package org.misarch.catalog.graphql.dataloader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.extensions.get
import graphql.GraphQLContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.reactor.awaitSingle
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import org.misarch.catalog.persistence.model.BaseEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Base class for data loaders which load entities by their id (with batch loading)
 *
 * @param T type of the DTO
 * @param D type of the entity
 * @property repository repository to load the entities from
 */
abstract class IdDataLoader<T, D : BaseEntity<T>>(private val repository: ReactiveCrudRepository<D, UUID>) :
    KotlinDataLoader<UUID, T> {
    override val dataLoaderName: String
        get() = this::class.simpleName!!

    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<UUID, T> {
        return DataLoaderFactory.newDataLoader({ ids, batchLoaderEnvironment ->
            val contextScope = batchLoaderEnvironment.getContext<GraphQLContext>()?.get<CoroutineScope>()
            val coroutineScope = contextScope ?: CoroutineScope(EmptyCoroutineContext)
            coroutineScope.future {
                repository.findAllById(ids).collectList().awaitSingle().map { it.toDTO() }.toList()
            }
        }, DataLoaderOptions.newOptions().setBatchLoaderContextProvider { graphQLContext })
    }
}