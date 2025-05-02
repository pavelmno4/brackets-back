package ru.pkozlov.brackets.app.plugins

import io.ktor.server.application.*
import ru.pkozlov.brackets.auth.routing.authRoutes
import ru.pkozlov.brackets.competition.routing.competitionRoutes
import ru.pkozlov.brackets.file.routing.fileRoutes
import ru.pkozlov.brackets.grid.routing.gridRoutes
import ru.pkozlov.brackets.participant.routing.participantRoutes

fun Application.configureRouting() {
    competitionRoutes()
    participantRoutes()
    authRoutes()
    gridRoutes()
    fileRoutes()
}
