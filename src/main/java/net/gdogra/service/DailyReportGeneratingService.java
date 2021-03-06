package net.gdogra.service;

import net.gdogra.pojo.OutputRow;
import net.gdogra.AppConstants;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class DailyReportGeneratingService {

    private static final OutputRow titleRow = OutputRow.of("Location", "First Name",
            "Last Name", "Event Date", "Logical Device");

    private static int rowsCreatedSoFar;

    public static XSSFWorkbook write(List<OutputRow> outputData) {
        rowsCreatedSoFar = 0;
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Daily Report");
        writeRow(sheet, titleRow);
        writeDataRows(sheet, outputData);
        autoSizeColumns(sheet);
        return workbook;
    }

    private static void writeDataRows(Sheet sheet, List<OutputRow> outputData) {
        for (OutputRow outputDatum : outputData) {
            writeRow(sheet, outputDatum);
        }
    }

    private static Row getNewRow(Sheet sheet) {
        return sheet.createRow(rowsCreatedSoFar++);
    }

    private static void writeRow(Sheet sheet, OutputRow rowData) {
        Row row = getNewRow(sheet);

        for (int col = 0; col < 5; col++) {
            row.createCell(col);
        }

        row.getCell(AppConstants.LOCATION_COL_NO).setCellValue(rowData.getLocation());
        row.getCell(AppConstants.FIRST_NAME_COL_NO).setCellValue(rowData.getFirstName());
        row.getCell(AppConstants.LAST_NAME_COL_NO).setCellValue(rowData.getLastName());
        String swipeEvent = rowData.getEventDate() == null ? "No Swipe Found" : rowData.getEventDate();
        row.getCell(AppConstants.EVENT_DATE_COL_NO).setCellValue(swipeEvent);
        row.getCell(AppConstants.LOGICAL_DEVICE_COL_NO).setCellValue(rowData.getLogicalDevice());
    }

    private static void autoSizeColumns(Sheet sheet) {
        for (int column = 0; column < 5; column++) {
            sheet.autoSizeColumn(column);
        }
    }
}
