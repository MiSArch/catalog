package org.misarch.catalog.persistence.model

import org.misarch.catalog.graphql.model.TaxRate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table
class TaxRateEntity(
    @Id
    override val id: UUID
) : BaseEntity<TaxRate> {

    companion object {
        /**
         * Querydsl entity
         */
        val ENTITY = QTaxRateEntity.taxRateEntity!!
    }

    override fun toDTO(): TaxRate {
        return TaxRate(id)
    }
}