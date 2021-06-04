package gmailgdogra;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SwipeProcessorTest {

    private static List<SwipeRecord> outputData;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    @BeforeAll
    static void setUp() throws IOException {
        File file = new File("src/main/resources/testFile.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                IOUtils.toByteArray(inputStream));

        List<SwipeRecord> allSwipes = ReadXlsx.parse(multipartFile.getInputStream());
        outputData = SwipeProcessor.getOutputDataFrom(allSwipes);
    }

    @Test
    void ignore_Swipeout_prev_night_shift() {
        SwipeRecord swipeOut_prev_night = new SwipeRecord("Anthony", "Capes",
                LocalDateTime.parse("24/05/2021 05:51", formatter), "PLA0103 - Turnstile West OUT");

        assertFalse(outputData.contains(swipeOut_prev_night), "Swipe Out from prev night shift must be ignored");
    }

    @Test
    void ignore_SwipeIn_next_day_shift() {
        SwipeRecord swipeIn_next_Day_shift = new SwipeRecord("Fesal", "Amin",
                LocalDateTime.parse("25/05/2021 05:58", formatter), "Thames LSI0903 - Turnstile North IN");

        assertFalse(outputData.contains(swipeIn_next_Day_shift), "Swipe In from next Day shift must be ignored");
    }

    @Test
    void forgot_swipeIn_on_nightShift() {
        SwipeRecord missedSwipeInExpectedRepresentation = new SwipeRecord("Media", "Coulibaly",
                null, null);
        assertTrue(outputData.contains(missedSwipeInExpectedRepresentation), "Missed swipe-in must be added");
    }
}