package gmailgdogra;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SwipeProcessorTest {

    private static final List<OutputRow> expectedResultsTestFile1 = resultsOfTestFile1();
    private static final List<OutputRow> expectedResultsTestFile2 = resultOfTestFile2();
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

    private static List<OutputRow> resultsOfTestFile1() {
        return Arrays.asList(
                new OutputRow("Plaistow", "Officer 1", "Officer 1",
                        "03/06/2021 17:54:13", "PLA0102 - Turnstile West IN"),
                new OutputRow("Plaistow", "Officer 1", "Officer 1",
                        "04/06/2021 05:55:12", "PLA0103 - Turnstile West OUT"),

                new OutputRow("Main Gate", "Officer 3", "Officer 3",
                        "03/06/2021 05:57:57", "Thames LSI0903 - Turnstile North IN"),
                new OutputRow("Main Gate", "Officer 3", "Officer 3",
                        "03/06/2021 18:08:05", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Plaistow", "Officer 4", "Officer 4",
                        "03/06/2021 05:41:20", "PLA0102 - Turnstile West IN"),
                new OutputRow("Plaistow", "Officer 4", "Officer 4",
                        "03/06/2021 18:01:06", "PLA0103 - Turnstile West OUT"),

                new OutputRow("Main Gate", "Officer 5", "Officer 5",
                        "03/06/2021 18:04:44", "Thames LSI0903 - Turnstile North IN"),
                new OutputRow("Main Gate", "Officer 5", "Officer 5",
                        null, null),

                new OutputRow("Main Gate", "Officer 7", "Officer 7",
                        "03/06/2021 17:56:02", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer 7", "Officer 7",
                        "04/06/2021 05:55:28", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Main Gate", "Officer 8", "Officer 8",
                        "03/06/2021 05:54:49", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer 8", "Officer 8",
                        "03/06/2021 17:55:49", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("EP Weighbridge", "Officer 10", "Officer 10",
                        "03/06/2021 17:59:57", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("EP Weighbridge", "Officer 10", "Officer 10",
                        "04/06/2021 05:54:37", "Thames LSI0702 - Weighbridge OUT"),

                new OutputRow("EP Weighbridge", "Officer 11", "Officer 11",
                        "03/06/2021 06:01:37", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("EP Weighbridge", "Officer 11", "Officer 11",
                        "03/06/2021 18:01:33", "Thames LSI0702 - Weighbridge OUT"),

                new OutputRow("Visitors Reception", "Officer 12", "Officer 12",
                        "03/06/2021 06:46:43", "Thames LSI0303 - Empl East Turnstile IN"),
                new OutputRow("Visitors Reception", "Officer 12", "Officer 12",
                        "03/06/2021 17:30:15", "Thames LSI0302 - Empl W Turnstile OUT")
                );
    }

    private static List<OutputRow> resultOfTestFile2() {
        return Arrays.asList(
                new OutputRow("Plaistow", "Officer 1", "Officer 1",
                        "24/05/2021 17:44:20", "PLA0102 - Turnstile West IN"),
                new OutputRow("Plaistow", "Officer 1", "Officer 1",
                        "25/05/2021 05:49:48", "PLA0103 - Turnstile West OUT"),

                new OutputRow("Main Gate", "Officer 3", "Officer 3",
                        "24/05/2021 05:59:58", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer 3", "Officer 3",
                        "24/05/2021 18:15:46", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Plaistow", "Officer 4", "Officer 4",
                        null, null),
                new OutputRow("Plaistow", "Officer 4", "Officer 4",
                        "24/05/2021 17:56:23", "PLA0103 - Turnstile West OUT"),

                new OutputRow("Main Gate", "Officer 6", "Officer 6",
                        "24/05/2021 17:47:07", "Thames LSI0903 - Turnstile North IN"),
                new OutputRow("Main Gate", "Officer 6", "Officer 6",
                        "25/05/2021 06:04:21", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Main Gate", "Officer 7", "Officer 7",
                        "24/05/2021 17:52:37", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer 7", "Officer 7",
                        "25/05/2021 05:47:19", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("EP Weighbridge", "Officer 8", "Officer 8",
                        "24/05/2021 17:51:14", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("EP Weighbridge", "Officer 8", "Officer 8",
                        "25/05/2021 05:55:15", "Thames LSI0702 - Weighbridge OUT"),

                new OutputRow("EP Weighbridge", "Officer 10", "Officer 10",
                        "24/05/2021 06:03:18", "Thames LSI0701 - Weighbridge IN"),
                new OutputRow("EP Weighbridge", "Officer 10", "Officer 10",
                        "24/05/2021 17:54:22", "Thames LSI0702 - Weighbridge OUT"),

                new OutputRow("Main Gate", "Officer 11", "Officer 11",
                        "24/05/2021 05:29:23", "Thames LSI0901 - Turnstile South IN"),
                new OutputRow("Main Gate", "Officer 11", "Officer 11",
                        "24/05/2021 17:52:39", "Thames LSI0904 - Turnstile North OUT"),

                new OutputRow("Visitors Reception", "Officer 12", "Officer 12",
                        "24/05/2021 06:47:11", "Thames LSI0303 - Empl East Turnstile IN"),
                new OutputRow("Visitors Reception", "Officer 12", "Officer 12",
                        "24/05/2021 17:31:33", "Thames LSI0302 - Empl W Turnstile OUT")
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
    void testFile1() throws IOException {
        MockMultipartFile testFile1 = getFile("src/main/resources/test file 1.xlsx");
        List<SwipeRecord> allSwipes = ReadXlsxService.readAllRows(testFile1.getInputStream());
        List<Shift> shifts = getShiftsForTestFile1();
        List<OutputRow> results = SwipeProcessor.getOutputDataFrom(allSwipes, shifts);
        assertTrue(results.containsAll(expectedResultsTestFile1));
    }

    @Test
    void testFile2() throws IOException {
        MockMultipartFile testFile2 = getFile("src/main/resources/test file 2.xlsx");
        List<SwipeRecord> allSwipes = ReadXlsxService.readAllRows(testFile2.getInputStream());
        List<Shift> shifts = getShiftsForTestFile2();
        List<OutputRow> results = SwipeProcessor.getOutputDataFrom(allSwipes, shifts);
        assertTrue(results.containsAll(expectedResultsTestFile2));
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