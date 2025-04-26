package ru.pkozlov.brackets.competition

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkClass
import org.junit.Rule
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.competition.dto.category.CategoriesByGender
import ru.pkozlov.brackets.competition.dto.category.Category
import ru.pkozlov.brackets.competition.dto.CompetitionDto
import ru.pkozlov.brackets.competition.dto.PersistCompetitionDto
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class CompetitionTest : KoinTest {
    @Rule
    fun koin() = KoinTestRule.create {
        initKoinModules()
    }

    @Rule
    fun mockProvider() = MockProviderRule.create { clazz -> mockkClass(clazz) }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `find all upcoming`() = testApplication {
        initPlugins()

        val client = initClient()

        val now = declareMock<() -> LocalDateTime>()
        every { now() } returns LocalDateTime.parse("2024-09-15T12:15:00")

        val response = client.get("/competitions/upcoming")
        val results = response.body<List<CompetitionDto>>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedCompetitionTitles = listOf("Турнир номер 1")
        val actualCompetitionTitles = results.map(CompetitionDto::title)
        assertContentEquals(expectedCompetitionTitles, actualCompetitionTitles)
    }

    @Test
    fun `find all past`() = testApplication {
        initPlugins()

        val client = initClient()

        val now = declareMock<() -> LocalDateTime>()
        every { now() } returns LocalDateTime.parse("2024-09-15T12:15:00")

        val response = client.get("/competitions/past")
        val results = response.body<List<CompetitionDto>>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedCompetitionTitles = listOf("Турнир номер 2")
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
                    categories = CategoriesByGender(
                        male = listOf(
                            Category(
                                yearRange = AgeCategory("2014-2013"),
                                weights = setOf(WeightCategory("36"), WeightCategory("40"))
                            )
                        ),
                        female = emptyList()
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
                    categories = CategoriesByGender(
                        male = listOf(
                            Category(
                                yearRange = AgeCategory("2014-2013"),
                                weights = setOf(WeightCategory("36"), WeightCategory("40"))
                            )
                        ),
                        female = emptyList()
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
                    categories = CategoriesByGender(
                        male = listOf(
                            Category(
                                yearRange = AgeCategory("2014-2013"),
                                weights = setOf(WeightCategory("36"), WeightCategory("40"))
                            )
                        ),
                        female = emptyList()
                    )
                )
            )
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
    }
}