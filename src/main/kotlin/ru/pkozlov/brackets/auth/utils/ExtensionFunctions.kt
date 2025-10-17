package ru.pkozlov.brackets.auth.utils

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.pkozlov.brackets.auth.dto.UserSession
import ru.pkozlov.brackets.auth.enumeration.Role
import ru.pkozlov.brackets.auth.service.UserService

suspend fun RoutingContext.hasRoles(roles: Set<Role>, userService: UserService) {
    val userRoles: Set<Role> = call.principal<UserSession>()
        ?.let { principal -> userService.findUser(principal.login)?.roles }
        ?: emptySet()

    if (!userRoles.containsAll(roles)) call.respond(HttpStatusCode.Forbidden)
}