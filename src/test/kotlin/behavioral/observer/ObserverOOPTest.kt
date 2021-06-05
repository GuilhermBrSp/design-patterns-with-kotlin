package behavioral.observer

import behavioral.observer.SystemEventsManagerOOP.SystemEventType.*
import behavioral.observer.SystemEventsManagerOOP.EventData
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class ObserverOOPTest {

    private lateinit var systemEventsManager: SystemEventsManagerOOP

    @BeforeEach
    fun setEventsManager() = run {
        systemEventsManager = SystemEventsManagerOOP()
    }

    @Test
    fun `should subscribe a subscriber relating an EventType when using the subscribe method`() {
        val logSubscriber = LogSubscriber()
        val emailSubscriber = EmailSubscriber()

        systemEventsManager.subscribe(logSubscriber, SYSTEM_SUSPEND)
        systemEventsManager.subscribe(emailSubscriber, SYSTEM_ON)

        systemEventsManager.subscribers.size `should be equal to` 2
    }

    @Test
    fun `should unsubscribe a subscriber when using the unsubscribe method`() {
        val logSubscriber = LogSubscriber()
        val emailSubscriber = EmailSubscriber()

        systemEventsManager.subscribe(logSubscriber, SYSTEM_SUSPEND)
        systemEventsManager.subscribe(emailSubscriber, SYSTEM_ON)
        systemEventsManager.subscribers.size `should be equal to` 2

        systemEventsManager.unsubscribe(logSubscriber)
        systemEventsManager.unsubscribe(emailSubscriber)
        systemEventsManager.subscribers.size `should be equal to` 0
    }

    @Test
    fun `should notify all subscribers with update when using the notify method`() {
        val logSubscriber = mockk<LogSubscriber>(relaxed = true)
        val emailSubscriber = mockk<EmailSubscriber>(relaxed = true)

        systemEventsManager.run {
            subscribe(logSubscriber, SYSTEM_SUSPEND)
            subscribe(emailSubscriber, SYSTEM_ON)

            notify(SYSTEM_ON, "System was being initialized at 09:00 AM")
            notify(SYSTEM_SUSPEND, "System was being suspended at 06:00 PM")
            notify(SYSTEM_OFF, "System was shutdown at 08:00 AM")
            notify(SYSTEM_ON, "System was being initialized at 10:00 AM")
        }



        verify(exactly = 1) {
            logSubscriber.update(
                EventData(
                    "System was being suspended at 06:00 PM",
                    SYSTEM_SUSPEND,
                    )
            )
        }
        verify(exactly = 1) {
            emailSubscriber.update(
                EventData(
                    "System was being initialized at 09:00 AM",
                    SYSTEM_ON,
                )
            )
        }
        verify(exactly = 1) {
            emailSubscriber.update(
                EventData(
                    "System was being initialized at 10:00 AM",
                    SYSTEM_ON,
                )
            )
        }
    }
}