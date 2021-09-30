package net.gdogra.service;

import net.gdogra.AppConstants;
import net.gdogra.pojo.OutputRow;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;

public class ShiftReportGeneratingService {

    public static XSSFWorkbook write(List<OutputRow> inSwipesCurrentShift) throws IOException {
        FileInputStream templateFile = new FileInputStream("src/main/resources/Shift Report Template.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(templateFile);
        XSSFSheet sheet = workbook.getSheetAt(0);
        insertShiftDate(sheet);
        insertData(sheet, inSwipesCurrentShift);
        templateFile.close();
        return workbook;
    }

    private static void insertData(XSSFSheet sheet, List<OutputRow> inSwipesCurrentShift) {
        Collections.sort(inSwipesCurrentShift);
        int currentRowNo = AppConstants.DATA_START_ROW;
        for (OutputRow data : inSwipesCurrentShift) {
            Row row = sheet.getRow(currentRowNo++);
            row.getCell(AppConstants.LOCATION_COL_NO).setCellValue(data.getLocation());
            row.getCell(AppConstants.FULL_NAME_COL).setCellValue(data.getFirstName() + " " + data.getLastName());
            row.getCell(AppConstants.TIME_ON_COL).setCellValue(data.getEventDate().substring(AppConstants.TIME_INFO_START_INDEX));
            String timeOff = "Visitors Reception".equals(data.getLocation()) ? "17:30" : "18:00";
            row.getCell(AppConstants.TIME_OFF_COL).setCellValue(timeOff);
            String hoursCompleted = "Visitors Reception".equals(data.getLocation()) ? "10:5" : "12";
            row.getCell(AppConstants.HOURS_COMPLETED_COL).setCellValue(hoursCompleted);
        }
    }

    private static void insertShiftDate(XSSFSheet sheet) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        Row dateRow = sheet.getRow(AppConstants.SHIFT_DATE_ROW);
        dateRow.getCell(0).setCellValue(date.format(formatter));
    }
}
