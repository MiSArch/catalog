package org.misarch.catalog.graphql.input

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID

@GraphQLDescription("Input for creating a NumericalCategoryCharacteristicValue.")
class NumericalCategoryCharacteristicValueInput(
    @property:GraphQLDescription("The value of the NumericalCategoryCharacteristicValue.")
    val value: Double,
    @property:GraphQLDescription("The id of the CategoryCharacteristic.")
    val characteristicId: ID
)