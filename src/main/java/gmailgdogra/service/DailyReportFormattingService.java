package gmailgdogra.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalTime;

import static gmailgdogra.AppConstants.EVENT_DATE_COL_NO;
import static gmailgdogra.AppConstants.FIRST_NAME_COL_NO;
import static gmailgdogra.AppConstants.LOCATION_COL_NO;
import static gmailgdogra.AppConstants.LOGICAL_DEVICE_COL_NO;
import static gmailgdogra.AppConstants.TIME_INFO_START_INDEX;
import static gmailgdogra.AppConstants.TITLE_ROW_NO;


public class DailyReportFormattingService {

    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;

    public static XSSFWorkbook of(XSSFWorkbook plainXlsx) {
        workbook = plainXlsx;
        sheet = workbook.getSheetAt(0);
        insertEmptyRowAfterEachLocation();
        applyStyles();
        return workbook;
    }

    private static void insertEmptyRowAfterEachLocation() {
        for (int rowNo = 2; rowNo < sheet.getPhysicalNumberOfRows(); rowNo++) {
            String previousRowLocation = sheet.getRow(rowNo - 1).getCell(0).getStringCellValue();
            String currentRowLocation = sheet.getRow(rowNo).getCell(0).getStringCellValue();
            if (!previousRowLocation.equals(currentRowLocation)) {
                insertEmptyRowBefore(rowNo);
                rowNo++;
            }
        }
    }

    private static void insertEmptyRowBefore(int rowNo) {
        shiftRowsDownByOneFrom(rowNo);
        Row row = sheet.createRow(rowNo);
        for (int col = LOCATION_COL_NO; col <= LOGICAL_DEVICE_COL_NO; col++) {
            row.createCell(col).setBlank();
        }
    }

    private static void shiftRowsDownByOneFrom(int rowNo) {
        sheet.shiftRows(rowNo, sheet.getLastRowNum(), 1);
    }

    private static void applyStyles() {
        XSSFCellStyle titleRowStyle = createTitleRowStyle();
        XSSFCellStyle highlightedRowStyle = createHighlightedRowStyle();
        XSSFCellStyle normalRowStyle = createNormalRowStyle();

        for (Row row : sheet) {

            int currentRowNo = row.getRowNum();

            if (currentRowNo == TITLE_ROW_NO ||
                    rowIsEmpty(currentRowNo)) {
                applyStyle(currentRowNo, titleRowStyle);
                continue;
            }

            if (rowHasNoSwipe(currentRowNo) ||
                    swipeInIsLate(currentRowNo)) {
                applyStyle(currentRowNo, highlightedRowStyle);
            } else {
                applyStyle(currentRowNo, normalRowStyle);
            }
        }
    }

    private static boolean rowHasNoSwipe(int currentRowNo) {
        Row row = sheet.getRow(currentRowNo);
        String swipeInfoColVal = row.getCell(EVENT_DATE_COL_NO).getStringCellValue();
        return "No Swipe Found".equals(swipeInfoColVal);
    }

    private static boolean rowIsEmpty(int currentRowNo) {
        Row row = sheet.getRow(currentRowNo);
        String firstNameColVal = row.getCell(FIRST_NAME_COL_NO).getStringCellValue();
        return "".equals(firstNameColVal);
    }

    private static boolean swipeInIsLate(int currentRowNo) {
        Row row = sheet.getRow(currentRowNo);
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


    private static XSSFCellStyle createTitleRowStyle() {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.YELLOW.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        setThinBorderAllSides(style);
        return style;
    }

    private static void setThinBorderAllSides(XSSFCellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private static XSSFCellStyle createHighlightedRowStyle() {
        XSSFCellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.DARK_RED.index);
        style.setFont(font);
        setThinBorderAllSides(style);
        return style;
    }

    private static XSSFCellStyle createNormalRowStyle() {
        XSSFCellStyle style = workbook.createCellStyle();
        setThinBorderAllSides(style);
        return style;
    }

    private static void applyStyle(int rowNo, XSSFCellStyle titleRowStyle) {
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(rowNo);
        for (int i = 0; i < 5; i++) {
            row.getCell(i).setCellStyle(titleRowStyle);
        }
    }
}
