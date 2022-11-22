package io.holunda.camunda.bpm.correlate.util

import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils

/**
 * Loads a qualified bean or falls back to a non-qualified version.
 */
inline fun <reified T : Any> BeanFactory.getQualifiedBeanWithFallback(name: String): T {
  return try {
    BeanFactoryAnnotationUtils.qualifiedBeanOfType(this, T::class.java, name)
  } catch (e: NoSuchBeanDefinitionException) {
    requireNotNull(this.getBean(T::class.java)) {
      "A bean of type ${T::class.qualifiedName} is required, but could not be found." +
        " Implement ${T::class.qualifiedName} and provide it as a bean and optionally qualifying it with ${name}."
    }
  }
}