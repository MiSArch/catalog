package org.misarch.catalog.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import java.util.*

@GraphQLDescription("A numerical characteristic of a Category.")
class NumericalCategoryCharacteristic(
    id: UUID,
    name: String,
    description: String,
    categoryId: UUID,
    @property:GraphQLDescription("The unit of the NumericalCategoryCharacteristic.")
    val unit: String,
) : CategoryCharacteristic(id, name, description, categoryId)