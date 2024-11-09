package ru.pkozlov.brackets.grid.util

import org.apache.poi.ss.usermodel.CellType.*
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

fun Workbook.addSheet(sheet: Sheet, maxRow: Int = 100, maxColumn: Int = 50) {
    createSheet(sheet.sheetName).apply {
        sheet.mergedRegions.forEach(::addMergedRegion)

        sheet.forEach { row ->
            if (row.rowNum < maxRow) row.forEach { cell ->
                if (cell.columnIndex < maxColumn)
                    (getRow(row.rowNum) ?: createRow(row.rowNum))
                        .createCell(cell.columnIndex, cell.cellType).apply {
                            cellStyle = createCellStyle().apply { cloneStyleFrom(cell.cellStyle) }

                            when (cell.cellType) {
                                _NONE -> setBlank()
                                NUMERIC -> setCellValue(cell.numericCellValue)
                                STRING -> setCellValue(cell.stringCellValue)
                                FORMULA -> setCellFormula(cell.cellFormula)
                                BLANK -> setBlank()
                                BOOLEAN -> setCellValue(cell.booleanCellValue)
                                ERROR -> setCellErrorValue(cell.errorCellValue)
                                null -> setBlank()
                            }
                        }
            }
        }
    }
}