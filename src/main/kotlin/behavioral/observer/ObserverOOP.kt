package behavioral.observer

import behavioral.observer.SystemEventsManagerOOP.*

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
// This implementation uses an Object Oriented approach.


// This is the "publisher", also called as the "subject"
class SystemEventsManagerOOP() {
    var subscribers: Map<Subscriber, SystemEventType> = emptyMap()
        private set

    fun subscribe(subscriber: Subscriber, eventType: SystemEventType) = apply {
        subscribers = subscribers.plus(mapOf(subscriber to eventType))
    }

    fun unsubscribe(subscriber: Subscriber) = apply {
        subscribers = subscribers.minus(subscriber)
    }

    fun notify(eventType: SystemEventType, message: String) =
        subscribers
            .filter { it.value == eventType }
            .map { it.key.update(EventData(message, eventType)) }

    enum class SystemEventType {
        SYSTEM_ON,
        SYSTEM_OFF,
        SYSTEM_SUSPEND
    }

    data class EventData(val message: String, val eventType: SystemEventType)
}

// This is the "subscriber/observer/listener" interface, defining the default method to receive a notification
interface Subscriber {
    fun update(data: EventData)
}

// Concrete implementation of the "subscriber" interface with custom logic
class LogSubscriber : Subscriber {
    override fun update(data: EventData) =
        println("Logging the event ${data.eventType} with message ${data.message}")

}

// Concrete implementation of the "subscriber" interface with custom logic
class EmailSubscriber : Subscriber {
    override fun update(data: EventData) =
        println("Sending email to inform the event ${data.eventType} with message ${data.message}")

}

