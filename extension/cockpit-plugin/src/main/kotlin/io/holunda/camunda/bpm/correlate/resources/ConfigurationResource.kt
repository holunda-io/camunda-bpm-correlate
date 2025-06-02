package io.holunda.camunda.bpm.correlate.resources

import io.holunda.camunda.bpm.correlate.CamundaBpmCorrelateServices
import io.holunda.camunda.bpm.correlate.dto.ConfigurationDto
import io.holunda.camunda.bpm.correlate.getBean
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginResource
import jakarta.ws.rs.GET
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

/**
 * Resource to work with configuration of the camunda-correlate.
 */
class ConfigurationResource(engineName: String) : AbstractCockpitPluginResource(engineName) {

  private val services = getBean(CamundaBpmCorrelateServices::class)

  /**
   * Retrieves configuration.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  fun getConfiguration(): ConfigurationDto {
    return ConfigurationDto(
      maxRetries = services.configuration.persistence.messageMaxRetries
    )
  }

}
