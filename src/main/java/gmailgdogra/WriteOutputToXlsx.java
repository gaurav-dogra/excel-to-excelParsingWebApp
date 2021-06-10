package gmailgdogra;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WriteOutputToXlsx {

    private static final File templateFile =
            new File("src/main/resources/templates/Securitas Daily Report Template.xlsx");

    public static byte[] write(List<SwipeRecord> outputData, List<Shift> shifts) throws IOException {

        Workbook newWorkbook = copyFromTemplate();
        Sheet sheet = newWorkbook.getSheetAt(0);
        List<SwipeRecord> mainGate = getSwipesForGivenLoc(outputData, shifts, Location.MAIN_GATE);
        List<SwipeRecord> epWeighbridge = getSwipesForGivenLoc(outputData, shifts, Location.EP_WEIGHBRIDGE);
        List<SwipeRecord> tlPlaistow = getSwipesForGivenLoc(outputData, shifts, Location.TL_PLAISTOW);
        List<SwipeRecord> visitorsReception = getSwipesForGivenLoc(outputData, shifts, Location.VISITORS_RECEPTION);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            addToSheet(sheet, mainGate, Location.MAIN_GATE);
            addToSheet(sheet, epWeighbridge, Location.EP_WEIGHBRIDGE);
            addToSheet(sheet, tlPlaistow, Location.TL_PLAISTOW);
            addToSheet(sheet, visitorsReception, Location.VISITORS_RECEPTION);
            newWorkbook.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("failed to write to output file " + e.getMessage());
        }
    }

    private static void addToSheet(Sheet sheet, List<SwipeRecord> rows, Location loc) {
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

    private static List<SwipeRecord> getSwipesForGivenLoc(List<SwipeRecord> outputData, List<Shift> shifts, Location location) {
        List<SwipeRecord> returnList = new ArrayList<>();
        for (Shift shift : shifts) {
            if (shift.getLocation() == location) {
                Officer shiftOfficer = shift.getOfficer();
                returnList.addAll(outputData.stream()
                        .filter(swipe -> shiftOfficer.equals(swipe.getOfficer()))
                        .collect(Collectors.toList()));
            }
        }
        return returnList;
    }

    private static Workbook copyFromTemplate() throws IOException {
        File newFile = new File("newFile.xlsx");
        newFile.createNewFile();
        Files.copy(templateFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        InputStream inStream = new FileInputStream(newFile);
        return new XSSFWorkbook(inStream);
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
            addToSheet(sheet, OutputRow.of(record.getOfficer(), swipeTime, record.getDeviceName()), rowNo);
            rowNo++;
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
