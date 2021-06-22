package behavioral.chainofresponsibility

import io.mockk.spyk
import io.mockk.verify
import org.amshove.kluent.`should throw`
import org.amshove.kluent.`with message`
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Assertions.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.lang.IllegalArgumentException

internal class ChainOfResponsibilityOOPSpec : Spek({
    describe("Chain Of Responsibility pattern implementation with Object Oriented Programming features") {
        describe("dynamic chain creation and execution") {
            context("when creating a chain with 3 handlers and valid authenticated and authorized request") {
                it("process the request successfully and return it") {
                    val authorizationHandler = spyk(AuthorizationHandler(null))
                    val authenticationHandler = spyk(AuthenticationHandler(authorizationHandler))
                    val chain = spyk(ValidationHandler(authenticationHandler))
                    val request = Request("guilherme_admin@gmail.com", "random message")

                    chain.handle(request)

                    verify(exactly = 1) { chain.handle(request) }
                    verify(exactly = 1) { authenticationHandler.handle(request) }
                    verify(exactly = 1) { authorizationHandler.handle(request) }
                }
            }
            context("when creating a chain with 3 handlers and invalid request") {
                it("cease the request processing in validation handler") {
                    val authorizationHandler = spyk(AuthorizationHandler(null))
                    val authenticationHandler = spyk(AuthenticationHandler(authorizationHandler))
                    val chain = spyk(ValidationHandler(authenticationHandler))
                    val request = Request("", "")

                    invoking { chain.handle(request) } `should throw` IllegalArgumentException::class `with message` "the request is not valid"

                    verify(exactly = 1) { chain.handle(request) }
                    verify(exactly = 0) { authenticationHandler.handle(request) }
                    verify(exactly = 0) { authorizationHandler.handle(request) }
                }
            }
            context("when creating a chain with 3 handlers and valid but unauthenticated request") {
                it("cease the request processing in authentication handler") {
                    val authorizationHandler = spyk(AuthorizationHandler(null))
                    val authenticationHandler = spyk(AuthenticationHandler(authorizationHandler))
                    val chain = spyk(ValidationHandler(authenticationHandler))
                    val request = Request("bla@email.com", "random message")

                    invoking { chain.handle(request) } `should throw` IllegalArgumentException::class `with message` "the user is not authenticated"

                    verify(exactly = 1) { chain.handle(request) }
                    verify(exactly = 1) { authenticationHandler.handle(request) }
                    verify(exactly = 0) { authorizationHandler.handle(request) }
                }
            }
            context("when creating a chain with 3 handlers and valid but unauthorized request") {
                it("cease the request processing in authorization handler") {
                    val authorizationHandler = spyk(AuthorizationHandler(null))
                    val authenticationHandler = spyk(AuthenticationHandler(authorizationHandler))
                    val chain = spyk(ValidationHandler(authenticationHandler))
                    val request = Request("guilherme@gmail.com", "random message")

                    invoking { chain.handle(request) } `should throw` IllegalArgumentException::class `with message` "the user is not authorized"

                    verify(exactly = 1) { chain.handle(request) }
                    verify(exactly = 1) { authenticationHandler.handle(request) }
                    verify(exactly = 1) { authorizationHandler.handle(request) }
                }
            }
        }
    }
})