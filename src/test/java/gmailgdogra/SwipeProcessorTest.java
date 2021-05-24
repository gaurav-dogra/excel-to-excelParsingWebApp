package gmailgdogra;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SwipeProcessorTest {

    private static Officer abdulKhan;
    private static Officer basharatIqbal;
    private static SwipeProcessor swipeProcessor;

    @BeforeAll
    static void setUp() throws IOException {
        List<SwipeRecord> swipeRecords = ReadXlsx.parse("src/main/resources/testFile.xlsx");
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