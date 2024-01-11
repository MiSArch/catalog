package org.misarch.catalog.event

/**
 * Constants for catalog event topics
 */
object CatalogEvents {
    /**
     * Topic for product creation events
     */
    val PRODUCT_CREATE = "product/create"

    /**
     * Topic for product variant creation events
     */
    val PRODUCT_VARIANT_CREATE = "product-variant/create"

    /**
     * Topic for product variant version creation events
     */
    val PRODUCT_VARIANT_VERSION_CREATE = "product-variant-version/create"
}