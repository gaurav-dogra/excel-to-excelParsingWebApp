package gmailgdogra.service;

import gmailgdogra.AppConstants;
import gmailgdogra.pojo.OutputRow;
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

import static gmailgdogra.AppConstants.DATA_ROW;
import static gmailgdogra.AppConstants.FIRST_NAME_COL_NO;
import static gmailgdogra.AppConstants.HOURS_COMPLETED_COL;
import static gmailgdogra.AppConstants.LOCATION_COL_NO;
import static gmailgdogra.AppConstants.TIME_INFO_START_INDEX;
import static gmailgdogra.AppConstants.TIME_OFF_COL;
import static gmailgdogra.AppConstants.TIME_ON_COL;

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
        sheet.shiftRows(DATA_ROW, sheet.getLastRowNum(), inSwipesCurrentShift.size());
        int counter = 0;
        for (OutputRow data : inSwipesCurrentShift) {
            Row row = sheet.createRow(DATA_ROW + counter);
            counter++;
            row.createCell(LOCATION_COL_NO).setCellValue(data.getLocation());
            row.createCell(FIRST_NAME_COL_NO).setCellValue(data.getFirstName() + " " + data.getLastName());
            row.createCell(TIME_ON_COL).setCellValue(data.getEventDate().substring(TIME_INFO_START_INDEX));
            String timeOff = "Visitors Reception".equals(data.getLocation()) ? "17:30" : "18:00";
            row.createCell(TIME_OFF_COL).setCellValue(timeOff);
            String hoursCompleted = "Visitors Reception".equals(data.getLocation()) ? "10:5" : "12";
            row.createCell(HOURS_COMPLETED_COL).setCellValue(hoursCompleted);
        }
    }

    private static void insertShiftDate(XSSFSheet sheet) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        Row dateRow = sheet.getRow(AppConstants.SHIFT_DATE_ROW);
        dateRow.getCell(0).setCellValue(date.format(formatter));
    }
}
