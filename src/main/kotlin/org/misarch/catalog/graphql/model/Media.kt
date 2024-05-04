package org.misarch.catalog.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.federation.directives.FieldSet
import com.expediagroup.graphql.generator.federation.directives.KeyDirective
import java.util.*

@GraphQLDescription("A Media file.")
@KeyDirective(fields = FieldSet("id"), resolvable = false)
class Media(
    id: UUID
) : Node(id)