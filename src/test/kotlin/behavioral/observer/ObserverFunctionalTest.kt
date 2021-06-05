package behavioral.observer

import behavioral.observer.SystemEventsManagerFunc.EventType.*
import behavioral.observer.SystemEventsManagerFunc.EventMessage
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class ObserverFunctionalTest {

    private lateinit var systemEventsManager: SystemEventsManagerFunc

    @BeforeEach
    fun setEventsManager() = run {
        systemEventsManager = SystemEventsManagerFunc()
    }

    @Test
    fun `should subscribe a listener relating an EventType when using the subscribe method`() {
        val logSubscriber = LoggerService()
        val emailSubscriber = MailerService()

        systemEventsManager.subscribe(logSubscriber::logEvent, SYSTEM_SUSPEND)
        systemEventsManager.subscribe(emailSubscriber::sendEmail, SYSTEM_ON)

        systemEventsManager.listeners.size `should be equal to` 2
    }

    @Test
    fun `should unsubscribe a listener when using the unsubscribe method`() {
        val logSubscriber = LoggerService()
        val emailSubscriber = MailerService()

        systemEventsManager.subscribe(logSubscriber::logEvent, SYSTEM_SUSPEND)
        systemEventsManager.subscribe(emailSubscriber::sendEmail, SYSTEM_ON)
        systemEventsManager.listeners.size `should be equal to` 2

        systemEventsManager.unsubscribe(logSubscriber::logEvent)
        systemEventsManager.unsubscribe(emailSubscriber::sendEmail)
        systemEventsManager.listeners.size `should be equal to` 0
    }

    @Test
    fun `should notify all listeners with update when using the notify method`() {
        val logSubscriber = mockk<LoggerService>(relaxed = true)
        val emailSubscriber = mockk<MailerService>(relaxed = true)

        systemEventsManager.run {
            subscribe(logSubscriber::logEvent, SYSTEM_SUSPEND)
            subscribe(emailSubscriber::sendEmail, SYSTEM_ON)

            notify(SYSTEM_ON, "System was being initialized at 09:00 AM")
            notify(SYSTEM_SUSPEND, "System was being suspended at 06:00 PM")
            notify(SYSTEM_OFF, "System was shutdown at 08:00 AM")
            notify(SYSTEM_ON, "System was being initialized at 10:00 AM")
        }



        verify(exactly = 1) {
            logSubscriber.logEvent(
                EventMessage(
                    "System was being suspended at 06:00 PM",
                    SYSTEM_SUSPEND,
                    )
            )
        }
        verify(exactly = 1) {
            emailSubscriber.sendEmail(
                EventMessage(
                    "System was being initialized at 09:00 AM",
                    SYSTEM_ON,
                )
            )
        }
        verify(exactly = 1) {
            emailSubscriber.sendEmail(
                EventMessage(
                    "System was being initialized at 10:00 AM",
                    SYSTEM_ON,
                )
            )
        }
    }
}