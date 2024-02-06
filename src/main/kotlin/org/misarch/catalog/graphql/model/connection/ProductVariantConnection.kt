package org.misarch.catalog.graphql.model.connection

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.federation.directives.ShareableDirective
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.ComparableExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.sql.SQLQuery
import org.misarch.catalog.graphql.model.ProductVariant
import org.misarch.catalog.graphql.model.connection.base.*
import org.misarch.catalog.persistence.model.ProductEntity
import org.misarch.catalog.persistence.model.ProductVariantEntity
import org.misarch.catalog.persistence.repository.ProductVariantRepository
import org.misarch.user.graphql.AuthorizedUser

/**
 * A GraphQL connection for [ProductVariant]s.
 *
 * @param first The maximum number of items to return
 * @param skip The number of items to skip
 * @param filter The filter to apply to the items
 * @param predicate The predicate to filter the items by
 * @param order The order to sort the items by
 * @param repository The repository to fetch the items from
 * @param authorizedUser The authorized user
 * @param applyJoin A function to apply a join to the query
 */
@GraphQLDescription("A connection to a list of `ProductVariant` values.")
@ShareableDirective
class ProductVariantConnection(
    first: Int?,
    skip: Int?,
    filter: ProductVariantFilter?,
    predicate: BooleanExpression?,
    order: ProductVariantOrder?,
    repository: ProductVariantRepository,
    authorizedUser: AuthorizedUser?,
    applyJoin: (query: SQLQuery<*>) -> SQLQuery<*> = { it }
) : BaseConnection<ProductVariant, ProductVariantEntity>(
    first,
    skip,
    filter,
    predicate,
    (order ?: ProductVariantOrder.DEFAULT).toOrderSpecifier(ProductVariantOrderField.ID),
    repository,
    ProductVariantEntity.ENTITY,
    authorizedUser,
    applyJoin
) {

    override val primaryKey: ComparableExpression<*> get() = ProductVariantEntity.ENTITY.id

    override fun authorizedUserFilter(): BooleanExpression? {
        return if (authorizedUser?.isEmployee == true) {
            null
        } else {
            ProductEntity.ENTITY.isPubliclyVisible.eq(Expressions.TRUE)
        }
    }
}

@GraphQLDescription("ProductVariant order fields")
enum class ProductVariantOrderField(override vararg val expressions: Expression<out Comparable<*>>) : BaseOrderField {
    @GraphQLDescription("Order productVariants by their id")
    ID(ProductVariantEntity.ENTITY.id)
}

@GraphQLDescription("ProductVariant order")
class ProductVariantOrder(
    direction: OrderDirection?, field: ProductVariantOrderField?
) : BaseOrder<ProductVariantOrderField>(direction, field) {

    companion object {
        val DEFAULT = ProductVariantOrder(OrderDirection.ASC, ProductVariantOrderField.ID)
    }
}

@GraphQLDescription("Product variant filter")
class ProductVariantFilter(
    val isPubliclyVisible: Boolean?
) : BaseFilter {

    override fun toExpression(): BooleanExpression? {
        return if (isPubliclyVisible != null) {
            ProductVariantEntity.ENTITY.isPubliclyVisible.eq(isPubliclyVisible)
        } else {
            null
        }
    }
}