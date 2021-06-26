package gmailgdogra;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class WriteOutputToXlsx {

    private static final OutputRow titleRow = OutputRow.of("Location", "First Name",
            "Last Name", "Event Date", "Logical Device");

    private static int rowsCreatedSoFar = 0;
    private static final int SHEET_INDEX = 0;
    private static final int LOCATION_COL_NO = 0;
    private static final int FIRST_NAME_COL_NO = 1;
    private static final int LAST_NAME_COL_NO = 2;
    private static final int EVENT_DATE_COL_NO = 3;
    private static final int LOGICAL_DEVICE_COL_NO = 4;
    private static final int TIME_INFO_START_INDEX = 11;


    private static XSSFWorkbook workbook;

    public static byte[] write(List<OutputRow> outputData) {
        System.out.println("WriteOutputToXlsx.write");
        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            addRow(sheet, titleRow);
            addRows(sheet, outputData);
            autoSizeColumns(sheet);
            applyStyles(sheet);
            workbook.write(out);
            workbook.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("failed to write to output file " + e.getMessage());
        }
    }

    private static void addRows(Sheet sheet, List<OutputRow> outputData) {
        Collections.sort(outputData);
        for (int i = 2; i < outputData.size(); i++) {
            if (!outputData.get(i).getLocation().equals(outputData.get(i - 1).getLocation())) {
                addEmptyRow(sheet);
            }
            addRow(sheet, outputData.get(i));
        }
    }

    private static void addEmptyRow(Sheet sheet) {
        Row row = getNewRow(sheet);
        for (int col = 0; col < 5; col++) {
            row.createCell(col);
        }
    }

    private static Row getNewRow(Sheet sheet) {
        return sheet.createRow(rowsCreatedSoFar++);
    }

    private static void addRow(Sheet sheet, OutputRow data) {
        Row row = getNewRow(sheet);

        for (int col = 0; col < 5; col++) {
            row.createCell(col);
        }

        row.getCell(LOCATION_COL_NO).setCellValue(data.getLocation());
        row.getCell(FIRST_NAME_COL_NO).setCellValue(data.getFirstName());
        row.getCell(LAST_NAME_COL_NO).setCellValue(data.getLastName());
        String swipeEvent = data.getEventDate() == null ? "No Swipe Found" : data.getEventDate();
        row.getCell(EVENT_DATE_COL_NO).setCellValue(swipeEvent);
        row.getCell(LOGICAL_DEVICE_COL_NO).setCellValue(data.getLogicalDevice());
    }

    private static void applyStyles(Sheet sheet) {
        XSSFCellStyle titleRowStyle = createTitleRowStyle();
        XSSFCellStyle highlightedRowStyle = createHighlightedRowStyle();
        XSSFCellStyle normalRowStyle = createNormalRowStyle();

        for (Row row : sheet) {
            int currentRowNo = row.getRowNum();
            if (currentRowNo == 0) {
                applyStyle(currentRowNo, titleRowStyle);
                continue;
            }

            String locationCellValue = row.getCell(LOCATION_COL_NO).getStringCellValue();
            String swipeEventCellValue = row.getCell(EVENT_DATE_COL_NO).getStringCellValue();
            if ("".equals(locationCellValue)) {
                applyStyle(currentRowNo, titleRowStyle);
                continue;
            }

            if ("No Swipe Found".equals(swipeEventCellValue) ||
                    isLateSwipeIn(currentRowNo)) {
                applyStyle(currentRowNo, highlightedRowStyle);
            } else {
                applyStyle(currentRowNo, normalRowStyle);
            }
        }
    }

    private static boolean isLateSwipeIn(int rowNo) {

        Row row = workbook.getSheetAt(SHEET_INDEX).getRow(rowNo);
        String locationCellVal = row.getCell(LOCATION_COL_NO).getStringCellValue();
        String timeInfo = row.getCell(EVENT_DATE_COL_NO).getStringCellValue().substring(TIME_INFO_START_INDEX);
        String logicalDevCellVal = row.getCell(LOGICAL_DEVICE_COL_NO).getStringCellValue();
        LocalTime swipeInTime = LocalTime.parse(timeInfo);

        if (logicalDevCellVal.endsWith("OUT")) {
            return false;
        } else if (swipeInTime.isAfter(LocalTime.parse("04:00")) && swipeInTime.isBefore(LocalTime.parse("06:01"))) {
            return false;
        } else if (swipeInTime.isAfter(LocalTime.parse("16:00")) && swipeInTime.isBefore(LocalTime.parse("18:01"))) {
            return false;
        } else return !"Visitors Reception".equals(locationCellVal) || !swipeInTime.isBefore(LocalTime.parse("07:01"));
    }

    private static XSSFCellStyle createNormalRowStyle() {
        XSSFCellStyle style = workbook.createCellStyle();
        setBorder(style);
        return style;
    }

    private static XSSFCellStyle createHighlightedRowStyle() {
        XSSFCellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.DARK_RED.index);
        style.setFont(font);
        setBorder(style);
        return style;
    }

    private static void setBorder(XSSFCellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private static void applyStyle(int rowNo, XSSFCellStyle titleRowStyle) {
        Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
        Row row = sheet.getRow(rowNo);
        for (int i = 0; i < 5; i++) {
            row.getCell(i).setCellStyle(titleRowStyle);
        }
    }

    private static XSSFCellStyle createTitleRowStyle() {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.YELLOW.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        setBorder(style);
        return style;
    }

    private static void autoSizeColumns(Sheet sheet) {
        for (int column = 0; column < 5; column++) {
            sheet.autoSizeColumn(column);
        }
    }
}
