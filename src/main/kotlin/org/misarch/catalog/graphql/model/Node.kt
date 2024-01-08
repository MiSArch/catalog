package org.misarch.catalog.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import java.util.*

@GraphQLDescription("An object with an ID.")
abstract class Node(
    internal val id: UUID
) {

    @GraphQLDescription("The ID of the node.")
    fun id(): ID {
        return ID(id.toString())
    }

}