package behavioral.chainofresponsibility

import java.lang.IllegalArgumentException

// Chain of responsibility (AKA CoR, Chain of Command) is a behavioral design pattern that lets you pass requests along
// a chain of handlers. Upon receiving a request, each handler decides either to process the request or to pass it to
// the next handler in the chain


interface Handler{
    fun handle(request: Request): Request
}

abstract class BaseHandler(private var next: Handler?): Handler {
    fun handleNext(request: Request): Request = next?.handle(request) ?: request
}

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


