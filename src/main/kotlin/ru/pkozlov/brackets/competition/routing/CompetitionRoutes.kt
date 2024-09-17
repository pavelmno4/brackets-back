package ru.pkozlov.brackets.competition.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.pkozlov.brackets.competition.dto.competition.PersistCompetitionDto
import ru.pkozlov.brackets.competition.service.CompetitionService
import java.util.*

fun Application.competitionRoutes() {
    val competitionService: CompetitionService by inject()

    routing {
        route("/competitions") {
            get("/upcoming") {
                competitionService.findUpcoming()
                    .let { competitions -> call.respond(competitions) }
            }

            get("/past") {
                competitionService.findPast()
                    .let { competitions -> call.respond(competitions) }
            }

            get("/{id}") {
                try {
                    val id: UUID = call.parameters["id"]?.run(UUID::fromString) ?: throw IllegalStateException()
                    competitionService.findById(id)
                        ?.let { competition -> call.respond(competition) }
                        ?: call.respond(HttpStatusCode.NoContent)

                } catch (exc: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            authenticate("auth-session") {
                post {
                    val competition: PersistCompetitionDto = call.receive<PersistCompetitionDto>()

                    competitionService.create(competition)
                        .let { createdCompetition -> call.respond(createdCompetition) }
                }
            }

            authenticate("auth-session") {
                put("/{id}") {
                    try {
                        val id: UUID = call.parameters["id"]?.run(UUID::fromString) ?: throw IllegalStateException()
                        val competition: PersistCompetitionDto = call.receive<PersistCompetitionDto>()

                        competitionService.update(id, competition)
                            ?.let { updatedCompetition -> call.respond(updatedCompetition) }
                            ?: call.respond(HttpStatusCode.NoContent)

                    } catch (exc: IllegalArgumentException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }

            get("/{id}/categories") {
                try {
                    val id: UUID = call.parameters["id"]?.run(UUID::fromString) ?: throw IllegalStateException()
                    competitionService.findById(id)?.categories
                        ?.let { categories -> call.respond(categories) }
                        ?: call.respond(HttpStatusCode.NoContent)

                } catch (exc: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}