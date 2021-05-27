package gmailgdogra;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SwipeProcessorTest {

    private static Officer abdulKhan;
    private static Officer basharatIqbal;
    private static SwipeProcessor swipeProcessor;

    @BeforeAll
    static void setUp() throws IOException {
        Path path = Paths.get("src/main/resources/testFile.xlsx");
        String name = "testFile.xlsx";
        String originalFileName = "testFile.xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException ignored) {
        }
        MultipartFile result = new MockMultipartFile(name,
                originalFileName, contentType, content);
        List<SwipeRecord> swipeRecords = ReadXlsx.parse(result.getInputStream());
        abdulKhan = new Officer("Abdul", "Khan", Location.TL_PLAISTOW, true);
        basharatIqbal = new Officer("Basharat", "Iqbal", Location.MAIN_GATE, false);
        swipeProcessor = new SwipeProcessor(swipeRecords);
    }

    @Test
    @DisplayName("Checking Swipe-In times")
    void getFirstSwipeIn() {
        String abdulKhanSwipeIn = swipeProcessor.getFirstSwipeIn(abdulKhan).getSwipeTime().toString();
        String basharatIqbalSwipeIn = swipeProcessor.getFirstSwipeIn(basharatIqbal).getSwipeTime().toString();
        assertEquals("2021-05-08T05:42:35", abdulKhanSwipeIn);
        assertEquals("2021-05-08T18:00:23", basharatIqbalSwipeIn);
    }

    @Test
    @DisplayName("Checking Swipe-Out Times")
    void getLastSwipeOut() {
        String abdulKhanSwipeOut = swipeProcessor.getLastSwipeOut(abdulKhan).getSwipeTime().toString();
        String basharatIqbalSwipeOut = swipeProcessor.getLastSwipeOut(basharatIqbal).getSwipeTime().toString();
        assertEquals("2021-05-08T17:49:01", abdulKhanSwipeOut);
        assertEquals("2021-05-09T05:59:48", basharatIqbalSwipeOut);
    }
}