package behavioral.chainofresponsibility

import java.lang.IllegalArgumentException

// Chain of responsibility (AKA CoR, Chain of Command) is a behavioral design pattern that lets you pass requests along
// a chain of handlers. Upon receiving a request, each handler decides either to process the request or to pass it to
// the next handler in the chain

// In this example we are implementing a handler structure for processing incoming requests on a application application
// server, disposing of tree basic handlers.

// Handler interface that all nodes of a chain must implement
interface Handler{
    fun handle(request: Request): Request
}

// Abstract class that concentrate common logic for all handlers (like triggering next nodes of the chain)
// This avoids code repetition.
abstract class BaseHandler(private var next: Handler?): Handler {
    fun handleNext(request: Request): Request = next?.handle(request) ?: request
}

// Concrete implementations of our handler interface

class ValidationHandler(val next: Handler?) : BaseHandler(next) {
    override fun handle(request: Request): Request {
        if(request.email.isBlank() || request.message.isBlank())
            throw IllegalArgumentException("the request is not valid")
        return super.handleNext(request)
    }
}

class AuthenticationHandler(val next: Handler?) : BaseHandler(next) {
    override fun handle(request: Request): Request {
        if(!request.email.isAuthenticated())
            throw IllegalArgumentException("the user is not authenticated")
        return super.handleNext(request)
    }
}

class AuthorizationHandler(val next: Handler?) : BaseHandler(next) {
    override fun handle(request: Request): Request {
        if(!request.email.isAuthorized())
            throw IllegalArgumentException("the user is not authorized")
        return super.handleNext(request)
    }
}


