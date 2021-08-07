package gmailgdogra;

import gmailgdogra.pojo.Location;
import gmailgdogra.pojo.Officer;
import gmailgdogra.pojo.OutputRow;
import gmailgdogra.pojo.Shift;
import gmailgdogra.pojo.SwipeRecord;
import gmailgdogra.service.ReadXlsxService;
import gmailgdogra.service.SwipeProcessorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SwipeProcessorServiceIT {

    private static final List<OutputRow> inOutSwipesPrevTwoShiftsFromFile1 = getInOutSwipesPrevTwoShiftsFromFile1();
    private static final List<OutputRow> inOutSwipesPrevTwoShiftsFromFile2 = getInOutSwipesPrevTwoShiftsFromFile2();
    private static final List<OutputRow> inSwipesCurrentShiftFromFile1 = getInSwipesCurrentShiftsFromFile1();
    private static final List<OutputRow> inSwipesCurrentShiftFromFile2 = getInSwipesCurrentShiftsFromFile2();
    private static final Officer officerOne = new Officer("Officer", "One");
    private static final Officer officerThree = new Officer("Officer", "Three");
    private static final Officer officerFour = new Officer("Officer", "Four");
    private static final Officer officerFive = new Officer("Officer", "Five");
    private static final Officer officerSix = new Officer("Officer", "Six");
    private static final Officer officerSeven = new Officer("Officer", "Seven");
    private static final Officer officerEight = new Officer("Officer", "Eight");
    private static final Officer officerTen = new Officer("Officer", "Ten");
    private static final Officer officerEleven = new Officer("Officer", "Eleven");
    private static final Officer officerTwelve = new Officer("Officer", "Twelve");
    private final ReadXlsxService readXlsxService;
    private final SwipeProcessorService swipeProcessorService;

    @Autowired
    public SwipeProcessorServiceIT(ReadXlsxService readXlsxService, SwipeProcessorService swipeProcessorService) {
        this.readXlsxService = readXlsxService;
        this.swipeProcessorService = swipeProcessorService;
    }

    private static List<OutputRow> getInOutSwipesPrevTwoShiftsFromFile1() {
        return Arrays.asList(
                new OutputRow("Plaistow", "Officer", "One",
                        "03/06/2021 17:54", "PLA0102 - Turnstile West IN"),
                new OutputRow("Plaistow", "Officer", "One",
                        "04/06/2021 05:55", "PLA0103 - Turnstile West OUT"),

                new OutputRow("Main Gate", "Officer", "Three",
                        "03/06/2021 05:57", "Thames LSI0903 - Turnstile North IN"),
                new OutputRow("Main Gate", "Officer", "Three",
                        "03/06/2021 18:08", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Plaistow", "Officer", "Four",
                        "03/06/2021 05:41", "PLA0102 - Turnstile West IN"),
                new OutputRow("Plaistow", "Officer", "Four",
                        "03/06/2021 18:01", "PLA0103 - Turnstile West OUT"),

                new OutputRow("Main Gate", "Officer", "Five",
                        "03/06/2021 18:04", "Thames LSI0903 - Turnstile North IN"),
                new OutputRow("Main Gate", "Officer", "Five",
                        null, null),

                new OutputRow("Main Gate", "Officer", "Seven",
                        "03/06/2021 17:56", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer", "Seven",
                        "04/06/2021 05:55", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Main Gate", "Officer", "Eight",
                        "03/06/2021 05:54", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer", "Eight",
                        "03/06/2021 17:55", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("EP Weighbridge", "Officer", "Ten",
                        "03/06/2021 17:59", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("EP Weighbridge", "Officer", "Ten",
                        "04/06/2021 05:54", "Thames LSI0702 - Weighbridge OUT"),

                new OutputRow("EP Weighbridge", "Officer", "Eleven",
                        "03/06/2021 06:01", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("EP Weighbridge", "Officer", "Eleven",
                        "03/06/2021 18:01", "Thames LSI0702 - Weighbridge OUT"),

                new OutputRow("Visitors Reception", "Officer", "Twelve",
                        "03/06/2021 06:46", "Thames LSI0303 - Empl East Turnstile IN"),
                new OutputRow("Visitors Reception", "Officer", "Twelve",
                        "03/06/2021 17:30", "Thames LSI0302 - Empl W Turnstile OUT")
        );
    }

    private static List<OutputRow> getInOutSwipesPrevTwoShiftsFromFile2() {
        return Arrays.asList(
                new OutputRow("Plaistow", "Officer", "One",
                        "24/05/2021 17:44", "PLA0102 - Turnstile West IN"),
                new OutputRow("Plaistow", "Officer", "One",
                        "25/05/2021 05:49", "PLA0103 - Turnstile West OUT"),

                new OutputRow("Main Gate", "Officer", "Three",
                        "24/05/2021 05:59", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer", "Three",
                        "24/05/2021 18:15", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Plaistow", "Officer", "Four",
                        null, null),
                new OutputRow("Plaistow", "Officer", "Four",
                        "24/05/2021 17:56", "PLA0103 - Turnstile West OUT"),

                new OutputRow("Main Gate", "Officer", "Six",
                        "24/05/2021 17:47", "Thames LSI0903 - Turnstile North IN"),
                new OutputRow("Main Gate", "Officer", "Six",
                        "25/05/2021 06:04", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Main Gate", "Officer", "Seven",
                        "24/05/2021 17:52", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer", "Seven",
                        "25/05/2021 05:47", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("EP Weighbridge", "Officer", "Eight",
                        "24/05/2021 17:51", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("EP Weighbridge", "Officer", "Eight",
                        "25/05/2021 05:55", "Thames LSI0702 - Weighbridge OUT"),

                new OutputRow("EP Weighbridge", "Officer", "Ten",
                        "24/05/2021 06:03", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("EP Weighbridge", "Officer", "Ten",
                        "24/05/2021 17:54", "Thames LSI0702 - Weighbridge OUT"),

                new OutputRow("Main Gate", "Officer", "Eleven",
                        "24/05/2021 05:29", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer", "Eleven",
                        "24/05/2021 17:52", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Visitors Reception", "Officer", "Twelve",
                        "24/05/2021 06:47", "Thames LSI0303 - Empl East Turnstile IN"),
                new OutputRow("Visitors Reception", "Officer", "Twelve",
                        "24/05/2021 17:31", "Thames LSI0302 - Empl W Turnstile OUT")
        );
    }

    private static List<OutputRow> getInSwipesCurrentShiftsFromFile1() {
        return Arrays.asList(
                new OutputRow("Main Gate", "Officer", "Three",
                        "04/06/2021 06:00", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("EP Weighbridge", "Officer", "Six",
                        "04/06/2021 05:53", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("Main Gate", "Officer", "Eight",
                        "04/06/2021 05:53", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Visitors Reception", "Officer", "Twelve",
                        "04/06/2021 06:55", "Thames LSI0303 - Empl East Turnstile IN"));
    }

    private static List<OutputRow> getInSwipesCurrentShiftsFromFile2() {
        return Arrays.asList(
                new OutputRow("Main Gate", "Officer", "Five",
                        "25/05/2021 05:58", "Thames LSI0903 - Turnstile North IN"),
                new OutputRow("Main Gate", "Officer", "Eleven",
                        "25/05/2021 05:46", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Visitors Reception", "Officer", "Twelve",
                        "25/05/2021 06:48", "Thames LSI0303 - Empl East Turnstile IN"),
                new OutputRow("EP Weighbridge", "Officer", "Ten",
                        "25/05/2021 05:55", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("Plaistow", "Officer", "Four",
                        "25/05/2021 05:42", "PLA0102 - Turnstile West IN"));
    }

    private List<Shift> getShiftsForTestFile2() {
        return Arrays.asList(
                new Shift(officerOne, Location.TL_PLAISTOW, false),
                new Shift(officerThree, Location.MAIN_GATE, true),
                new Shift(officerFour, Location.TL_PLAISTOW, true),
                new Shift(officerSix, Location.MAIN_GATE, false),
                new Shift(officerSeven, Location.MAIN_GATE, false),
                new Shift(officerEight, Location.EP_WEIGHBRIDGE, false),
                new Shift(officerTen, Location.EP_WEIGHBRIDGE, true),
                new Shift(officerEleven, Location.MAIN_GATE, true),
                new Shift(officerTwelve, Location.VISITORS_RECEPTION, true)
        );
    }

    @Test
    void testInOutSwipesPrevTwoShiftsForFile1() throws IOException {
        Path path = Paths.get("src/test/resources/test file 1.xlsx");
        InputStream stream = new FileInputStream(path.toFile());

        List<SwipeRecord> allSwipes = readXlsxService.readAllRows(stream);
        List<Shift> shifts = getShiftsForTestFile1();
        swipeProcessorService.prepareData(allSwipes, shifts);
        List<OutputRow> results = swipeProcessorService.getInOutSwipesPrevTwoShifts();

        assertTrue(results.containsAll(inOutSwipesPrevTwoShiftsFromFile1), "Daily report not as expected");
    }

    @Test
    void testInOutSwipesPrevTwoShiftsForFile2() throws IOException {
        Path path = Paths.get("src/test/resources/test file 2.xlsx");
        InputStream stream = new FileInputStream(path.toFile());

        List<SwipeRecord> allSwipes = readXlsxService.readAllRows(stream);
        List<Shift> shifts = getShiftsForTestFile2();
        swipeProcessorService.prepareData(allSwipes, shifts);
        List<OutputRow> results = swipeProcessorService.getInOutSwipesPrevTwoShifts();

        assertTrue(results.containsAll(inOutSwipesPrevTwoShiftsFromFile2), "Daily report not as expected");
    }

    @Test
    void testCurrentShiftSwipeInsForFile1() throws IOException {
        Path path = Paths.get("src/test/resources/test file 1.xlsx");
        InputStream stream = new FileInputStream(path.toFile());

        List<SwipeRecord> allSwipes = readXlsxService.readAllRows(stream);
        List<Shift> shifts = getShiftsForTestFile1();
        swipeProcessorService.prepareData(allSwipes, shifts);
        List<OutputRow> results = swipeProcessorService.getInSwipesCurrentShift();

        assertTrue(results.containsAll(inSwipesCurrentShiftFromFile1), "Current Shift Swipe-in not as expected");
    }

    @Test
    void testCurrentShiftSwipeInsForFile2() throws IOException {
        Path path = Paths.get("src/test/resources/test file 2.xlsx");
        InputStream stream = new FileInputStream(path.toFile());

        List<SwipeRecord> allSwipes = readXlsxService.readAllRows(stream);
        List<Shift> shifts = getShiftsForTestFile2();
        swipeProcessorService.prepareData(allSwipes, shifts);
        List<OutputRow> results = swipeProcessorService.getInSwipesCurrentShift();

        assertTrue(results.containsAll(inSwipesCurrentShiftFromFile2), "Current Shift Swipe-in not as expected");
    }

    private List<Shift> getShiftsForTestFile1() {
        return Arrays.asList(
                new Shift(officerOne, Location.TL_PLAISTOW, false),
                new Shift(officerThree, Location.MAIN_GATE, true),
                new Shift(officerFour, Location.TL_PLAISTOW, true),
                new Shift(officerFive, Location.MAIN_GATE, false),
                new Shift(officerSeven, Location.MAIN_GATE, false),
                new Shift(officerEight, Location.MAIN_GATE, true),
                new Shift(officerTen, Location.EP_WEIGHBRIDGE, false),
                new Shift(officerEleven, Location.EP_WEIGHBRIDGE, true),
                new Shift(officerTwelve, Location.VISITORS_RECEPTION, true)
        );
    }
}