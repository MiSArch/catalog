package org.misarch.catalog.graphql.model.connection

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.federation.directives.ShareableDirective
import com.querydsl.core.types.*
import com.querydsl.core.types.dsl.Expressions
import kotlinx.coroutines.reactor.awaitSingle
import org.misarch.catalog.graphql.model.CategoricalCategoryCharacteristicValue
import org.misarch.catalog.graphql.model.CategoryCharacteristic
import org.misarch.catalog.graphql.model.connection.base.BaseOrder
import org.misarch.catalog.graphql.model.connection.base.BaseOrderField
import org.misarch.catalog.graphql.model.connection.base.OrderDirection
import org.misarch.catalog.persistence.model.CategoryCharacteristicValueEntity
import org.misarch.catalog.persistence.repository.CategoryCharacteristicValueRepository
import java.util.*

/**
 * A GraphQL connection for [CategoryCharacteristic]s.
 *
 * @param first The maximum number of items to return
 * @param skip The number of items to skip
 * @param order The order to sort the items by
 * @param repository The repository to fetch the items from
 * @param applyJoin A function to apply a join to the query
 */
@GraphQLDescription("A connection to a list of `CategoricalCategoryCharacteristicValue` values.")
@ShareableDirective
class CategoricalCategoryCharacteristicValueConnection(
    private val first: Int?,
    private val skip: Int?,
    private val order: CategoricalCategoryCharacteristicValueOrder?,
    private val repository: CategoryCharacteristicValueRepository,
    private val characteristicId: UUID
) {

    companion object {
        val ENTITY = CategoryCharacteristicValueEntity.ENTITY
    }

    @GraphQLDescription("The total amount of items in this connection")
    suspend fun totalCount(): Int {
        return repository.query {
            it.select(
                Expressions.numberOperation(
                    Long::class.javaObjectType, Ops.AggOps.COUNT_DISTINCT_AGG, ENTITY.stringValue
                )
            ).from(ENTITY).where(
                ENTITY.stringValue.isNotNull.and(ENTITY.categoryCharacteristicId.eq(characteristicId))
            )
        }.first().awaitSingle().toInt()
    }

    @GraphQLDescription("The resulting items.")
    suspend fun nodes(): List<CategoricalCategoryCharacteristicValue> {
        return repository.query {
            it.select(ENTITY.stringValue).distinct().from(ENTITY).where(
                ENTITY.stringValue.isNotNull.and(ENTITY.categoryCharacteristicId.eq(characteristicId))
            ).orderBy(
                *(order ?: CategoricalCategoryCharacteristicValueOrder.DEFAULT).toOrderSpecifier(
                    CategoricalCategoryCharacteristicValueOrderField.VALUE
                )
            ).offset(skip?.toLong() ?: 0).limit(first?.toLong() ?: Long.MAX_VALUE)
        }.all().collectList().awaitSingle().map { CategoricalCategoryCharacteristicValue(characteristicId, it) }
    }

    @GraphQLDescription("Whether this connection has a next page")
    suspend fun hasNextPage(): Boolean {
        if (first == null) {
            return false
        }
        return repository.query {
            it.select(ENTITY.stringValue).distinct().from(ENTITY).where(
                ENTITY.stringValue.isNotNull.and(ENTITY.categoryCharacteristicId.eq(characteristicId))
            ).offset(first.toLong() + (skip ?: 0)).limit(1)
        }.all().hasElements().awaitSingle()
    }
}

@GraphQLDescription("CategoricalCategoryCharacteristicValue order fields")
enum class CategoricalCategoryCharacteristicValueOrderField(
    override vararg val expressions: Expression<out Comparable<*>>
) : BaseOrderField {
    @GraphQLDescription("Order CategoricalCategoryCharacteristicValue by their id")
    VALUE(CategoryCharacteristicValueEntity.ENTITY.stringValue)
}

@GraphQLDescription("CategoricalCategoryCharacteristicValue order")
class CategoricalCategoryCharacteristicValueOrder(
    direction: OrderDirection?, field: CategoricalCategoryCharacteristicValueOrderField?
) : BaseOrder<CategoricalCategoryCharacteristicValueOrderField>(direction, field) {

    companion object {
        val DEFAULT = CategoricalCategoryCharacteristicValueOrder(
            OrderDirection.ASC, CategoricalCategoryCharacteristicValueOrderField.VALUE
        )
    }
}