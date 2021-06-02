package gmailgdogra;

import org.apache.poi.hssf.util.HSSFColor;
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
    private static final int TITLE_ROW = 0;

    public static boolean write(List<SwipeRecord> outputData) throws IOException {

        addToSheet(OutputRow.of("First Name", "Last Name", "Event Date",
                "Logical Device"), TITLE_ROW);
        writeData(outputData);
        autoSizeColumns();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        try (FileOutputStream outputStream = new FileOutputStream("Securitas Report " + date + ".xlsx")) {
            workbook.write(outputStream);
            System.out.println("Excel file created");
        }

        return true;
    }

    private static void autoSizeColumns() {
        for (int column = 0; column < 4; column++) {
            sheet.autoSizeColumn(column);
        }
    }

    private static void writeData(List<SwipeRecord> outputData) {
        int rowNo = 1;
        for (SwipeRecord record : outputData) {
            String firstName = record.getFirstName();
            String lastName = record.getLastName();
            String swipeTime = record.getSwipeDateTime()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            String deviceName = record.getDeviceName();
            addToSheet(OutputRow.of(firstName, lastName, swipeTime, deviceName), rowNo);
            rowNo++;
        }
    }

    private static void addToSheet(OutputRow outputRow, int rowNo) {
        Row row = sheet.createRow(rowNo);

        for (int column = 0; column < 4; column++) {
            row.createCell(column);
        }

        row.getCell(0).setCellValue(outputRow.getFirstName());
        row.getCell(1).setCellValue(outputRow.getLastName());
        row.getCell(2).setCellValue(outputRow.getSwipeTime());
        row.getCell(3).setCellValue(outputRow.getDeviceName());

        applyStyle(row);
    }

    private static void applyStyle(Row row) {
        int rowNo = row.getRowNum();
        if (rowNo == TITLE_ROW) {
            applyHeaderStyle(row);
            return;
        }

        if (rowNo % 2 != 0) { // rows for swipe-ins
            final int TIME_INFO_START_INDEX = 11;
            final int CELL_FOR_SWIPE_DATE_TIME = 3;

            String time = row.getCell(CELL_FOR_SWIPE_DATE_TIME)
                    .toString().substring(TIME_INFO_START_INDEX);
            if (!time.equals("06:00") && !time.equals("18:00") // bang on time
                    && !time.startsWith("05") && !time.startsWith("17")) { // up to one hour early
                applyLateSwipeInStyle(row);
            }
        }
    }

    private static void applyLateSwipeInStyle(Row row) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex());
        style.setFont(font);
        applyStyleToRow(row, style);
    }

    private static void applyStyleToRow(Row row, CellStyle style) {
        for (int column = 0; column < 4; column++) {
            row.getCell(column).setCellStyle(style);
        }
    }

    private static void applyHeaderStyle(Row row) {
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.index);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(headerFont);
        applyStyleToRow(row, headerStyle);
    }
}
