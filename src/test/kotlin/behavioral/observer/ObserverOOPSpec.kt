package behavioral.observer

import behavioral.observer.SystemEventsManagerOOP.SystemEventType.*
import behavioral.observer.SystemEventsManagerOOP.EventData
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


internal class ObserverOOPSpec : Spek({
    describe("Observer pattern implementation with Object Oriented Programming features") {
        lateinit var systemEventsManager: SystemEventsManagerOOP

        beforeEachTest { systemEventsManager = SystemEventsManagerOOP() }

        describe("subscribe() method") {
            it("should include the subscriber relating the given EventType into subscribers map") {
                val logSubscriber = LogSubscriber()
                val emailSubscriber = EmailSubscriber()

                systemEventsManager.subscribe(logSubscriber, SYSTEM_SUSPEND)
                systemEventsManager.subscribe(emailSubscriber, SYSTEM_ON)

                systemEventsManager.subscribers.size `should be equal to` 2
            }
        }
        describe("unsubscribe() method") {
            it("should remove the subscriber from subscribers map") {
                val logSubscriber = LogSubscriber()
                val emailSubscriber = EmailSubscriber()

                systemEventsManager.subscribe(logSubscriber, SYSTEM_SUSPEND)
                systemEventsManager.subscribe(emailSubscriber, SYSTEM_ON)
                systemEventsManager.subscribers.size `should be equal to` 2

                systemEventsManager.unsubscribe(logSubscriber)
                systemEventsManager.unsubscribe(emailSubscriber)
                systemEventsManager.subscribers.size `should be equal to` 0
            }
        }

        describe("notify() method") {
            it("should notify all subscribers matching the given EventType") {
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
    }
})