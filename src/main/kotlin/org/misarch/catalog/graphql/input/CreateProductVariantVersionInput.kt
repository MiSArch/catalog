package org.misarch.catalog.graphql.input

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import java.util.UUID

@GraphQLDescription("Input for the createProductVariantVersion mutation")
class CreateProductVariantVersionInput(
    @property:GraphQLDescription("The id of the ProductVariant this ProductVariantVersion belongs to.")
    val productVariantId: UUID,
    name: String,
    description: String,
    retailPrice: Int,
    canBeReturnedForDays: Double? = null,
    categoricalCharacteristicValues: List<CategoricalCategoryCharacteristicValueInput>,
    numericalCharacteristicValues: List<NumericalCategoryCharacteristicValueInput>,
    taxRateId: UUID
) : ProductVariantVersionInput(
    name,
    description,
    retailPrice,
    canBeReturnedForDays,
    categoricalCharacteristicValues,
    numericalCharacteristicValues,
    taxRateId
)