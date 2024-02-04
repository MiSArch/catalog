package org.misarch.catalog.event

/**
 * Constants for catalog event topics
 */
object CatalogEvents {
    /**
     * Topic for product creation events
     */
    const val PRODUCT_CREATED = "product/created"

    /**
     * Topic for product variant creation events
     */
    const val PRODUCT_VARIANT_CREATED = "product-variant/created"

    /**
     * Topic for product variant version creation events
     */
    const val PRODUCT_VARIANT_VERSION_CREATED = "product-variant-version/created"

    /**
     * Topic for tax rate creation events
     */
    const val TAX_RATE_CREATED = "tax/tax-rate/created"

    /**
     * Name of the pubsub component
     */
    const val PUBSUB_NAME = "pubsub"
}