package gmailgdogra;

import gmailgdogra.pojo.*;
import gmailgdogra.service.ReadXlsxService;
import gmailgdogra.service.SwipeProcessorService;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SwipeProcessorServiceTest {

    private static final List<OutputRow> inOutSwipesPrevTwoShiftsFromFile1 = getInOutSwipesPrevTwoShiftsFromFile1();
    private static final List<OutputRow> inOutSwipesPrevTwoShiftsFromFile2 = getInOutSwipesPrevTwoShiftsFromFile2();
    private static final Officer officer1 = new Officer("Officer 1", "Officer 1");
    private static final Officer officer2 = new Officer("Officer 2", "Officer 2");
    private static final Officer officer3 = new Officer("Officer 3", "Officer 3");
    private static final Officer officer4 = new Officer("Officer 4", "Officer 4");
    private static final Officer officer5 = new Officer("Officer 5", "Officer 5");
    private static final Officer officer6 = new Officer("Officer 6", "Officer 6");
    private static final Officer officer7 = new Officer("Officer 7", "Officer 7");
    private static final Officer officer8 = new Officer("Officer 8", "Officer 8");
    private static final Officer officer9 = new Officer("Officer 9", "Officer 9");
    private static final Officer officer10 = new Officer("Officer 10", "Officer 10");
    private static final Officer officer11 = new Officer("Officer 11", "Officer 11");
    private static final Officer officer12 = new Officer("Officer 12", "Officer 12");
    private final ReadXlsxService readXlsxService;
    private final SwipeProcessorService swipeProcessorService;

    @Autowired
    public SwipeProcessorServiceTest(ReadXlsxService readXlsxService, SwipeProcessorService swipeProcessorService) {
        this.readXlsxService = readXlsxService;
        this.swipeProcessorService = swipeProcessorService;
    }

    private static List<OutputRow> getInOutSwipesPrevTwoShiftsFromFile1() {
        return Arrays.asList(
                new OutputRow("Plaistow", "Officer 1", "Officer 1",
                        "03/06/2021 17:54", "PLA0102 - Turnstile West IN"),
                new OutputRow("Plaistow", "Officer 1", "Officer 1",
                        "04/06/2021 05:55", "PLA0103 - Turnstile West OUT"),

                new OutputRow("Main Gate", "Officer 3", "Officer 3",
                        "03/06/2021 05:57", "Thames LSI0903 - Turnstile North IN"),
                new OutputRow("Main Gate", "Officer 3", "Officer 3",
                        "03/06/2021 18:08", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Plaistow", "Officer 4", "Officer 4",
                        "03/06/2021 05:41", "PLA0102 - Turnstile West IN"),
                new OutputRow("Plaistow", "Officer 4", "Officer 4",
                        "03/06/2021 18:01", "PLA0103 - Turnstile West OUT"),

                new OutputRow("Main Gate", "Officer 5", "Officer 5",
                        "03/06/2021 18:04", "Thames LSI0903 - Turnstile North IN"),
                new OutputRow("Main Gate", "Officer 5", "Officer 5",
                        null, null),

                new OutputRow("Main Gate", "Officer 7", "Officer 7",
                        "03/06/2021 17:56", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer 7", "Officer 7",
                        "04/06/2021 05:55", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Main Gate", "Officer 8", "Officer 8",
                        "03/06/2021 05:54", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer 8", "Officer 8",
                        "03/06/2021 17:55", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("EP Weighbridge", "Officer 10", "Officer 10",
                        "03/06/2021 17:59", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("EP Weighbridge", "Officer 10", "Officer 10",
                        "04/06/2021 05:54", "Thames LSI0702 - Weighbridge OUT"),

                new OutputRow("EP Weighbridge", "Officer 11", "Officer 11",
                        "03/06/2021 06:01", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("EP Weighbridge", "Officer 11", "Officer 11",
                        "03/06/2021 18:01", "Thames LSI0702 - Weighbridge OUT"),

                new OutputRow("Visitors Reception", "Officer 12", "Officer 12",
                        "03/06/2021 06:46", "Thames LSI0303 - Empl East Turnstile IN"),
                new OutputRow("Visitors Reception", "Officer 12", "Officer 12",
                        "03/06/2021 17:30", "Thames LSI0302 - Empl W Turnstile OUT")
        );
    }

    private static List<OutputRow> getInOutSwipesPrevTwoShiftsFromFile2() {
        return Arrays.asList(
                new OutputRow("Plaistow", "Officer 1", "Officer 1",
                        "24/05/2021 17:44", "PLA0102 - Turnstile West IN"),
                new OutputRow("Plaistow", "Officer 1", "Officer 1",
                        "25/05/2021 05:49", "PLA0103 - Turnstile West OUT"),

                new OutputRow("Main Gate", "Officer 3", "Officer 3",
                        "24/05/2021 05:59", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer 3", "Officer 3",
                        "24/05/2021 18:15", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Plaistow", "Officer 4", "Officer 4",
                        null, null),
                new OutputRow("Plaistow", "Officer 4", "Officer 4",
                        "24/05/2021 17:56", "PLA0103 - Turnstile West OUT"),

                new OutputRow("Main Gate", "Officer 6", "Officer 6",
                        "24/05/2021 17:47", "Thames LSI0903 - Turnstile North IN"),
                new OutputRow("Main Gate", "Officer 6", "Officer 6",
                        "25/05/2021 06:04", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Main Gate", "Officer 7", "Officer 7",
                        "24/05/2021 17:52", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer 7", "Officer 7",
                        "25/05/2021 05:47", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("EP Weighbridge", "Officer 8", "Officer 8",
                        "24/05/2021 17:51", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("EP Weighbridge", "Officer 8", "Officer 8",
                        "25/05/2021 05:55", "Thames LSI0702 - Weighbridge OUT"),

                new OutputRow("EP Weighbridge", "Officer 10", "Officer 10",
                        "24/05/2021 06:03", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("EP Weighbridge", "Officer 10", "Officer 10",
                        "24/05/2021 17:54", "Thames LSI0702 - Weighbridge OUT"),

                new OutputRow("Main Gate", "Officer 11", "Officer 11",
                        "24/05/2021 05:29", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer 11", "Officer 11",
                        "24/05/2021 17:52", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Visitors Reception", "Officer 12", "Officer 12",
                        "24/05/2021 06:47", "Thames LSI0303 - Empl East Turnstile IN"),
                new OutputRow("Visitors Reception", "Officer 12", "Officer 12",
                        "24/05/2021 17:31", "Thames LSI0302 - Empl W Turnstile OUT")
        );
    }

    private List<Shift> getShiftsForTestFile2() {
        return Arrays.asList(
                new Shift(officer1, Location.TL_PLAISTOW, false),
                new Shift(officer3, Location.MAIN_GATE, true),
                new Shift(officer4, Location.TL_PLAISTOW, true),
                new Shift(officer6, Location.MAIN_GATE, false),
                new Shift(officer7, Location.MAIN_GATE, false),
                new Shift(officer8, Location.EP_WEIGHBRIDGE, false),
                new Shift(officer10, Location.EP_WEIGHBRIDGE, true),
                new Shift(officer11, Location.MAIN_GATE, true),
                new Shift(officer12, Location.VISITORS_RECEPTION, true)
        );
    }

    private MockMultipartFile getFile(String pathname) throws IOException {
        File file = new File(pathname);
        FileInputStream inputStream = new FileInputStream(file);
        return new MockMultipartFile("file",
                file.getName(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                IOUtils.toByteArray(inputStream));
    }

    @Test
    void testInOutSwipesPrevTwoShiftsForFile1() throws IOException {
        MockMultipartFile testFile1 = getFile("src/main/resources/test file 1.xlsx");
        List<SwipeRecord> allSwipes = readXlsxService.readAllRows(testFile1.getInputStream());
        List<Shift> shifts = getShiftsForTestFile1();
        swipeProcessorService.prepareData(allSwipes, shifts);
        List<OutputRow> results = swipeProcessorService.getInOutSwipesPrevTwoShifts();
        assertTrue(results.containsAll(inOutSwipesPrevTwoShiftsFromFile1), "Daily report not as expected");
    }

    @Test
    void testInOutSwipesPrevTwoShiftsForFile2() throws IOException {
        MockMultipartFile testFile2 = getFile("src/main/resources/test file 2.xlsx");
        List<SwipeRecord> allSwipes = readXlsxService.readAllRows(testFile2.getInputStream());
        List<Shift> shifts = getShiftsForTestFile2();
        swipeProcessorService.prepareData(allSwipes, shifts);
        List<OutputRow> results = swipeProcessorService.getInOutSwipesPrevTwoShifts();
        assertTrue(results.containsAll(inOutSwipesPrevTwoShiftsFromFile2), "Daily report not as expected");
    }

    @Test
    void testCurrentShiftSwipeInsForFile1() throws IOException {
//        MockMultipartFile testFile1 = getFile("src/main/resources/test file 1.xlsx");
//        List<SwipeRecord> allSwipes = readXlsxService.readAllRows(testFile1.getInputStream());
//        List<Shift> shifts = getShiftsForTestFile1();
//        swipeProcessorService.prepareData(allSwipes, shifts);
//        List<OutputRow> results = swipeProcessorService.getInSwipesCurrentShift();
//        assertTrue(results.containsAll(inOutSwipesPrevTwoShiftsFromFile1), "Daily report not as expected");
    }

    @Test
    void testCurrentShiftSwipeInsForFile2() {

    }

    private List<Shift> getShiftsForTestFile1() {
        return Arrays.asList(
                new Shift(officer1, Location.TL_PLAISTOW, false),
                new Shift(officer3, Location.MAIN_GATE, true),
                new Shift(officer4, Location.TL_PLAISTOW, true),
                new Shift(officer5, Location.MAIN_GATE, false),
                new Shift(officer7, Location.MAIN_GATE, false),
                new Shift(officer8, Location.MAIN_GATE, true),
                new Shift(officer10, Location.EP_WEIGHBRIDGE, false),
                new Shift(officer11, Location.EP_WEIGHBRIDGE, true),
                new Shift(officer12, Location.VISITORS_RECEPTION, true)
        );
    }
}