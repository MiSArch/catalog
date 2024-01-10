package org.misarch.catalog.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import java.util.*

@GraphQLDescription("An object with an ID.")
abstract class Node(
    @property:GraphQLDescription("The ID of the node.")
    val id: UUID
)