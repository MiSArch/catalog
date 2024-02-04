package org.misarch.catalog.event


import io.dapr.Topic
import io.dapr.client.domain.CloudEvent
import org.misarch.catalog.event.model.CreateTaxRateDTO
import org.misarch.catalog.service.TaxRateService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Controller for Dapr events
 *
 * @param taxRateService the service for TaxRates
 */
@Controller
class DaprEventController(
    private val taxRateService: TaxRateService
) {

    /**
     * Handles user creation events
     *
     * @param cloudEvent the event to handle
     */
    @Topic(name = CatalogEvents.TAX_RATE_CREATED, pubsubName = CatalogEvents.PUBSUB_NAME)
    @PostMapping("/subscription/${CatalogEvents.TAX_RATE_CREATED}")
    @ResponseStatus(HttpStatus.OK)
    suspend fun onTaxRateCreated(
        @RequestBody
        cloudEvent: CloudEvent<CreateTaxRateDTO>
    ) {
        taxRateService.registerTaxRate(cloudEvent.data)
    }

}