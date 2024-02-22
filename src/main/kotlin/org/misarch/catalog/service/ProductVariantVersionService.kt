package org.misarch.catalog.service

import kotlinx.coroutines.reactor.awaitSingle
import org.misarch.catalog.event.CatalogEvents
import org.misarch.catalog.event.EventPublisher
import org.misarch.catalog.graphql.input.CreateProductVariantVersionInput
import org.misarch.catalog.graphql.input.ProductVariantVersionInput
import org.misarch.catalog.persistence.model.CategoryCharacteristicValueEntity
import org.misarch.catalog.persistence.model.ProductVariantEntity
import org.misarch.catalog.persistence.model.ProductVariantVersionEntity
import org.misarch.catalog.persistence.model.TaxRateEntity
import org.misarch.catalog.persistence.repository.ProductVariantRepository
import org.misarch.catalog.persistence.repository.ProductVariantVersionRepository
import org.misarch.catalog.persistence.repository.TaxRateRepository
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

/**
 * Service for [ProductVariantVersionEntity]s
 *
 * @param repository the provided repository
 * @property productVariantRepository repository for [ProductVariantEntity]s
 * @property taxRateRepository repository for [TaxRateEntity]s
 * @property categoryCharacteristicValueService service for [CategoryCharacteristicValueEntity]s
 * @property eventPublisher publisher for events
 */
@Service
class ProductVariantVersionService(
    repository: ProductVariantVersionRepository,
    private val productVariantRepository: ProductVariantRepository,
    private val taxRateRepository: TaxRateRepository,
    private val categoryCharacteristicValueService: CategoryCharacteristicValueService,
    private val eventPublisher: EventPublisher
) : BaseService<ProductVariantVersionEntity, ProductVariantVersionRepository>(repository) {

    /**
     * Creates a product variant version
     * Also checks if the product variant exists
     *
     * @param input defines the product variant version to be created
     */
    suspend fun createProductVariantVersion(
        input: CreateProductVariantVersionInput
    ): ProductVariantVersionEntity {
        if (!productVariantRepository.existsById(input.productVariantId).awaitSingle()) {
            throw IllegalArgumentException("Product variant with id ${input.productVariantId} does not exist.")
        }
        val productVariantVersion = createProductVariantVersionInternal(input, input.productVariantId)
        val productVariant = productVariantRepository.findById(input.productVariantId).awaitSingle()
        productVariant.currentVersion = productVariantVersion.id!!
        productVariantRepository.save(productVariant).awaitSingle()
        eventPublisher.publishEvent(CatalogEvents.PRODUCT_VARIANT_VERSION_CREATED, productVariantVersion.toEventDTO())
        return productVariantVersion
    }

    /**
     * Creates a product variant version without checking if the product variant exists
     *
     * @param input defines the product variant version to be created
     * @param productVariantId the id of the product variant
     * @return the created product variant version
     */
    suspend fun createProductVariantVersionInternal(
        input: ProductVariantVersionInput, productVariantId: UUID
    ): ProductVariantVersionEntity {
        if (!taxRateRepository.existsById(input.taxRateId).awaitSingle()) {
            throw IllegalArgumentException("Tax rate with id ${input.taxRateId} does not exist")
        }
        val version = repository.findMaxVersionByProductVariantId(productVariantId)?.plus(1) ?: 1
        val productVariantVersion = ProductVariantVersionEntity(
            name = input.name,
            description = input.description,
            version = version,
            retailPrice = input.retailPrice,
            createdAt = OffsetDateTime.now(),
            canBeReturnedForDays = input.canBeReturnedForDays,
            productVariantId = productVariantId,
            taxRateId = input.taxRateId,
            weight = input.weight,
            id = null
        )
        val savedProductVariantVersion = repository.save(productVariantVersion).awaitSingle()
        categoryCharacteristicValueService.upsertCategoryCharacteristicValues(
            input.categoricalCharacteristicValues,
            input.numericalCharacteristicValues,
            savedProductVariantVersion.id!!
        )
        return savedProductVariantVersion
    }

}