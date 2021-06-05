package behavioral.observer

import behavioral.observer.EventsManager.EventType.*
import behavioral.observer.EventsManager.UpdateData
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class ObserverOOPTest {

    private lateinit var eventsManager: EventsManager

    @BeforeEach
    fun setEventsManager() = run {
        eventsManager = EventsManager()
    }

    @Test
    fun `should subscribe a listener relating an EventType when using the subscribe method`() {
        val logSubscriber = LogSubscriber()
        val emailSubscriber = EmailSubscriber()

        eventsManager.subscribe(logSubscriber, SYSTEM_SUSPEND)
        eventsManager.subscribe(emailSubscriber, SYSTEM_ON)

        eventsManager.listeners.size `should be equal to` 2
    }

    @Test
    fun `should unsubscribe a listener when using the unsubscribe method`() {
        val logSubscriber = LogSubscriber()
        val emailSubscriber = EmailSubscriber()

        eventsManager.subscribe(logSubscriber, SYSTEM_SUSPEND)
        eventsManager.subscribe(emailSubscriber, SYSTEM_ON)
        eventsManager.listeners.size `should be equal to` 2

        eventsManager.unsubscribe(logSubscriber)
        eventsManager.unsubscribe(emailSubscriber)
        eventsManager.listeners.size `should be equal to` 0
    }

    @Test
    fun `should notify all listeners with update when using the notify method`() {
        val logSubscriber = mockk<LogSubscriber>(relaxed = true)
        val emailSubscriber = mockk<EmailSubscriber>(relaxed = true)

        eventsManager.run {
            subscribe(logSubscriber, SYSTEM_SUSPEND)
            subscribe(emailSubscriber, SYSTEM_ON)

            notify(SYSTEM_ON, "System was being initialized at 09:00 AM")
            notify(SYSTEM_SUSPEND, "System was being suspended at 06:00 PM")
            notify(SYSTEM_OFF, "System was shutdown at 08:00 AM")
            notify(SYSTEM_ON, "System was being initialized at 10:00 AM")
        }



        verify(exactly = 1) {
            logSubscriber.update(
                UpdateData(
                    "System was being suspended at 06:00 PM",
                    SYSTEM_SUSPEND,
                    )
            )
        }
        verify(exactly = 1) {
            emailSubscriber.update(
                UpdateData(
                    "System was being initialized at 09:00 AM",
                    SYSTEM_ON,
                )
            )
        }
        verify(exactly = 1) {
            emailSubscriber.update(
                UpdateData(
                    "System was being initialized at 10:00 AM",
                    SYSTEM_ON,
                )
            )
        }
    }
}