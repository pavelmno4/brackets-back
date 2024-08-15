package ru.pkozlov.competition

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.koin.core.context.stopKoin
import ru.pkozlov.competition.dto.category.Category
import ru.pkozlov.competition.dto.competition.CompetitionDto
import ru.pkozlov.competition.dto.competition.PersistCompetitionDto
import java.time.LocalDate
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class CompetitionTest {
    
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `find all exclute inactive`() = testApplication {
        initPlugins()

        val client = initClient()

        val response = client.get("/competitions?showInactive=false")
        val results = response.body<List<CompetitionDto>>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedCompetitionTitles = listOf("Турнир номер 1")
        val actualCompetitionTitles = results.map(CompetitionDto::title)
        assertContentEquals(expectedCompetitionTitles, actualCompetitionTitles)
    }

    @Test
    fun `find all include inactive`() = testApplication {
        initPlugins()

        val client = initClient()

        val response = client.get("/competitions?showInactive=true")
        val results = response.body<List<CompetitionDto>>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedCompetitionTitles = listOf("Турнир номер 1", "Турнир номер 2")
        val actualCompetitionTitles = results.map(CompetitionDto::title)
        assertContentEquals(expectedCompetitionTitles, actualCompetitionTitles)
    }

    @Test
    fun `find by id`() = testApplication {
        initPlugins()

        val client = initClient()

        val response = client.get("/competitions/eff81db2-e3f5-47eb-bdd8-827ed5dfb322")
        val result = response.body<CompetitionDto>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedCompetitionTitle = "Турнир номер 1"
        val actualCompetitionTitle = result.title
        assertEquals(expectedCompetitionTitle, actualCompetitionTitle)
    }

    @Test
    fun `competition not found by id`() = testApplication {
        initPlugins()

        val client = initClient()

        val response = client.get("/competitions/eff81db2-e3f5-47eb-bdd8-827ed5dfb324")

        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun `create competition`() = testApplication {
        initPlugins()

        val client = initClient()

        val response = client.post("/competitions") {
            contentType(ContentType.Application.Json)
            setBody(
                PersistCompetitionDto(
                    title = "Турнир номер 3",
                    startDate = LocalDate.parse("2024-10-05"),
                    endDate = LocalDate.parse("2024-10-05"),
                    address = "Московская обл., п. Большевик",
                    imagePath = "/image",
                    categories = listOf(
                        Category(
                            yearRange = "2014-2013",
                            weights = setOf("36", "40")
                        )
                    )
                )
            )
        }
        val result = response.body<CompetitionDto>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedCompetitionTitle = "Турнир номер 3"
        val actualCompetitionTitle = result.title
        assertEquals(expectedCompetitionTitle, actualCompetitionTitle)
    }

    @Test
    fun `successfully update competition`() = testApplication {
        initPlugins()

        val client = initClient()

        val oldTitle = client.get("/competitions/eff81db2-e3f5-47eb-bdd8-827ed5dfb322")
            .body<CompetitionDto>()
            .title

        val response = client.put("/competitions/eff81db2-e3f5-47eb-bdd8-827ed5dfb322") {
            contentType(ContentType.Application.Json)
            setBody(
                PersistCompetitionDto(
                    title = "Турнир номер 3",
                    startDate = LocalDate.parse("2024-10-05"),
                    endDate = LocalDate.parse("2024-10-05"),
                    address = "Московская обл., п. Большевик",
                    imagePath = "/image",
                    categories = listOf(
                        Category(
                            yearRange = "2014-2013",
                            weights = setOf("36", "40")
                        )
                    )
                )
            )
        }
        val result = response.body<CompetitionDto>()

        assertEquals(HttpStatusCode.OK, response.status)

        assertEquals("Турнир номер 1", oldTitle)
        assertEquals("Турнир номер 3", result.title)
    }

    @Test
    fun `competition not found on update`() = testApplication {
        initPlugins()

        val client = initClient()

        val response = client.put("/competitions/eff81db2-e3f5-47eb-bdd8-827ed5dfb324") {
            contentType(ContentType.Application.Json)
            setBody(
                PersistCompetitionDto(
                    title = "Турнир номер 3",
                    startDate = LocalDate.parse("2024-10-05"),
                    endDate = LocalDate.parse("2024-10-05"),
                    address = "Московская обл., п. Большевик",
                    imagePath = "/image",
                    categories = listOf(
                        Category(
                            yearRange = "2014-2013",
                            weights = setOf("36", "40")
                        )
                    )
                )
            )
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun `successfully delete`() = testApplication {
        initPlugins()

        val client = initClient()

        val beforeDelete = client.get("/competitions/eff81db2-e3f5-47eb-bdd8-827ed5dfb322").body<CompetitionDto>()
        
        assertEquals(false, beforeDelete.deleted)
        
        val afterDeleteResponse = client.delete("/competitions/eff81db2-e3f5-47eb-bdd8-827ed5dfb322")

        assertEquals(HttpStatusCode.OK, afterDeleteResponse.status)
    }
    
    @Test
    fun `competition not found on delete`() = testApplication {
        initPlugins()

        val client = initClient()
        
        val afterDeleteResponse = client.delete("/competitions/eff81db2-e3f5-47eb-bdd8-827ed5dfb324")

        assertEquals(HttpStatusCode.NoContent, afterDeleteResponse.status)
    }
}