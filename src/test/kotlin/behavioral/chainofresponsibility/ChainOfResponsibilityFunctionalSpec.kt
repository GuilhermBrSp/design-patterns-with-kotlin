package behavioral.chainofresponsibility

import io.mockk.spyk
import io.mockk.verify
import org.amshove.kluent.`should not throw`
import org.amshove.kluent.`should throw`
import org.amshove.kluent.`with message`
import org.amshove.kluent.invoking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.lang.IllegalArgumentException

class ChainOfResponsibilityFunctionalSpec: Spek({
    describe("Chain Of Responsibility pattern implementation with Functional Programming features") {
        describe("dynamic chain creation and execution") {
            context("when creating a chain with 3 handlers and valid authenticated and authorized request") {
                it("process the request successfully and return it") {
                    val chain = validationHandler(authenticationHandler(authorizationHandler(null)))
                    val request = Request("guilherme_admin@gmail.com", "random message")

                    invoking { chain(request) } `should not throw` Exception::class
                }
            }
            context("when creating a chain with 3 handlers and invalid request") {
                it("cease the request processing in validation handler") {
                    val chain = validationHandler(authenticationHandler(authorizationHandler(null)))
                    val request = Request("", "")

                    invoking { chain(request) } `should throw` IllegalArgumentException::class `with message` "the request is not valid"
                }
            }
            context("when creating a chain with 3 handlers and valid but unauthenticated request") {
                it("cease the request processing in authentication handler") {
                    val chain = validationHandler(authenticationHandler(authorizationHandler(null)))
                    val request = Request("bla@email.com", "random message")

                    invoking { chain(request) } `should throw` IllegalArgumentException::class `with message` "the user is not authenticated"

                }
            }
            context("when creating a chain with 3 handlers and valid but unauthorized request") {
                it("cease the request processing in authorization handler") {
                    val chain = validationHandler(authenticationHandler(authorizationHandler(null)))
                    val request = Request("guilherme@gmail.com", "random message")

                    invoking { chain(request) } `should throw` IllegalArgumentException::class `with message` "the user is not authorized"
                }
            }
        }
    }

})