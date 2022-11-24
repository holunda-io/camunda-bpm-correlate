package io.holunda.camunda.bpm.correlate.resources

import io.holunda.camunda.bpm.correlate.CamundaBpmCorrelateServices
import io.holunda.camunda.bpm.correlate.dto.MessageDto
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

  /**
   * Retrieves messages from the inbox.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  fun getMessages(@QueryParam("faultsOnly") faults: Boolean, @QueryParam("page") page: Int, @QueryParam("size") size: Int): List<MessageDto> {
    return services
      .messageRepository
      .findAllLight(page, size, faults)
      .map { it.toDto(services.configuration.persistence.messageMaxRetries) }
  }

  /**
   * Changes message retries.
   */
  @POST
  @Path("/{messageId}/retries")
  @Consumes(MediaType.APPLICATION_JSON)
  fun changeMassageRetries(@PathParam("messageId") messageId: String, retriesDto: RetriesDto) {
    services.messageManagementService.changeMessageRetries(messageId, retriesDto.retries, nextRetry = retriesDto.nextRetry.toInstant())
  }

  /**
   * Pauses message delivery.
   */
  @PUT
  @Path("/{messageId}/pause")
  @Consumes(MediaType.APPLICATION_JSON)
  fun pauseMessage(@PathParam("messageId") messageId: String) {
    services.messageManagementService.pauseMessageProcessing(messageId)
  }

  /**
   * Resume message delivery.
   */
  @DELETE
  @Path("/{messageId}/pause")
  @Consumes(MediaType.APPLICATION_JSON)
  fun resumeMessage(@PathParam("messageId") messageId: String) {
    services.messageManagementService.resumeMessageProcessing(messageId)
  }

  /**
   * Deletes a message.
   */
  @DELETE
  @Path("/{messageId}")
  @Consumes(MediaType.APPLICATION_JSON)
  fun deleteMessage(@PathParam("messageId") messageId: String) {
    services.messageManagementService.deleteMessage(messageId)
  }
}
