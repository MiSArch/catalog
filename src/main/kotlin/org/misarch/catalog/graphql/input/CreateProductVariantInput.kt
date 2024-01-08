package org.misarch.catalog.graphql.input

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID

@GraphQLDescription("Input for the createProductVariant mutation")
class CreateProductVariantInput(
    @property:GraphQLDescription("The id of the Product this ProductVariant belongs to.")
    val productId: ID,
    isPubliclyVisible: Boolean,
    initialVersion: ProductVariantVersionInput
) : ProductVariantInput(isPubliclyVisible, initialVersion)