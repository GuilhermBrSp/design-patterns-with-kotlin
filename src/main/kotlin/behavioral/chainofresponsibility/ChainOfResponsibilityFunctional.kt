package behavioral.chainofresponsibility

import java.lang.IllegalArgumentException

// Chain of responsibility (AKA CoR, Chain of Command) is a behavioral design pattern that lets you pass requests along
// a chain of handlers. Upon receiving a request, each handler decides either to process the request or to pass it to
// the next handler in the chain

// In this example we are implementing a handler structure for processing incoming requests on a application application
// server, disposing of tree basic handlers.

// Here we are defining this function signature as our "interface" for handlers, using typealias to give a name to this type
typealias HandlerFunctional = (request: Request) -> Request

val authorizationHandler = fun(next: HandlerFunctional?) =
    fun(request: Request): Request {
        if (!request.email.isAuthorized())
            throw IllegalArgumentException("the user is not authorized")
        return next?.let { it(request) } ?: request
    }

val authenticationHandler = fun(next: HandlerFunctional?) =
    fun(request: Request): Request {
        if (!request.email.isAuthenticated())
            throw IllegalArgumentException("the user is not authenticated")
        return next?.let { it(request) } ?: request
    }


val validationHandler = fun(next: HandlerFunctional?) =
    fun(request: Request): Request {
        if (request.email.isBlank() || request.message.isBlank())
            throw IllegalArgumentException("the request is not valid")
        return next?.let { it(request) } ?: request
    }
