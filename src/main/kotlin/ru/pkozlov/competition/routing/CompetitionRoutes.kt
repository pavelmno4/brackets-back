package ru.pkozlov.competition.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.pkozlov.competition.dto.competition.PersistCompetitionDto
import ru.pkozlov.competition.service.CompetitionService
import java.util.*

fun Application.competitionRoutes() {
    val competitionService: CompetitionService by inject()

    routing {
        route("/competitions") {
            get {
                val showInactive: Boolean = call.request.queryParameters["showInactive"]?.toBoolean() ?: false
                competitionService.findAll(showInactive)
                    .let { competitions -> call.respond(competitions) }
            }

            get("/{id}") {
                val id: UUID = call.parameters["id"]?.run(UUID::fromString) ?: throw IllegalStateException()
                competitionService.findById(id)
                    ?.let { competition -> call.respond(competition) }
                    ?: call.respond(HttpStatusCode.NoContent)
            }

            post {
                val competition: PersistCompetitionDto = call.receive<PersistCompetitionDto>()

                competitionService.create(competition)
                    .let { createdCompetition -> call.respond(createdCompetition) }
            }

            put("/{id}") {
                val id: UUID = call.parameters["id"]?.run(UUID::fromString) ?: throw IllegalStateException()
                val competition: PersistCompetitionDto = call.receive<PersistCompetitionDto>()

                competitionService.update(id, competition)
                    ?.let { updatedCompetition -> call.respond(updatedCompetition) }
                    ?: call.respond(HttpStatusCode.NoContent)
            }

            delete("/{id}") {
                val id: UUID = call.parameters["id"]?.run(UUID::fromString) ?: throw IllegalStateException()

                competitionService.delete(id)
                    ?.run { call.response.status(HttpStatusCode.OK) }
                    ?: call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}