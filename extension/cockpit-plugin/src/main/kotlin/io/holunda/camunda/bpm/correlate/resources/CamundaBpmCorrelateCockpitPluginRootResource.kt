package io.holunda.camunda.bpm.correlate.resources

import io.holunda.camunda.bpm.correlate.CamundaBpmCorrelateCockpitPlugin
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginRootResource
import javax.ws.rs.Path
import javax.ws.rs.PathParam

@Path("plugin/" + CamundaBpmCorrelateCockpitPlugin.ID)
class CamundaBpmCorrelateCockpitPluginRootResource: AbstractCockpitPluginRootResource(CamundaBpmCorrelateCockpitPlugin.ID) {
  @Path("{engineName}/messages")
  fun getProcessInstanceResource(@PathParam("engineName") engineName: String): CorrelateMessageResource {
    return subResource(CorrelateMessageResource(engineName), engineName)
  }
}
