package ru.pkozlov.brackets.file.service

import ru.pkozlov.brackets.app.config.OutputConfig
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.competition.service.CompetitionService
import ru.pkozlov.brackets.grid.service.GridService
import java.io.FileOutputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class FileService(
    private val competitionService: CompetitionService,
    private val gridService: GridService,
    private val outputConfig: OutputConfig
) {
    suspend fun getGridsContentInPdf(
        competitionId: UUID,
        gender: Gender?,
        ageCategory: AgeCategory?,
        weightCategory: WeightCategory?
    ) = suspendTransaction {
        competitionService.findById(competitionId)?.let { competition ->
            gridService.findBy(competition.id, gender, ageCategory, weightCategory).let { grids ->
                FileOutputStream("${outputConfig.path}/${competition.startDate}_${competition.id}.zip")
                    .run(::ZipOutputStream)
                    .use { zipOutput ->
                        grids.forEach { grid ->
                            val zipEntry = ZipEntry("${competition.title}/${grid.gender}/${grid.ageCategory.value}/${grid.weightCategory.value}.html")
                            zipOutput.putNextEntry(zipEntry)

                            GridHtmlComponent.createHtml(competition, grid).byteInputStream().copyTo(zipOutput)

                            zipOutput.closeEntry()
                        }
                    }
            }
        }
    }
}