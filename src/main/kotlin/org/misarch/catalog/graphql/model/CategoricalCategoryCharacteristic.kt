package org.misarch.catalog.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.federation.directives.FieldSet
import com.expediagroup.graphql.generator.federation.directives.KeyDirective
import java.util.*

@GraphQLDescription("A categorical characteristic of a category.")
@KeyDirective(fields = FieldSet("id"))
class CategoricalCategoryCharacteristic(
    id: UUID,
    name: String,
    description: String,
    categoryId: UUID
) : CategoryCharacteristic(id, name, description, categoryId)