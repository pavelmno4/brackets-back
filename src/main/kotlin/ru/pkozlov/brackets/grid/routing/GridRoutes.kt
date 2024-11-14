package ru.pkozlov.brackets.grid.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject
import ru.pkozlov.brackets.grid.service.GridService
import java.util.*

fun Application.gridRoutes() {
    val gridService: GridService by inject()

    routing {
        route("/competitions/{competitionId}/grid") {
            authenticate("auth-session") {
                post {
                    val competitionId: UUID = call.parameters["competitionId"]
                        ?.run(UUID::fromString)
                        ?: run { call.respond(HttpStatusCode.BadRequest); return@post }

                    launch(Dispatchers.IO) {
                        gridService.generate(competitionId)
                    }
                    call.respondText(status = HttpStatusCode.OK) { "Grid generation started for competition $competitionId" }
                }
            }
        }
    }
}