package org.misarch.catalog.service

import kotlinx.coroutines.reactor.awaitSingle
import org.misarch.catalog.event.CatalogEvents
import org.misarch.catalog.event.EventPublisher
import org.misarch.catalog.graphql.input.CreateProductInput
import org.misarch.catalog.graphql.input.UpdateProductInput
import org.misarch.catalog.persistence.model.CategoryEntity
import org.misarch.catalog.persistence.model.ProductEntity
import org.misarch.catalog.persistence.model.ProductToCategoryEntity
import org.misarch.catalog.persistence.model.ProductVariantEntity
import org.misarch.catalog.persistence.repository.CategoryRepository
import org.misarch.catalog.persistence.repository.ProductRepository
import org.misarch.catalog.persistence.repository.ProductToCategoryRepository
import org.misarch.catalog.persistence.repository.ProductVariantRepository
import org.springframework.stereotype.Service
import java.util.*

/**
 * Service for [ProductEntity]s
 *
 * @param repository the provided repository
 * @property productVariantService service for [ProductVariantEntity]s
 * @property productToCategoryRepository repository for [ProductToCategoryEntity]s
 * @property categoryRepository repository for [CategoryEntity]s
 * @property productVariantRepository repository for [ProductVariantEntity]s
 * @property eventPublisher publisher for events
 */
@Service
class ProductService(
    repository: ProductRepository,
    private val productVariantService: ProductVariantService,
    private val productToCategoryRepository: ProductToCategoryRepository,
    private val categoryRepository: CategoryRepository,
    private val productVariantRepository: ProductVariantRepository,
    private val eventPublisher: EventPublisher,
) : BaseService<ProductEntity, ProductRepository>(repository) {

    /**
     * Creates a product, also creates the default variant (and their versions) and adds the categories
     *
     * @param input defines the product (and default variant) to be created
     * @return the created product
     */
    suspend fun createProduct(input: CreateProductInput): ProductEntity {
        val product = ProductEntity(
            internalName = input.internalName,
            isPubliclyVisible = input.isPubliclyVisible,
            defaultVariantId = null,
            id = null
        )
        val savedProduct = repository.save(product).awaitSingle()
        addCategories(savedProduct, input.categoryIds)
        val (productVariant, initialVersion) = productVariantService.createProductVariantInternal(input.defaultVariant, savedProduct.id!!)
        savedProduct.defaultVariantId = productVariant.id
        val newSavedProduct =  repository.save(savedProduct).awaitSingle()
        eventPublisher.publishEvent(CatalogEvents.PRODUCT_CREATED, newSavedProduct.toEventDTO(input.categoryIds.toSet()))
        eventPublisher.publishEvent(CatalogEvents.PRODUCT_VARIANT_CREATED, productVariant.toEventDTO())
        eventPublisher.publishEvent(CatalogEvents.PRODUCT_VARIANT_VERSION_CREATED, initialVersion.toEventDTO())
        return newSavedProduct
    }

    /**
     * Updates a product
     *
     * @param input defines the product to be updated
     * @return the updated product
     */
    suspend fun updateProduct(input: UpdateProductInput): ProductEntity {
        val product = repository.findById(input.id).awaitSingle()
        if (input.isPubliclyVisible != null) {
            product.isPubliclyVisible = input.isPubliclyVisible
        }
        if (input.internalName != null) {
            product.internalName = input.internalName
        }
        if (input.defaultVariantId != null) {
            val variant = productVariantRepository.findById(input.defaultVariantId).awaitSingle()
            if (variant.productId != product.id) {
                throw IllegalArgumentException("Variant with id ${input.defaultVariantId} does not belong to product with id ${product.id}.")
            }
            product.defaultVariantId = input.defaultVariantId
        }
        val savedProduct = repository.save(product).awaitSingle()
        val categoryIds = productToCategoryRepository.findByProductId(savedProduct.id!!).map { it.categoryId }
        eventPublisher.publishEvent(CatalogEvents.PRODUCT_UPDATED, savedProduct.toEventDTO(categoryIds.toSet()))
        return savedProduct
    }

    /**
     * Adds categories to a product
     *
     * @param product the product where the categories should be added
     * @param categoryIds the ids of the categories
     */
    private suspend fun addCategories(product: ProductEntity, categoryIds: List<UUID>) {
        checkCategoriesExist(categoryIds)
        productToCategoryRepository.saveAll(
            categoryIds.map { categoryId ->
                ProductToCategoryEntity(
                    productId = product.id!!,
                    categoryId = categoryId,
                    id = null
                )
            }
        ).collectList().awaitSingle()
    }

    /**
     * Checks if the categories exist
     *
     * @param categoryIds the ids of the categories
     * @throws IllegalArgumentException if a category does not exist
     */
    private suspend fun checkCategoriesExist(categoryIds: List<UUID>) {
        categoryIds.forEach { categoryId ->
            if (!categoryRepository.existsById(categoryId).awaitSingle()) {
                throw IllegalArgumentException("Category with id $categoryId does not exist.")
            }
        }
    }

}