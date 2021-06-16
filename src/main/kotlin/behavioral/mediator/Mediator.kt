package behavioral.mediator

import kotlinx.coroutines.*

// Mediator (AKA Intermediary, Controller) is a design pattern that lets you reduce chaotic dependencies between objects
// The pattern restricts direct communications between the objects and forces them to collaborate only via a mediator object

// The code below implements the mediator pattern to enable an AirstripControlTower orchestrate landings, managing states
// and communication between flight and runway objects. This implementation is making use of kotlin coroutines to better simulate
// the landing elapsed time.

// The interface of the mediator class, which define a simple method to process events
interface Mediator {
    suspend fun notify(sender: Component, event: String): Any
}

// The interface of components that relates the mediator
interface Component {
    val mediator: Mediator
}

class Flight(override val mediator: Mediator) : Component {
    var hasLanded: Boolean = false

    suspend fun requestLanding() = mediator.notify(this, "requestLanding")
}

class Runway(override val mediator: Mediator) : Component {
    var clearForLanding = true

    private suspend fun notifySuccessfullyLanding() = mediator.notify(this, "successfullyLanding")

    suspend fun receiveLanding() = coroutineScope {
        clearForLanding = false
        launch {
            delay(1000L)
            clearForLanding = true
            notifySuccessfullyLanding()
        }
    }
}

class AirstripControlTower(
    var flights: List<Flight> = emptyList(),
    var runways: List<Runway> = emptyList(),
) : Mediator {

    val successfullyLandings: MutableMap<Runway, Int> = mutableMapOf()

    // we can use the kotlin when expression to clearly assign the treatment for each sender and event that is trying to
    // communicate with the mediator.
    override suspend fun notify(sender: Component, event: String) =
        when (sender) {
            is Flight -> reactOnFlight(sender, event)
            is Runway -> reactOnRunway(sender, event)
            else -> throw java.lang.IllegalArgumentException()
        }


    private suspend fun reactOnFlight(flight: Flight, event: String) =
        when (event) {
            "requestLanding" -> {
                getFreeRunway()?.receiveLanding()?.invokeOnCompletion { flight.hasLanded = true }
                    ?: { flight.hasLanded = false }
            }
            else -> throw IllegalArgumentException()
        }


    private fun reactOnRunway(runway: Runway, event: String) =
        when (event) {
            "successfullyLanding" -> successfullyLandings[runway] = successfullyLandings.getOrDefault(runway, 0) + 1
            else -> throw IllegalArgumentException()
        }

    private fun getFreeRunway() = runways.find { it.clearForLanding }

}