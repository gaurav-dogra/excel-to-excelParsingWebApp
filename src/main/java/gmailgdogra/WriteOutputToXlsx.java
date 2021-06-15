package gmailgdogra;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class WriteOutputToXlsx {

    private static final File templateFile =
            new File("src/main/resources/templates/Securitas Daily Report Template.xlsx");

    private static final OutputRow titleRow = OutputRow.of("Location", "First Name",
            "Last Name", "Event Date", "Logical Device");

    private static int rowsCreatedSoFar = 0;

    public static byte[] write(List<OutputRow> outputData, List<Shift> shifts) {
        System.out.println("WriteOutputToXlsx.write");
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            addRow(sheet, titleRow);
            addRows(sheet, outputData);
            autoSizeColumns(sheet);
            workbook.write(out);
            workbook.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("failed to write to output file " + e.getMessage());
        }
    }

    private static void addRows(Sheet sheet, List<OutputRow> outputData) {
        Collections.sort(outputData);
        for (OutputRow data : outputData) {
            addRow(sheet, data);
        }
    }

    private static void addRow(Sheet sheet, OutputRow data) {
        Row row = sheet.createRow(rowsCreatedSoFar);
        rowsCreatedSoFar++;
        for (int col = 0; col < 5; col++) {
            row.createCell(col);
        }

        row.getCell(0).setCellValue(data.getLocation());
        row.getCell(1).setCellValue(data.getFirstName());
        row.getCell(2).setCellValue(data.getLastName());
        row.getCell(3).setCellValue(data.getEventDate());
        row.getCell(4).setCellValue(data.getLogicalDevice());
    }

    private static void autoSizeColumns(Sheet sheet) {
        for (int column = 0; column < 5; column++) {
            sheet.autoSizeColumn(column);
        }
    }

    private static void applyStyle(Row row) {
        int rowNo = row.getRowNum();

        if (rowNo % 2 != 0) { // rows for swipe-ins
            final int TIME_INFO_START_INDEX = 11;
            final int CELL_FOR_SWIPE_DATE_TIME = 2;

            String time = row.getCell(CELL_FOR_SWIPE_DATE_TIME)
                    .getStringCellValue();

            if (time.equals("Did Not Swipe")) {
//                createRowHighlight(row);
            } else {
                time = time.substring(TIME_INFO_START_INDEX);
                if (!time.equals("06:00") && !time.equals("18:00")
                        && !time.startsWith("04") && !time.startsWith("05")
                        && !time.startsWith("16") && !time.startsWith("17")) {
//                    createRowHighlight(row);
                }
            }
        }
    }

//    private static void createRowHighlight(Row row) {
//        CellStyle style = workbook.createCellStyle();
//        Font font = workbook.createFont();
//        font.setColor(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex());
//        style.setFont(font);
//        applyGivenStyle(row, style);
//    }

    private static void applyGivenStyle(Row row, CellStyle style) {
        for (int column = 0; column < 4; column++) {
            row.getCell(column).setCellStyle(style);
        }
    }
}
