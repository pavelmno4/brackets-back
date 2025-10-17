package ru.pkozlov.brackets.viewer.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.pkozlov.brackets.auth.enumeration.Role.VIEWER
import ru.pkozlov.brackets.auth.service.UserService
import ru.pkozlov.brackets.auth.utils.hasRoles
import ru.pkozlov.brackets.competition.service.CompetitionService
import ru.pkozlov.brackets.viewer.dto.CreateViewerDto
import ru.pkozlov.brackets.viewer.service.ViewerService
import java.util.*

fun Application.viewerRoutes() {
    val competitionService: CompetitionService by inject()
    val viewerService: ViewerService by inject()
    val userService: UserService by inject()

    routing {
        route("/competitions/{competitionId}/viewers") {
            post {
                val competitionId: UUID = call.parameters["competitionId"]
                    ?.run(UUID::fromString)
                    ?: run { call.respond(HttpStatusCode.BadRequest); return@post }

                if (competitionService.isPassed(competitionId)) {
                    call.respond(HttpStatusCode.Forbidden); return@post
                }

                val viewer: CreateViewerDto = call.receive<CreateViewerDto>()

                viewerService.create(competitionId, viewer)
                    .let { createdViewer -> call.respond(createdViewer) }
            }

            authenticate("auth-session") {
                get {
                    try {
                        hasRoles(setOf(VIEWER), userService)

                        val competitionId: UUID = call.parameters["competitionId"]
                            ?.run(UUID::fromString)
                            ?: run { call.respond(HttpStatusCode.BadRequest); return@get }

                        viewerService.findAll(competitionId)
                            .let { viewers -> call.respond(viewers) }

                    } catch (exc: IllegalArgumentException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
        }
    }
}