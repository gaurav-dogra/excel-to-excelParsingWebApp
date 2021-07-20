package gmailgdogra.service;

import gmailgdogra.pojo.OutputRow;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class ShiftReportGeneratingService {

    public static XSSFWorkbook write(List<OutputRow> inSwipesCurrentShift) throws IOException {
        FileInputStream templateFile = new FileInputStream("src/main/resources/Shift Report Template.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(templateFile);
        XSSFSheet sheet = workbook.getSheetAt(0);
        insertShiftDate(sheet);
        templateFile.close();
        return workbook;
    }

    private static void insertShiftDate(XSSFSheet sheet) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        Row dateRow = sheet.getRow(3);
        dateRow.getCell(0).setCellValue(date.format(formatter));
    }
}
