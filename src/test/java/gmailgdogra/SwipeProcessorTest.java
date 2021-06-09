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
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    @BeforeAll
    static void setUp() throws IOException {
        File file = new File("src/main/resources/testFile.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                IOUtils.toByteArray(inputStream));

        List<SwipeRecord> allSwipes = ReadXlsxService.readAllRows(multipartFile.getInputStream());
        List<Shift> shifts = getShiftDetails();
        outputData = SwipeProcessor.getOutputDataFrom(allSwipes, shifts);
    }

    private static List<Shift> getShiftDetails() {
        Officer abdul = new Officer("Abdul", "Khan");
        Officer dean = new Officer("Dean", "Colquhoun");
        Officer derek = new Officer("Derek", "Devlin");
        Officer gaurav = new Officer("GAURAV", "DOGRA");
        Officer kayode = new Officer("Kayode", "Dairo");
        Officer media = new Officer("Media", "Coulibaly");
        Officer reehad = new Officer("Reehad", "Ali");
        Officer tahiru = new Officer("Tahiru", "Haruna");
        Officer zubair = new Officer("Zubair", "Patel");
        return List.of(
                new Shift(abdul, Location.TL_PLAISTOW, false),
                new Shift(dean, Location.MAIN_GATE, true),
                new Shift(derek, Location.TL_PLAISTOW, true),
                new Shift(gaurav, Location.MAIN_GATE, false),
                new Shift(kayode, Location.MAIN_GATE, false),
                new Shift(media, Location.EP_WEIGHBRIDGE, false),
                new Shift(reehad, Location.EP_WEIGHBRIDGE, true),
                new Shift(tahiru, Location.MAIN_GATE, true),
                new Shift(zubair, Location.VISITORS_RECEPTION, true)
        );
    }

    @Test
    void ignore_Swipeout_prev_night_shift() {
        SwipeRecord swipeOut_prev_night = new SwipeRecord("Anthony", "Capes",
                LocalDateTime.parse("24/05/2021 05:51:29", formatter), "PLA0103 - Turnstile West OUT");

        assertFalse(outputData.contains(swipeOut_prev_night), "Swipe Out from prev night shift must be ignored");
    }

    @Test
    void ignore_SwipeIn_next_day_shift() {
        SwipeRecord swipeIn_next_Day_shift = new SwipeRecord("Fesal", "Amin",
                LocalDateTime.parse("25/05/2021 05:58:17", formatter), "Thames LSI0903 - Turnstile North IN");

        assertFalse(outputData.contains(swipeIn_next_Day_shift), "Swipe In from next Day shift must be ignored");
    }

    @Test
    void forgot_swipeIn_on_nightShift() {
        SwipeRecord missedSwipeInExpectedRepresentation = new SwipeRecord("Media", "Coulibaly",
                null, null);
        assertTrue(outputData.contains(missedSwipeInExpectedRepresentation), "Missed swipe-in must be added");
    }

    @Test
    void swipeTimesTest() {

        SwipeRecord swipeInGaurav = new SwipeRecord("GAURAV", "DOGRA",
                LocalDateTime.parse("24/05/2021 17:47:07", formatter), "Thames LSI0903 - Turnstile North IN");
        SwipeRecord swipeOutGaurav = new SwipeRecord("GAURAV", "DOGRA",
                LocalDateTime.parse("25/05/2021 06:04:21", formatter), "Thames LSI0904 - Turnstile North OUT");
        SwipeRecord swipeInKayode = new SwipeRecord("Kayode", "Dairo",
                LocalDateTime.parse("24/05/2021 17:52:37", formatter), "Thames LSI0901 - Turnstile South IN");
        SwipeRecord swipeOutKayode = new SwipeRecord("Kayode", "Dairo",
                LocalDateTime.parse("25/05/2021 05:47:19", formatter), "Thames LSI0904 - Turnstile North OUT");

        assertTrue(outputData.contains(swipeInGaurav));
        assertTrue(outputData.contains(swipeOutGaurav));
        assertTrue(outputData.contains(swipeInKayode));
        assertTrue(outputData.contains(swipeOutKayode));
    }
}