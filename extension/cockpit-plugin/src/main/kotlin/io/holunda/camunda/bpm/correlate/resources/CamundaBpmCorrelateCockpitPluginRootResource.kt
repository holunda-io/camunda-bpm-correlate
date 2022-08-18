package io.holunda.camunda.bpm.correlate.resources

import io.holunda.camunda.bpm.correlate.CamundaBpmCorrelateCockpitPlugin
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginRootResource
import javax.ws.rs.Path
import javax.ws.rs.PathParam

@Path("plugin/" + CamundaBpmCorrelateCockpitPlugin.ID)
class CamundaBpmCorrelateCockpitPluginRootResource : AbstractCockpitPluginRootResource(CamundaBpmCorrelateCockpitPlugin.ID) {

  @Path("{engineName}/messages")
  fun getMessagesResource(@PathParam("engineName") engineName: String): CorrelateMessageResource {
    return subResource(CorrelateMessageResource(engineName), engineName)
  }

  @Path("{engineName}/configuration")
  fun getConfigurationResource(@PathParam("engineName") engineName: String): ConfigurationResource {
    return subResource(ConfigurationResource(engineName), engineName)
  }

}
