package ru.pkozlov.brackets.file.service

import ru.pkozlov.brackets.app.config.OutputConfig
import ru.pkozlov.brackets.competition.dto.CompetitionDto
import ru.pkozlov.brackets.grid.dto.GridDto
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class FileService(
    private val outputConfig: OutputConfig
) {
    fun getGridsContentInPdf(competition: CompetitionDto, grids: List<GridDto>) {
        FileOutputStream("${outputConfig.path}/${competition.title}_${competition.startDate}.zip")
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