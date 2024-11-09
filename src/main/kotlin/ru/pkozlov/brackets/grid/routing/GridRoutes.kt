package ru.pkozlov.brackets.grid.routing

import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.Attachment
import io.ktor.http.ContentDisposition.Parameters.FileName
import io.ktor.http.HttpHeaders.ContentDisposition
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.pkozlov.brackets.grid.service.GridService
import java.util.*

fun Application.gridRoutes() {
    val gridService: GridService by inject()

    routing {
        route("/competitions/{competitionId}/grid") {
            get {
                val competitionId: UUID = call.parameters["competitionId"]
                    ?.run(UUID::fromString)
                    ?: run { call.respond(HttpStatusCode.BadRequest); return@get }

                call.response.header(
                    name = ContentDisposition,
                    value = Attachment.withParameter(FileName, "competition_grid.zip").toString()
                )
                call.respondOutputStream { gridService.generate(competitionId, buffered()) }
            }
        }
    }
}