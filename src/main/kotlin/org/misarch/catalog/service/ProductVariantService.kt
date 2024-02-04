package org.misarch.catalog.service

import kotlinx.coroutines.reactor.awaitSingle
import org.misarch.catalog.event.CatalogEvents
import org.misarch.catalog.event.EventPublisher
import org.misarch.catalog.graphql.input.CreateProductVariantInput
import org.misarch.catalog.graphql.input.ProductVariantInput
import org.misarch.catalog.graphql.input.UpdateProductVariantInput
import org.misarch.catalog.persistence.model.ProductEntity
import org.misarch.catalog.persistence.model.ProductVariantEntity
import org.misarch.catalog.persistence.model.ProductVariantVersionEntity
import org.misarch.catalog.persistence.repository.ProductRepository
import org.misarch.catalog.persistence.repository.ProductVariantRepository
import org.springframework.stereotype.Service
import java.util.*

/**
 * Service for [ProductVariantEntity]s
 *
 * @param repository the provided repository
 * @property productVariantVersionService service for [ProductVariantVersionEntity]s
 * @property productRepository repository for [ProductEntity]s
 * @property eventPublisher publisher for events
 */
@Service
class ProductVariantService(
    repository: ProductVariantRepository,
    private val productVariantVersionService: ProductVariantVersionService,
    private val productRepository: ProductRepository,
    private val eventPublisher: EventPublisher
) : BaseService<ProductVariantEntity, ProductVariantRepository>(repository) {

    /**
     * Creates a product variant, also creates the initial version
     * Also checks if the product exists
     *
     * @param input defines the product variant (and initial version) to be created
     * @return the created product variant
     */
    suspend fun createProductVariant(input: CreateProductVariantInput): ProductVariantEntity {
        if (!productRepository.existsById(input.productId).awaitSingle()) {
            throw IllegalArgumentException("Product with id ${input.productId} does not exist.")
        }
        val (productVariant, initialVersion) = createProductVariantInternal(input, input.productId)
        eventPublisher.publishEvent(CatalogEvents.PRODUCT_VARIANT_CREATED, productVariant.toEventDTO())
        eventPublisher.publishEvent(CatalogEvents.PRODUCT_VARIANT_VERSION_CREATED, initialVersion.toEventDTO())
        return productVariant
    }

    /**
     * Creates a product variant without checking if the product exists
     *
     * @param input defines the product variant (and initial version) to be created
     * @param productId the id of the product
     * @return the created product variant and the initial version
     */
    suspend fun createProductVariantInternal(input: ProductVariantInput, productId: UUID): Pair<ProductVariantEntity, ProductVariantVersionEntity> {
        val productVariant = ProductVariantEntity(
            isPubliclyVisible = input.isPubliclyVisible,
            productId = productId,
            currentVersion = null,
            id = null
        )
        val savedProductVariant = repository.save(productVariant).awaitSingle()
        val initialVersion = productVariantVersionService.createProductVariantVersionInternal(
            input.initialVersion,
            savedProductVariant.id!!
        )
        savedProductVariant.currentVersion = initialVersion.id!!
        val newSavedProductVariant = repository.save(savedProductVariant).awaitSingle()
        return Pair(newSavedProductVariant, initialVersion)
    }

    /**
     * Updates a product variant
     *
     * @param input defines the product to be updated
     * @return the updated product variant
     */
    suspend fun updateProductVariant(input: UpdateProductVariantInput): ProductVariantEntity {
        val productVariant = repository.findById(input.id).awaitSingle()
        if (input.isPubliclyVisible != null) {
            productVariant.isPubliclyVisible = input.isPubliclyVisible
        }
        val savedProductVariant = repository.save(productVariant).awaitSingle()
        return savedProductVariant
    }

}