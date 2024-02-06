package org.misarch.catalog.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.federation.directives.FieldSet
import com.expediagroup.graphql.generator.federation.directives.KeyDirective
import graphql.schema.DataFetchingEnvironment
import org.misarch.catalog.graphql.dataloader.ProductVariantDataLoader
import org.misarch.catalog.graphql.model.connection.*
import org.misarch.catalog.persistence.model.CategoryEntity
import org.misarch.catalog.persistence.model.ProductToCategoryEntity
import org.misarch.catalog.persistence.model.ProductVariantEntity
import org.misarch.catalog.persistence.repository.CategoryRepository
import org.misarch.catalog.persistence.repository.ProductVariantRepository
import org.misarch.user.graphql.authorizedUser
import org.misarch.user.graphql.authorizedUserOrNull
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.concurrent.CompletableFuture

@GraphQLDescription("A product.")
@KeyDirective(fields = FieldSet("id"))
class Product(
    id: UUID,
    @property:GraphQLDescription("An internal name to identify the Product, not visible to customers.")
    val internalName: String,
    @property:GraphQLDescription("If true, the Product is visible to customers.")
    val isPubliclyVisible: Boolean, private val defaultVariantId: UUID
) : Node(id) {

    @GraphQLDescription("The default variant of the product.")
    fun defaultVariant(
        dfe: DataFetchingEnvironment
    ): CompletableFuture<ProductVariant> {
        if (!this.isPubliclyVisible) {
            dfe.authorizedUser.checkIsEmployee()
        }
        return dfe.getDataLoader<UUID, ProductVariant>(ProductVariantDataLoader::class.simpleName!!)
            .load(defaultVariantId, dfe)
    }

    @GraphQLDescription("Get all associated ProductVariants")
    suspend fun variants(
        @GraphQLDescription("Number of items to return")
        first: Int? = null,
        @GraphQLDescription("Number of items to skip")
        skip: Int? = null,
        @GraphQLDescription("Ordering")
        orderBy: ProductVariantOrder? = null,
        @GraphQLDescription("Filtering")
        filter: ProductVariantFilter? = null,
        @GraphQLIgnore
        @Autowired
        productVariantRepository: ProductVariantRepository,
        dfe: DataFetchingEnvironment
    ): ProductVariantConnection {
        if (!this.isPubliclyVisible) {
            dfe.authorizedUser.checkIsEmployee()
        }
        return ProductVariantConnection(
            first, skip, filter, ProductVariantEntity.ENTITY.productId.eq(id), orderBy, productVariantRepository, dfe.authorizedUserOrNull
        )
    }

    @GraphQLDescription("Get all associated Categories")
    suspend fun categories(
        @GraphQLDescription("Number of items to return")
        first: Int? = null,
        @GraphQLDescription("Number of items to skip")
        skip: Int? = null,
        @GraphQLDescription("Ordering")
        orderBy: CategoryOrder? = null,
        @GraphQLIgnore
        @Autowired
        categoryRepository: CategoryRepository,
        dfe: DataFetchingEnvironment
    ): CategoryConnection {
        return CategoryConnection(
            first, skip, ProductToCategoryEntity.ENTITY.productId.eq(id), orderBy, categoryRepository, dfe.authorizedUserOrNull
        ) {
            it.innerJoin(ProductToCategoryEntity.ENTITY)
                .on(ProductToCategoryEntity.ENTITY.categoryId.eq(CategoryEntity.ENTITY.id))
        }
    }

}