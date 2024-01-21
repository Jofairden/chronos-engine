package chronos.engine.util

object Reflection {
    /**
     * Gets the fully qualified class name of the caller.
     *
     * @return the fully qualified class name of the caller, or null if the class name cannot be determined.
     */
    @JvmStatic
    fun getCallerClassName(): String? {
        val stElements = Thread.currentThread().stackTrace
        var callerClassName: String? = null
        for (i in 1 until stElements.size) {
            val ste = stElements[i]
            if (ste.className.indexOf("java.lang.Thread") != 0) {
                if (callerClassName == null) {
                    callerClassName = ste.className
                } else if (callerClassName != ste.className
                    && ste.className != "chronos.engine.implementation.logging.Loggable"
                    && !ste.className.startsWith("chronos.engine.core.dsl.LoggingDSLKt")
                ) {
                    callerClassName = ste.className
                    break
                }
            }
        }
        return callerClassName
    }
}