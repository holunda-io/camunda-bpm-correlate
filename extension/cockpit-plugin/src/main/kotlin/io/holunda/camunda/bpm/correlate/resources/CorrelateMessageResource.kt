package io.holunda.camunda.bpm.correlate.resources

import io.holunda.camunda.bpm.correlate.CamundaBpmCorrelateServices
import io.holunda.camunda.bpm.correlate.dto.MessageDto
import io.holunda.camunda.bpm.correlate.dto.NextRetryDto
import io.holunda.camunda.bpm.correlate.dto.RetriesDto
import io.holunda.camunda.bpm.correlate.dto.toDto
import io.holunda.camunda.bpm.correlate.getBean
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginResource
import javax.ws.rs.*
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

  @POST
  @Path("{messageId}/nextRetry")
  @Consumes(MediaType.APPLICATION_JSON)
  fun changeMassageNextRetry(@PathParam("messageId") messageId: String, nextRetryDto: NextRetryDto) {
    services.messageManagementService.changeMessageNextRetry(messageId, nextRetryDto.nextRetry)
  }

  @POST
  @Path("{messageId}/retries")
  @Consumes(MediaType.APPLICATION_JSON)
  fun changeMassageRetries(@PathParam("messageId") messageId: String, retriesDto: RetriesDto) {
    services.messageManagementService.changeMessageRetryAttempt(messageId, retriesDto.retries)
  }

  @DELETE
  @Path("{messageId}")
  fun deleteMessage(@PathParam("messageId") messageId: String) {
    services.messageManagementService.deleteMessage(messageId)
  }
}
