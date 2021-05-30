package gmailgdogra;

import org.apache.poi.ss.usermodel.*;
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
        autoSizeColumns();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        try (FileOutputStream outputStream = new FileOutputStream("Securitas Report " + date + ".xlsx")) {
            workbook.write(outputStream);
        }

        return true;
    }

    private static void autoSizeColumns() {
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
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

    private static void addToSheet(OutputRow outputRow, int rowNo) {
        Row row = sheet.createRow(rowNo);

        Cell firstNameCell = row.createCell(0);
        firstNameCell.setCellValue(outputRow.getFirstName());

        Cell lastNameCell = row.createCell(1);
        lastNameCell.setCellValue(outputRow.getLastName());

        Cell swipeTimeCell = row.createCell(2);
        swipeTimeCell.setCellValue(outputRow.getSwipeTime());

        Cell deviceNameCell = row.createCell(3);
        deviceNameCell.setCellValue(outputRow.getDeviceName());

        applyStyle(row);
    }

    private static void applyStyle(Row row) {
        int rowNo = row.getRowNum();
        if (rowNo == 0) {
            applyHeaderStyle(row);
        }
    }

    private static void applyHeaderStyle(Row titleRow) {
        System.out.println("WriteOutputXlsx.applyHeaderStyle");
        CellStyle style = workbook.createCellStyle();
        style.setLocked(true);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleRow.getCell(0).setCellStyle(style);
        titleRow.getCell(1).setCellStyle(style);
        titleRow.getCell(2).setCellStyle(style);
        titleRow.getCell(3).setCellStyle(style);
    }
}
