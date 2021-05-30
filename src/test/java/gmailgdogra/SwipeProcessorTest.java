package gmailgdogra;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SwipeProcessorTest {

    private static Officer abdulKhan;
    private static Officer basharatIqbal;
    private static List<SwipeRecord> allSwipes;

    @BeforeAll
    static void setUp() throws IOException {
        File file = new File("src/main/resources/testFile.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                IOUtils.toByteArray(inputStream));

        allSwipes = ReadXlsx.parse(multipartFile.getInputStream());
        abdulKhan = new Officer("Abdul", "Khan", Location.TL_PLAISTOW, true);
        basharatIqbal = new Officer("Basharat", "Iqbal", Location.MAIN_GATE, false);
    }

    @Test
    @DisplayName("Checking Swipe-In times")
    void getFirstSwipeIn() {
        String abdulKhanSwipeIn = SwipeProcessor.getFirstSwipeIn(abdulKhan, allSwipes).getSwipeTime().toString();
        String basharatIqbalSwipeIn = SwipeProcessor.getFirstSwipeIn(basharatIqbal, allSwipes).getSwipeTime().toString();
        assertEquals("2021-05-08T05:42:35", abdulKhanSwipeIn);
        assertEquals("2021-05-08T18:00:23", basharatIqbalSwipeIn);
    }

    @Test
    @DisplayName("Checking Swipe-Out Times")
    void getLastSwipeOut() {
        String abdulKhanSwipeOut = SwipeProcessor.getLastSwipeOut(abdulKhan, allSwipes).getSwipeTime().toString();
        String basharatIqbalSwipeOut = SwipeProcessor.getLastSwipeOut(basharatIqbal, allSwipes).getSwipeTime().toString();
        assertEquals("2021-05-08T17:49:01", abdulKhanSwipeOut);
        assertEquals("2021-05-09T05:59:48", basharatIqbalSwipeOut);
    }
}