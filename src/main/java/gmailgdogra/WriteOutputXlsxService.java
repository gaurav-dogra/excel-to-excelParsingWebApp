package gmailgdogra;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WriteOutputXlsxService {

    private static final String SHEET_NAME = "Securitas Daily Report";
    private static final int TITLE_ROW = 0;
    private static final Workbook workbook = new XSSFWorkbook();

    public static byte[] write(List<SwipeRecord> outputData) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET_NAME);
            addToSheet(sheet, OutputRow.of("First Name", "Last Name", "Event Date",
                    "Logical Device"), TITLE_ROW);
            writeData(sheet, outputData);
            autoSizeColumns(sheet);
            workbook.write(out);
            workbook.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("fail to write data to Excel file " + e.getMessage());
        }
    }

    private static void autoSizeColumns(Sheet sheet) {
        for (int column = 0; column < 4; column++) {
            sheet.autoSizeColumn(column);
        }
    }

    private static void writeData(Sheet sheet, List<SwipeRecord> outputData) {
        int rowNo = 1;
        for (SwipeRecord record : outputData) {
            String swipeTime;
            if (record.getSwipeDateTime() == null) {
                swipeTime = "Did Not Swipe";
            } else {
                swipeTime = record.getSwipeDateTime()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            }
            addToSheet(sheet, OutputRow.of(record.getFirstName(), record.getLastName(), swipeTime, record.getDeviceName()), rowNo);
            rowNo++;
        }
    }

    private static void addToSheet(Sheet sheet, OutputRow outputRow, int rowNo) {
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
            createHeaderStyle(row);
            return;
        }

        if (rowNo % 2 != 0) { // rows for swipe-ins
            final int TIME_INFO_START_INDEX = 11;
            final int CELL_FOR_SWIPE_DATE_TIME = 2;

            String time = row.getCell(CELL_FOR_SWIPE_DATE_TIME)
                    .getStringCellValue();

            if (time.equals("Did Not Swipe")) {
                createRowHighlight(row);
            } else {
                time = time.substring(TIME_INFO_START_INDEX);
                if (!time.equals("06:00") && !time.equals("18:00")
                        && !time.startsWith("04") && !time.startsWith("05")
                        && !time.startsWith("16") && !time.startsWith("17")) {
                    createRowHighlight(row);
                }
            }
        }
    }

    private static void createRowHighlight(Row row) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex());
        style.setFont(font);
        applyGivenStyle(row, style);
    }

    private static void applyGivenStyle(Row row, CellStyle style) {
        for (int column = 0; column < 4; column++) {
            row.getCell(column).setCellStyle(style);
        }
    }

    private static void createHeaderStyle(Row row) {
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.index);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(headerFont);
        applyGivenStyle(row, headerStyle);
    }
}
