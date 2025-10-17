package ru.pkozlov.brackets.file.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.auth.enumeration.Role.EDITOR
import ru.pkozlov.brackets.auth.service.UserService
import ru.pkozlov.brackets.auth.utils.hasRoles
import ru.pkozlov.brackets.file.service.FileService
import java.util.*

fun Application.fileRoutes() {
    val fileService: FileService by inject()
    val userService: UserService by inject()

    routing {
        route("/competitions/{competitionId}/files") {

            authenticate("auth-session") {
                get("/grids") {
                    hasRoles(setOf(EDITOR), userService)

                    val competitionId = call.parameters["competitionId"]?.run(UUID::fromString)
                        ?: throw IllegalStateException("Param 'competitionId' is required")
                    val gender = call.request.queryParameters["gender"]?.run(Gender::valueOf)
                    val ageCategory = call.request.queryParameters["ageCategory"]?.run(::AgeCategory)
                    val weightCategory = call.request.queryParameters["weightCategory"]?.run(::WeightCategory)

                    fileService.getGridsContentInPdf(competitionId, gender, ageCategory, weightCategory)
                        .let { call.respond(HttpStatusCode.OK, "Grid files generated") }
                }
            }
        }
    }
}