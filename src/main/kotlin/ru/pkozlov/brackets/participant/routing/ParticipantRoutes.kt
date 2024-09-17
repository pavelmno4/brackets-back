package ru.pkozlov.brackets.participant.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.participant.dto.CreateParticipantDto
import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.participant.dto.PatchParticipantDto
import ru.pkozlov.brackets.participant.dto.criteria.*
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

                    val criteria: Set<Criteria<*>> = setOfNotNull(
                        call.request.queryParameters["gender"]?.run(Gender::valueOf)?.run(::GenderCriteria),
                        call.request.queryParameters["ageCategory"]?.run(::AgeCategory)?.run(::AgeCategoryCriteria),
                        call.request.queryParameters["weightCategory"]?.run(::WeightCategory)?.run(::WeightCategoryCriteria),
                        call.request.queryParameters["team"]?.run(::TeamCriteria),
                    )

                    participantService.findAllByCriteria(competitionId, criteria)
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

                    val participant: CreateParticipantDto = call.receive<CreateParticipantDto>()

                    participantService.create(competitionId, participant)
                        .let { createdPaticipant -> call.respond(createdPaticipant) }

                } catch (exc: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            authenticate("auth-session") {
                patch("/{id}") {
                    try {
                        val id: UUID = call.parameters["id"]
                            ?.run(UUID::fromString)
                            ?: run { call.respond(HttpStatusCode.BadRequest); return@patch }

                        val participant: PatchParticipantDto = call.receive<PatchParticipantDto>()

                        participantService.update(id, participant)
                            ?.let { updatedParticipant -> call.respond(updatedParticipant) }
                            ?: call.respond(HttpStatusCode.NoContent)

                    } catch (exc: IllegalArgumentException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }

            authenticate("auth-session") {
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

            get("/teams") {
                try {
                    val competitionId: UUID = call.parameters["competitionId"]
                        ?.run(UUID::fromString)
                        ?: run { call.respond(HttpStatusCode.BadRequest); return@get }

                    participantService.findAllByCriteria(competitionId, emptySet())
                        .map(ParticipantDto::team).toSet()
                        .let { teams -> call.respond(teams) }

                } catch (exc: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}