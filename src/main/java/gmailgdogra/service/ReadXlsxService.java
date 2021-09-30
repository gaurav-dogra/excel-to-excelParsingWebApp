package gmailgdogra.service;

import gmailgdogra.pojo.Officer;
import gmailgdogra.pojo.Swipe;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static gmailgdogra.AppConstants.DATA_START_ROW;

@Service
public class ReadXlsxService {

    private static final int COL_FIRST_NAME = 0;
    private static final int COL_LAST_NAME = 1;
    private static final int COL_SWIPE_DATE_TIME = 2;
    private static final int COL_DEVICE_NAME = 5;

    public List<Swipe> readAllRows(InputStream inputStream) throws IOException {
        return collectDataFrom(new XSSFWorkbook(inputStream).getSheetAt(0));
    }

    private List<Swipe> collectDataFrom(Sheet sheet) throws IOException {
        List<Swipe> data = new ArrayList<>();
        for (int rowIndex = DATA_START_ROW; rowIndex <= sheet.getPhysicalNumberOfRows(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                String firstName = row.getCell(COL_FIRST_NAME).getStringCellValue();
                firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
                String lastName = row.getCell(COL_LAST_NAME).getStringCellValue();
                lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
                Officer officer = new Officer(firstName, lastName);
                LocalDateTime swipeDateAndTime = row.getCell(COL_SWIPE_DATE_TIME).getLocalDateTimeCellValue();
                String deviceName = row.getCell(COL_DEVICE_NAME).getStringCellValue();
                data.add(new Swipe(officer, swipeDateAndTime, deviceName));
            }
        }
        if (data.size() == 0) {
            throw new IOException();
        }
        return data;
    }
}
