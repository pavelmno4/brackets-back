package ru.pkozlov.brackets.plugins

import io.ktor.server.application.*
import ru.pkozlov.brackets.competition.routing.competitionRoutes

fun Application.configureRouting() {
    competitionRoutes()
}
