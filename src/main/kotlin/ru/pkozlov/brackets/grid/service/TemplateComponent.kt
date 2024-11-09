package ru.pkozlov.brackets.grid.service

import org.apache.poi.ss.usermodel.WorkbookFactory
import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder
import ru.pkozlov.brackets.grid.dto.GridDto
import ru.pkozlov.brackets.grid.util.addSheet
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * Inherits code from [brackets-excel](https://github.com/pavelmno4/brackets-excel)
 * */
class TemplateComponent {
    companion object Companion {
        private const val XSSF = true
        private const val TEMPLATE_SHEET_INDEX = 0
    }

    fun process(grids: List<GridDto>): ByteArray =
        ByteArrayOutputStream().let { outputStream ->
            WorkbookFactory.create(XSSF).let { mainWorkbook ->
                grids.forEach { grid ->
                    TemplateComponent::class.java.getResourceAsStream(grid.templatePath).let { template ->
                        grid.graph.flat(HashMap()) { it.level.name }.let { flatGraph ->
                            HashMap<String, Any>().let { context ->
                                context["tournamentName"] = grid.tournamentName
                                context["birthYearRange"] = grid.ageCategory.value
                                context["weightCategory"] = grid.weightCategory.value
                                context["graph"] = flatGraph

                                JxlsPoiTemplateFillerBuilder.newInstance()
                                    .withTemplate(template)
                                    .buildAndFill(context)
                                    .run(::ByteArrayInputStream)
                                    .use(WorkbookFactory::create)
                                    .apply {
                                        setSheetName(TEMPLATE_SHEET_INDEX, grid.weightCategory.value)
                                        getSheetAt(TEMPLATE_SHEET_INDEX).run(mainWorkbook::addSheet)
                                        close()
                                    }
                            }
                        }
                    }
                }

                mainWorkbook.write(outputStream)
                mainWorkbook.close()
                outputStream.toByteArray()
            }
        }
}