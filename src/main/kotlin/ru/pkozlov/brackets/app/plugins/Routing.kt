package ru.pkozlov.brackets.app.plugins

import io.ktor.server.application.*
import ru.pkozlov.brackets.competition.routing.competitionRoutes
import ru.pkozlov.brackets.participant.routing.participantRoutes

fun Application.configureRouting() {
    competitionRoutes()
    participantRoutes()
}
