package io.holunda.camunda.bpm.correlate

import io.holunda.camunda.bpm.correlate.resources.CamundaBpmCorrelateCockpitPluginRootResource
import org.camunda.bpm.cockpit.plugin.spi.impl.AbstractCockpitPlugin

class CamundaBpmCorrelateCockpitPlugin : AbstractCockpitPlugin() {

  companion object {
    const val PREFIX = "io.holunda.camunda.bpm.correlate"
    const val ID = "correlate-cockpit-plugin"
  }

  override fun getId(): String = ID

  override fun getResourceClasses(): Set<Class<*>> = hashSetOf<Class<*>>(
    CamundaBpmCorrelateCockpitPluginRootResource::class.java
  )

  override fun getMappingFiles(): List<String> {
    return listOf("${PREFIX.replace(".", "/")}/cockpit-plugin-query.xml")
  }
}
