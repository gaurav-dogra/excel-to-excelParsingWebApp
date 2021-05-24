package gmailgdogra;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WriteOutputXlsx {

    private static final XSSFWorkbook workbook = new XSSFWorkbook();
    private static final XSSFSheet sheet = workbook.createSheet("Securitas Daily Report");

    public static boolean write(List<SwipeRecord> outputData) throws IOException {
        addToSheet(OutputRow.of("First Name", "Last Name", "Event Date",
                "Logical Device"), 0); // title row
        writeData(outputData);
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        try (FileOutputStream outputStream = new FileOutputStream("Securitas Report " + date + ".xlsx")) {
            workbook.write(outputStream);
        }
        
        return true;
    }

    private static void writeData(List<SwipeRecord> outputData) {

        for (int record = 0; record < outputData.size(); record++) {
            String firstName = outputData.get(record).getFirstName();
            String lastName = outputData.get(record).getLastName();
            String swipeTime = outputData.get(record).getSwipeTime()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            String deviceName = outputData.get(record).getDeviceName();
            addToSheet(OutputRow.of(firstName, lastName, swipeTime, deviceName), record + 1);
        }
    }

    private static void addToSheet(OutputRow row, int rowNo) {
        Row titleRow = sheet.createRow(rowNo);

        Cell firstNameCell = titleRow.createCell(0);
        firstNameCell.setCellValue(row.getFirstName());

        Cell lastNameCell = titleRow.createCell(1);
        lastNameCell.setCellValue(row.getLastName());

        Cell swipeTimeCell = titleRow.createCell(2);
        swipeTimeCell.setCellValue(row.getSwipeTime());

        Cell deviceNameCell = titleRow.createCell(3);
        deviceNameCell.setCellValue(row.getDeviceName());
    }
}
