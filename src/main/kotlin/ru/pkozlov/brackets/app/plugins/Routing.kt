package ru.pkozlov.brackets.app.plugins

import io.ktor.server.application.*
import ru.pkozlov.brackets.competition.routing.competitionRoutes

fun Application.configureRouting() {
    competitionRoutes()
}
