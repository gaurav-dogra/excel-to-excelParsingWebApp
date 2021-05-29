package gmailgdogra;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// this file reads firstName(col no. 0), lastName(col no. 1), Event Date(col no. 2)
// and deviceName(col no. 5) parse a given file
public class ReadXlsx {

    // actual data always start parse row 7, Note: Rows index start at 0
    private static final int ROW_START = 7;

    public static List<SwipeRecord> parse(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = getFirstSheet(workbook);
        return collectDataFrom(firstSheet);
    }

    private static List<SwipeRecord> collectDataFrom(Sheet sheet) {
        List<SwipeRecord> data = new ArrayList<>();
        for (int rowIndex = ROW_START; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                String firstName = row.getCell(0).getStringCellValue();
                String lastName = row.getCell(1).getStringCellValue();
                LocalDateTime swipeDateAndTime = row.getCell(2).getLocalDateTimeCellValue();
                String deviceName = row.getCell(5).getStringCellValue();
                data.add(new SwipeRecord(firstName, lastName, swipeDateAndTime, deviceName));
            }
        }
        return data;
    }

    private static Sheet getFirstSheet(Workbook workbook) {
        return workbook.getSheetAt(0);
    }
}
