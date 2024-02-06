package org.misarch.catalog.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.federation.directives.FieldSet
import com.expediagroup.graphql.generator.federation.directives.KeyDirective
import graphql.schema.DataFetchingEnvironment
import org.misarch.catalog.graphql.model.connection.*
import org.misarch.catalog.persistence.model.CategoryCharacteristicEntity
import org.misarch.catalog.persistence.model.ProductEntity
import org.misarch.catalog.persistence.model.ProductToCategoryEntity
import org.misarch.catalog.persistence.repository.CategoryCharacteristicRepository
import org.misarch.catalog.persistence.repository.ProductRepository
import org.misarch.user.graphql.authorizedUserOrNull
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@GraphQLDescription("A category")
@KeyDirective(fields = FieldSet("id"))
class Category(
    id: UUID,
    @property:GraphQLDescription("The name of the category.")
    val name: String,
    @property:GraphQLDescription("The description of the category.")
    val description: String,
) : Node(id) {

    @GraphQLDescription("Get all associated products")
    suspend fun products(
        @GraphQLDescription("Number of items to return")
        first: Int? = null,
        @GraphQLDescription("Number of items to skip")
        skip: Int? = null,
        @GraphQLDescription("Ordering")
        orderBy: ProductOrder? = null,
        @GraphQLDescription("Filtering")
        filter: ProductFilter? = null,
        @GraphQLIgnore
        @Autowired
        productRepository: ProductRepository,
        dfe: DataFetchingEnvironment
    ): ProductConnection {
        return ProductConnection(
            first, skip, filter, ProductToCategoryEntity.ENTITY.categoryId.eq(id), orderBy, productRepository, dfe.authorizedUserOrNull
        ) {
            it.innerJoin(ProductToCategoryEntity.ENTITY)
                .on(ProductToCategoryEntity.ENTITY.productId.eq(ProductEntity.ENTITY.id))
        }
    }

    @GraphQLDescription("Get characteristics for the category")
    suspend fun characteristics(
        @GraphQLDescription("Number of items to return")
        first: Int? = null,
        @GraphQLDescription("Number of items to skip")
        skip: Int? = null,
        @GraphQLDescription("Ordering")
        orderBy: CategoryCharacteristicOrder? = null,
        @GraphQLIgnore
        @Autowired
        categoryCharacteristicsRepository: CategoryCharacteristicRepository,
        dfe: DataFetchingEnvironment
    ): CategoryCharacteristicConnection {
        return CategoryCharacteristicConnection(
            first,
            skip,
            CategoryCharacteristicEntity.ENTITY.categoryId.eq(id),
            orderBy,
            categoryCharacteristicsRepository,
            dfe.authorizedUserOrNull
        )
    }

}