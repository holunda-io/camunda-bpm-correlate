package io.holunda.camunda.bpm.correlate.resources

import io.holunda.camunda.bpm.correlate.CamundaBpmCorrelateServices
import io.holunda.camunda.bpm.correlate.dto.MessageDto
import io.holunda.camunda.bpm.correlate.dto.toDto
import io.holunda.camunda.bpm.correlate.getBean
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginResource
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

/**
 * Resource for access to correlate messages.
 */
class CorrelateMessageResource(engineName: String) : AbstractCockpitPluginResource(engineName) {

  private val services = getBean(CamundaBpmCorrelateServices::class)

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  fun getMessages(@QueryParam("faultsOnly") faults: Boolean, @QueryParam("page") page: Int, @QueryParam("size") size: Int): List<MessageDto> {
    return services.messageRepository.findAllLight(page, size).map { it.toDto() }
  }

}
