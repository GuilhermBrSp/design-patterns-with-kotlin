package behavioral.mediator

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class MediatorSpec : Spek({
    describe("Mediator pattern implementation") {
        describe("requestLanding() method from mediator's Flight component") {
            context("when there is just one runway available and 3 flights request for landing at same time") {
                it("mediator orchestrates states to just perform 1 successfully landing") {
                    runBlocking {
                        val mediator = AirstripControlTower()
                        val flight01 = Flight(mediator)
                        val flight02 = Flight(mediator)
                        val flight03 = Flight(mediator)
                        val runway01 = Runway(mediator)
                        mediator.flights = listOf(flight01, flight02, flight03)
                        mediator.runways = listOf(runway01)

                        val landing01 = async { flight01.requestLanding() }

                        val landing02 = async { flight02.requestLanding() }

                        val landing03 = async { flight03.requestLanding() }

                        awaitAll(landing01, landing02, landing03)

                        mediator.successfullyLandings `should be equal to` mutableMapOf(runway01 to 1)
                        flight01.hasLanded `should be equal to` true
                        flight02.hasLanded `should be equal to` false
                        flight03.hasLanded `should be equal to` false
                    }
                }
            }
            context("when there is two runway available and 3 flights request for landing at same time") {
                it("mediator orchestrates states to perform 2 successfully landings") {
                    runBlocking {
                        val mediator = AirstripControlTower()
                        val flight01 = Flight(mediator)
                        val flight02 = Flight(mediator)
                        val flight03 = Flight(mediator)
                        val runway01 = Runway(mediator)
                        val runway02 = Runway(mediator)
                        mediator.flights = listOf(flight01, flight02, flight03)
                        mediator.runways = listOf(runway01, runway02)

                        val landing01 = async { flight01.requestLanding() }

                        val landing02 = async { flight02.requestLanding() }

                        val landing03 = async { flight03.requestLanding() }

                        awaitAll(landing01, landing02, landing03)

                        mediator.successfullyLandings `should be equal to` mutableMapOf(
                            runway01 to 1,
                            runway02 to 1
                        )
                        flight01.hasLanded `should be equal to` true
                        flight02.hasLanded `should be equal to` true
                        flight03.hasLanded `should be equal to` false
                    }
                }
            }
            context("when there is one runway available and 3 flights request for landing sequentially") {
                it("mediator orchestrates states to perform 3 successfully landings") {
                    runBlocking {
                        val mediator = AirstripControlTower()
                        val flight01 = Flight(mediator)
                        val flight02 = Flight(mediator)
                        val flight03 = Flight(mediator)
                        val runway01 = Runway(mediator)
                        mediator.flights = listOf(flight01, flight02, flight03)
                        mediator.runways = listOf(runway01)

                        val landings = async {
                            flight01.requestLanding()
                            flight02.requestLanding()
                            flight03.requestLanding()
                        }

                        landings.await()

                        mediator.successfullyLandings `should be equal to` mutableMapOf(runway01 to 3)
                        flight01.hasLanded `should be equal to` true
                        flight02.hasLanded `should be equal to` true
                        flight03.hasLanded `should be equal to` true
                    }
                }
            }
        }
    }
})