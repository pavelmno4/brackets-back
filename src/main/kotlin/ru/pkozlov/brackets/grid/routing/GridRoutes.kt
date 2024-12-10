package ru.pkozlov.brackets.grid.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.grid.dto.PatchGridMedalistsDto
import ru.pkozlov.brackets.grid.dto.PatchNodeWinnerDto
import ru.pkozlov.brackets.grid.mapper.asView
import ru.pkozlov.brackets.grid.service.GridService
import java.util.*

fun Application.gridRoutes() {
    val gridService: GridService by inject()

    routing {
        route("/competitions/{competitionId}/grids") {
            get {
                try {
                    val competitionId = call.parameters["competitionId"]
                        ?.run(UUID::fromString)
                        ?: throw IllegalStateException("Param 'competitionId' is required")

                    val gender = call.request.queryParameters["gender"]?.run(Gender::valueOf)
                        ?: throw IllegalStateException("Param 'gender' is required")

                    val ageCategory = call.request.queryParameters["ageCategory"]?.run(::AgeCategory)
                        ?: throw IllegalStateException("Param 'ageCategory' is required")

                    val weightCategory = call.request.queryParameters["weightCategory"]?.run(::WeightCategory)
                        ?: throw IllegalStateException("Param 'weightCategory' is required")

                    gridService.findBy(competitionId, gender, ageCategory, weightCategory)
                        ?.let { competition -> call.respond(competition.asView()) }
                        ?: call.respond(HttpStatusCode.NoContent)

                } catch (exc: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest) { exc.message }
                }
            }

            authenticate("auth-session") {
                post {
                    val competitionId: UUID = call.parameters["competitionId"]
                        ?.run(UUID::fromString)
                        ?: run { call.respond(HttpStatusCode.BadRequest); return@post }

                    gridService.generate(competitionId)
                        .let { grids -> call.respond(grids) }
                }
            }

            authenticate("auth-session") {
                patch("/{gridId}/medalists") {
                    val gridId: UUID = call.parameters["gridId"]
                        ?.run(UUID::fromString)
                        ?: run { call.respond(HttpStatusCode.BadRequest); return@patch }

                    val patchMedalistsDto: PatchGridMedalistsDto = call.receive<PatchGridMedalistsDto>()

                    gridService.patchMedalists(gridId, patchMedalistsDto)
                        ?.let { updatedGrid -> call.respond(updatedGrid) }
                        ?: call.respond(HttpStatusCode.NoContent)
                }
            }

            authenticate("auth-session") {
                patch("/{gridId}/nodes/{nodeId}/winner") {
                    val gridId: UUID = call.parameters["gridId"]
                        ?.run(UUID::fromString)
                        ?: run { call.respond(HttpStatusCode.BadRequest); return@patch }

                    val nodeId: UUID = call.parameters["nodeId"]
                        ?.run(UUID::fromString)
                        ?: run { call.respond(HttpStatusCode.BadRequest); return@patch }

                    val winnerNodeId: UUID = call.receive<PatchNodeWinnerDto>().winnerNodeId

                    gridService.setWinnerForNode(gridId, nodeId, winnerNodeId)
                        ?.let { updatedGrid -> call.respond(updatedGrid) }
                        ?: call.respond(HttpStatusCode.NoContent)
                }
            }

            authenticate("auth-session") {
                patch("/{gridId}/nodes/{nodeId}/participant") {
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}