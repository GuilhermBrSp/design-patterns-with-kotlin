package behavioral.observer

// Observer is a behavioral design pattern that lets you define a subscription mechanism to notify
// multiple objects about any events that happen to the object they're observing.

// The fundamental logic is based on the principle that the Publisher (which holds the subject)
// provides a mechanism to other class to subscribe or unsubscribe from notifications (at runtime).
// Internally it can use some kind of collection to maintain a reference to its subscribers and they subscriptions.
// For example it would be wise to use a hashMap containing the subscriber and the event type it is subscribed for.
// Those subscribers will implement a common interface,
// which will enable the Publisher to send its notifications.

// The code below implements the observer pattern to enable the monitoring of power events of an imaginary system,
// where different classes can implement custom business logic to deal with specific power events notifications (like
// logging the event somewhere or sending an email).
// This implementation uses an Functional approach.

// The "listener/subscriber/observer" function signature, acting as our "interface"
// We are also using the kotlin typealias to give a name to this type so it can be more readable on the code
typealias Listener = (SystemEventsManagerFunc.EventMessage) -> Unit

// Sample class containing a function that follows the Listener signature an thus can be used as a Listener
class LoggerService {
    fun logEvent(data: SystemEventsManagerFunc.EventMessage): Unit =
        println("Logging the event ${data.eventType} with description ${data.description}")
}

// Sample class containing a function that follows the Listener signature an thus can be used as a Listener
class MailerService {
    fun sendEmail(data: SystemEventsManagerFunc.EventMessage): Unit =
        println("Sending email to inform the event ${data.eventType} with description ${data.description}")
}

// This is the "publisher", also called as the "subject"
class SystemEventsManagerFunc {

    var listeners: Map<Listener, EventType> = emptyMap()
        private set

    fun subscribe(listener: Listener, eventType: EventType) = apply {
        listeners = listeners.plus(mapOf(listener to eventType))
    }

    fun unsubscribe(listener: Listener) = apply {
        listeners = listeners.minus(listener)
    }

    fun notify(eventType: EventType, message: String) =
        listeners
            .filter { it.value == eventType }
            .map { it.key(EventMessage(message, eventType)) }

    data class EventMessage(val description: String, val eventType: EventType)

    enum class EventType {
        SYSTEM_ON,
        SYSTEM_OFF,
        SYSTEM_SUSPEND
    }
}


