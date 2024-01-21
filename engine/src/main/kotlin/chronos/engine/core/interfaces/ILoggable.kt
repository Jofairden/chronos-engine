package chronos.engine.core.interfaces

// Definieert generiek T dat gelogd kan worden
interface ILoggable<out T : Any> {
    val loggable : T // Loggable object
    fun getMessage() : String // The message to log
    fun <A : ILogLevel> log(level: A)// Log based on severity
    fun trace() // 	A log level describing events showing step by step execution of your code that can be ignored during the standard operation, but may be useful during extended debugging sessions.
    fun debug() //  A log level used for events considered to be useful during software debugging when more granular information is needed.
    fun info()  // 	An event happened, the event is purely informative and can be ignored during normal operations.
    fun warn()  // 	Unexpected behavior happened inside the application, but it is continuing its work and the key business features are operating as expected.
    fun error() // 	One or more functionalities are not working, preventing some functionalities from working correctly.
}