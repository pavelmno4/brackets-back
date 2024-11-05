package ru.pkozlov.brackets.auth.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import ru.pkozlov.brackets.auth.dto.UserSession
import ru.pkozlov.brackets.auth.service.UserService

fun Application.authRoutes() {
    val userService: UserService by inject()

    routing {
        authenticate("auth-form") {
            post("/login") {
                val principal = call.principal<UserSession>()
                call.sessions.set(principal)
                call.respond(HttpStatusCode.OK)
            }
        }

        post("/logout") {
            call.sessions.clear<UserSession>()
            call.respond(HttpStatusCode.OK)
        }

        authenticate("auth-session") {
            get("/users/me") {
                call.principal<UserSession>()
                    ?.let { principal -> userService.findUser(principal.login) }
                    ?.let { user -> call.respond(user) }
                    ?: call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}