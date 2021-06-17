package behavioral.chainofresponsibility

data class Request(val email: String, val message: String)

internal fun String.isAuthenticated(): Boolean = this.contains("@gmail.com")
internal fun String.isAuthorized(): Boolean = this.contains("_admin")

