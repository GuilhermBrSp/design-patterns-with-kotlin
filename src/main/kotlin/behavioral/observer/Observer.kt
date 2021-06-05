package behavioral.observer

// Observer is a behavioral design pattern that lets you define a subscription mechanism to notify
// multiple objects about any events that happen to the object they're observing.

// The fundamental logic is based on the principle that the Publisher (which holds the subject)
// provides a mechanism to other class to subscribe or unsubscribe from notifications (at runtime).
// Internally it can use some kind of collection to maintain a reference to its subscribers and they subscriptions.
// For example it would be wise to use a hashMap containing the subscriber and the event type it is subscribed for.
// Those subscribers will implement a common interface,
// which will enable the Publisher to send its notifications.


// This is the "publisher", also called as the "subject"
class EventsManager() {
    var listeners: Map<Subscriber, EventType> = emptyMap()
        private set

    fun subscribe(subscriber: Subscriber, eventType: EventType) = apply {
        listeners = listeners.plus(mapOf(subscriber to eventType))
    }

    fun unsubscribe(subscriber: Subscriber) = apply {
        listeners = listeners.minus(subscriber)
    }

    fun notify(eventType: EventType, message: String) =
        listeners
            .filter { it.value == eventType }
            .map { it.key.update(UpdateData(message, eventType)) }

    enum class EventType {
        SYSTEM_ON,
        SYSTEM_OFF,
        SYSTEM_SUSPEND
    }

    data class UpdateData(val message: String, val eventType: EventType)
}

// This is the "subscriber/observer/listener" interface, defining the default method to receive a notification
interface Subscriber {
    fun update(data: EventsManager.UpdateData)
}

// Concrete implementation of the "subscriber" interface with custom logic
class LogSubscriber : Subscriber {
    override fun update(data: EventsManager.UpdateData) =
        println("Logging the event ${data.eventType} with message ${data.message}")

}

// Concrete implementation of the "subscriber" interface with custom logic
class EmailSubscriber : Subscriber {
    override fun update(data: EventsManager.UpdateData) =
        println("Sending email to inform the event ${data.eventType} with message ${data.message}")

}

