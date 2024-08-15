package ru.pkozlov.plugins

import io.ktor.server.application.*
import ru.pkozlov.competition.routing.competitionRoutes

fun Application.configureRouting() {
    competitionRoutes()
}
