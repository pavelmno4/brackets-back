package ru.pkozlov.brackets.participant.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.pkozlov.brackets.participant.dto.PersistParticipantDto
import ru.pkozlov.brackets.participant.enumeration.Gender
import ru.pkozlov.brackets.participant.service.ParticipantService
import java.util.*

fun Application.participantRoutes() {
    val participantService: ParticipantService by inject()

    routing {
        route("/competitions/{competitionId}/participants") {
            get {
                try {
                    val competitionId: UUID = call.parameters["competitionId"]
                        ?.run(UUID::fromString)
                        ?: run { call.respond(HttpStatusCode.BadRequest); return@get }
                    
                    val gender: Gender? = call.request.queryParameters["gender"]
                        ?.run(Gender::valueOf)

                    participantService.findAllByCompetitionId(competitionId)
                        .let { participants -> call.respond(participants) }
                    
                } catch (exc: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post {
                try {
                    val competitionId: UUID = call.parameters["competitionId"]
                        ?.run(UUID::fromString)
                        ?: run { call.respond(HttpStatusCode.BadRequest); return@post }
                    
                    val participant: PersistParticipantDto = call.receive<PersistParticipantDto>()

                    participantService.create(competitionId, participant)
                        .let { createdPaticipant -> call.respond(createdPaticipant) }
                    
                } catch (exc: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            put("/{id}") {
                try {
                    val id: UUID = call.parameters["id"]
                        ?.run(UUID::fromString)
                        ?: run { call.respond(HttpStatusCode.BadRequest); return@put }

                    val participant: PersistParticipantDto = call.receive<PersistParticipantDto>()

                    participantService.update(id, participant)
                        ?.let { updatedParticipant -> call.respond(updatedParticipant) }
                        ?: call.respond(HttpStatusCode.NoContent)
                    
                } catch (exc: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            delete("/{id}") {
                try {
                    val id: UUID = call.parameters["id"]
                        ?.run(UUID::fromString)
                        ?: run { call.respond(HttpStatusCode.BadRequest); return@delete }
    
                    participantService.delete(id)
                        ?.run { call.response.status(HttpStatusCode.OK) }
                        ?: call.respond(HttpStatusCode.NoContent)
                    
                } catch (exc: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}