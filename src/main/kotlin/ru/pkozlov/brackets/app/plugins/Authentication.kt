package ru.pkozlov.brackets.app.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import ru.pkozlov.brackets.auth.service.UserService

const val ONE_DAY_IN_SECONDS = 60 * 60 * 24L

fun Application.configureAuthentication() {
    val userService: UserService by inject()

    install(Authentication) {
        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials -> userService.authenticate(credentials) }
            challenge {
                call.respond(HttpStatusCode.Unauthorized, "Credentials are not valid")
            }
        }

        session<UserIdPrincipal>("auth-session") {
            validate { session -> session }
            challenge {
                call.respond(HttpStatusCode.Unauthorized, "Invalid session")
            }
        }
    }

    install(Sessions) {
        cookie<UserIdPrincipal>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = ONE_DAY_IN_SECONDS
        }
    }
}