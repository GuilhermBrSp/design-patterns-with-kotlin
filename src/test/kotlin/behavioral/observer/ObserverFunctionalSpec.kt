package behavioral.observer

import behavioral.observer.SystemEventsManagerFunc.EventType.*
import behavioral.observer.SystemEventsManagerFunc.EventMessage
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


internal class ObserverFunctionalSpec : Spek({
    describe("Observer pattern implementation with Functional Programming features") {
        lateinit var systemEventsManager: SystemEventsManagerFunc

        beforeEachTest { systemEventsManager = SystemEventsManagerFunc() }

        describe("subscribe() method") {
            it("should subscribe a listener relating the given EventType") {
                val logSubscriber = LoggerService()
                val emailSubscriber = MailerService()

                systemEventsManager.subscribe(logSubscriber::logEvent, SYSTEM_SUSPEND)
                systemEventsManager.subscribe(emailSubscriber::sendEmail, SYSTEM_ON)

                systemEventsManager.listeners.size `should be equal to` 2
            }
        }
        describe("unsubscribe() method") {
            it("should unsubscribe the given listener") {
                val logSubscriber = LoggerService()
                val emailSubscriber = MailerService()

                systemEventsManager.subscribe(logSubscriber::logEvent, SYSTEM_SUSPEND)
                systemEventsManager.subscribe(emailSubscriber::sendEmail, SYSTEM_ON)

                systemEventsManager.listeners.size `should be equal to` 2
            }
        }

        describe("notify() method") {
            it("should notify all listeners matching the given EventType") {
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
    }
})
