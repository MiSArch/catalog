package org.misarch.catalog.graphql.model.connection

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.federation.directives.ShareableDirective
import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.ComparableExpression
import com.querydsl.sql.SQLQuery
import org.misarch.catalog.graphql.model.ProductVariantVersion
import org.misarch.catalog.graphql.model.connection.base.BaseConnection
import org.misarch.catalog.graphql.model.connection.base.BaseOrder
import org.misarch.catalog.graphql.model.connection.base.BaseOrderField
import org.misarch.catalog.graphql.model.connection.base.OrderDirection
import org.misarch.catalog.persistence.model.ProductVariantVersionEntity
import org.misarch.catalog.persistence.repository.ProductVariantVersionRepository
import org.misarch.catalog.graphql.AuthorizedUser

/**
 * A GraphQL connection for [ProductVariantVersion]s.
 *
 * @param first The maximum number of items to return
 * @param skip The number of items to skip
 * @param predicate The predicate to filter the items by
 * @param order The order to sort the items by
 * @param repository The repository to fetch the items from
 * @param authorizedUser The authorized user
 * @param applyJoin A function to apply a join to the query
 */
@GraphQLDescription("A connection to a list of `ProductVariantVersion` values.")
@ShareableDirective
class ProductVariantVersionConnection(
    first: Int?,
    skip: Int?,
    predicate: BooleanExpression?,
    order: ProductVariantVersionOrder?,
    repository: ProductVariantVersionRepository,
    authorizedUser: AuthorizedUser?,
    applyJoin: (query: SQLQuery<*>) -> SQLQuery<*> = { it }
) : BaseConnection<ProductVariantVersion, ProductVariantVersionEntity>(
    first,
    skip,
    null,
    predicate,
    (order ?: ProductVariantVersionOrder.DEFAULT).toOrderSpecifier(ProductVariantVersionOrderField.ID),
    repository,
    ProductVariantVersionEntity.ENTITY,
    authorizedUser,
    applyJoin
) {

    override val primaryKey: ComparableExpression<*> get() = ProductVariantVersionEntity.ENTITY.id
}

@GraphQLDescription("ProductVariantVersion order fields")
enum class ProductVariantVersionOrderField(override vararg val expressions: Expression<out Comparable<*>>) :
    BaseOrderField {
    @GraphQLDescription("Order productVariantVersions by their id")
    ID(ProductVariantVersionEntity.ENTITY.id),

    @GraphQLDescription("Order productVariantVersions by their version")
    VERSION(ProductVariantVersionEntity.ENTITY.version, ProductVariantVersionEntity.ENTITY.id),

    @GraphQLDescription("Order productVariantVersions by their creation date")
    CREATED_AT(ProductVariantVersionEntity.ENTITY.createdAt, ProductVariantVersionEntity.ENTITY.id)
}

@GraphQLDescription("ProductVariantVersion order")
class ProductVariantVersionOrder(
    direction: OrderDirection?, field: ProductVariantVersionOrderField?
) : BaseOrder<ProductVariantVersionOrderField>(direction, field) {

    companion object {
        val DEFAULT = ProductVariantVersionOrder(OrderDirection.ASC, ProductVariantVersionOrderField.ID)
    }
}