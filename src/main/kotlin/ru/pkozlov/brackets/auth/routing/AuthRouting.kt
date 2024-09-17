package ru.pkozlov.brackets.auth.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.authRoutes() {
    routing {
        authenticate("auth-form") {
            post("/login") {
                val principal = call.principal<UserIdPrincipal>()
                call.sessions.set(principal)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}