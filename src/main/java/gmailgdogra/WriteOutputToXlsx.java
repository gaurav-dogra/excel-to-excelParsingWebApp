package gmailgdogra;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WriteOutputToXlsx {

    private static final File templateFile =
            new File("src/main/resources/templates/Securitas Daily Report Template.xlsx");

    private static final OutputRow titleRow = OutputRow.of("Location", "First Name",
            "Last Name", "Event Date", "Logical Device");

    private static int rowsCreatedSoFar = 0;

    public static byte[] write(List<SwipeRecord> outputData, List<Shift> shifts) {
        System.out.println("WriteOutputToXlsx.write");
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<SwipeRecord> mainGate = getSwipesForGivenLoc(outputData, shifts, Location.MAIN_GATE);
        List<SwipeRecord> epWeighbridge = getSwipesForGivenLoc(outputData, shifts, Location.EP_WEIGHBRIDGE);
        List<SwipeRecord> tlPlaistow = getSwipesForGivenLoc(outputData, shifts, Location.TL_PLAISTOW);
        List<SwipeRecord> visitorsReception = getSwipesForGivenLoc(outputData, shifts, Location.VISITORS_RECEPTION);

        System.out.println("mainGate = " + mainGate.size());
        System.out.println("epWeighbridge = " + epWeighbridge.size());
        System.out.println("tlPlaistow = " + tlPlaistow.size());
        System.out.println("visitorsReception = " + visitorsReception.size());

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            addRow(sheet, titleRow);
            addAllRowsForALoc(sheet, mainGate, Location.MAIN_GATE);
            addAllRowsForALoc(sheet, epWeighbridge, Location.EP_WEIGHBRIDGE);
            addAllRowsForALoc(sheet, tlPlaistow, Location.TL_PLAISTOW);
            addAllRowsForALoc(sheet, visitorsReception, Location.VISITORS_RECEPTION);
            autoSizeColumns(sheet);
            workbook.write(out);
            workbook.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("failed to write to output file " + e.getMessage());
        }
    }

    private static void addRow(Sheet sheet, OutputRow data) {
        System.out.println("data = " + data);
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

//        applyStyle(row);
    }

    private static void addAllRowsForALoc(Sheet sheet, List<SwipeRecord> records, Location loc) {
        System.out.println("WriteOutputToXlsx.addAllRowsForALoc");
        System.out.println("loc = " + loc);
        for (SwipeRecord record : records) {
            String location = loc.toString();
            String firstName = record.getOfficer().getFirstName();
            String lastName = record.getOfficer().getLastName();
            String eventDate;
            String logicalDevice;
            LocalDateTime eventDateTime = record.getSwipeDateTime();
            if (eventDateTime == null) {
                eventDate = "Swipe Missing";
                logicalDevice = "";
            } else {
                eventDate = eventDateTime.toString();
                logicalDevice = record.getDeviceName();
            }
            OutputRow data = OutputRow.of(location, firstName, lastName, eventDate, logicalDevice);
            addRow(sheet, data);
        }
    }

    private static List<SwipeRecord> getSwipesForGivenLoc(List<SwipeRecord> outputData, List<Shift> shifts,
                                                          Location location) {
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
