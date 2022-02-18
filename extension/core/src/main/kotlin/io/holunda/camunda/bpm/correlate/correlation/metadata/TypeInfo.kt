package io.holunda.camunda.bpm.correlate.correlation.metadata

/**
 * Represents type info.
 */
data class TypeInfo(
  /**
   * Namespace.
   */
  val namespace: String,
  /**
   * Type name inside the namespace.
   */
  val name: String,
  /**
   * Revision of the type.
   */
  val revision: String? = null,
  /**
   * Indicates if the decision about the type is overridable. See [MessageMetaDataSnippet]
   */
  val overwritePossible: Boolean = true
) {
  companion object {
    /**
     * Constructs a type info from full-qualified class name.
     */
    fun fromFQCN(fqcn: String): TypeInfo {
      return if (fqcn.lastIndexOf(".") != -1) {
        from(
          namespace = fqcn.substringBeforeLast("."),
          name = fqcn.substringAfterLast("."),
          revision = null
        )
      } else {
        UNKNOWN
      }
    }

    /**
     * Constructs a type info from namespace coordinates.
     */
    fun from(namespace: String?, name: String?, revision: String?): TypeInfo {
      return if (namespace != null && name != null) {
        TypeInfo(namespace = namespace, name = name, revision = revision, overwritePossible = false)
      } else {
        UNKNOWN
      }
    }


    /**
     * Null object for the type info.
     */
    val UNKNOWN = TypeInfo(".not a namespace", ".not a type", ".no revision")
  }

  /**
   * Simple adding the class name into the namespace.
   */
  fun toFQCN(): String = "$namespace.$name"
}
