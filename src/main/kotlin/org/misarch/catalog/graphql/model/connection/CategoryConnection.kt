package org.misarch.catalog.graphql.model.connection

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.federation.directives.ShareableDirective
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.ComparableExpression
import com.querydsl.sql.SQLQuery
import org.misarch.catalog.graphql.model.Category
import org.misarch.catalog.graphql.model.connection.base.BaseConnection
import org.misarch.catalog.graphql.model.connection.base.BaseOrder
import org.misarch.catalog.graphql.model.connection.base.BaseOrderField
import org.misarch.catalog.graphql.model.connection.base.OrderDirection
import org.misarch.catalog.persistence.model.CategoryEntity
import org.misarch.catalog.persistence.repository.CategoryRepository
import org.misarch.user.graphql.AuthorizedUser

/**
 * A GraphQL connection for [Category]s.
 *
 * @param first The maximum number of items to return
 * @param skip The number of items to skip
 * @param predicate The predicate to filter the items by
 * @param order The order to sort the items by
 * @param repository The repository to fetch the items from
 * @param applyJoin A function to apply a join to the query
 */
@GraphQLDescription("A connection to a list of `Category` values.")
@ShareableDirective
class CategoryConnection(
    first: Int?,
    skip: Int?,
    predicate: BooleanExpression?,
    order: CategoryOrder?,
    repository: CategoryRepository,
    authorizedUser: AuthorizedUser?,
    applyJoin: (query: SQLQuery<*>) -> SQLQuery<*> = { it }
) : BaseConnection<Category, CategoryEntity>(
    first,
    skip,
    null,
    predicate,
    (order ?: CategoryOrder.DEFAULT).toOrderSpecifier(CategoryOrderField.ID),
    repository,
    CategoryEntity.ENTITY,
    authorizedUser,
    applyJoin
) {

    override val primaryKey: ComparableExpression<*> get() = CategoryEntity.ENTITY.id
}

@GraphQLDescription("Category order fields")
enum class CategoryOrderField(override vararg val expressions: Expression<out Comparable<*>>) : BaseOrderField {
    @GraphQLDescription("Order categories by their id")
    ID(CategoryEntity.ENTITY.id),

    @GraphQLDescription("Order categories by their name")
    NAME(CategoryEntity.ENTITY.name, CategoryEntity.ENTITY.id)
}

@GraphQLDescription("Category order")
class CategoryOrder(
    direction: OrderDirection?, field: CategoryOrderField?
) : BaseOrder<CategoryOrderField>(direction, field) {

    companion object {
        val DEFAULT = CategoryOrder(OrderDirection.ASC, CategoryOrderField.ID)
    }
}