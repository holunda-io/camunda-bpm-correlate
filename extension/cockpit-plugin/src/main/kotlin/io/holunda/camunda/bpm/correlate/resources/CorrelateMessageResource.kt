package io.holunda.camunda.bpm.correlate.resources

import io.holunda.camunda.bpm.correlate.CamundaBpmCorrelateCockpitPlugin
import io.holunda.camunda.bpm.correlate.db.CorrelateMessageDto
import org.camunda.bpm.cockpit.db.QueryParameters
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginResource
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

class CorrelateMessageResource(engineName: String) : AbstractCockpitPluginResource(engineName) {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  fun getCorrelateMessages(): List<CorrelateMessageDto> {
    return queryService.executeQuery("${CamundaBpmCorrelateCockpitPlugin.PREFIX}.selectCorrelateMessages", QueryParameters())
  }
}
