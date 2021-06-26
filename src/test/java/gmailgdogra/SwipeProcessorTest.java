package gmailgdogra;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SwipeProcessorTest {

    private static List<OutputRow> outputData;

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
        return Arrays.asList(
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
        OutputRow swipeOut_prev_night = OutputRow.of(Location.TL_PLAISTOW.toString(),
                "Anthony", "Capes", "24/05/2021 05:51:29", "PLA0103 - Turnstile West OUT");

        assertFalse(outputData.contains(swipeOut_prev_night), "Swipe Out from prev night shift must be ignored");
    }

    @Test
    void ignore_SwipeIn_next_day_shift() {
        OutputRow swipeIn_next_Day_shift = OutputRow.of(Location.MAIN_GATE.toString(), "Fesal","Amin",
                "25/05/2021 05:58:17", "Thames LSI0903 - Turnstile North IN");

        assertFalse(outputData.contains(swipeIn_next_Day_shift), "Swipe In from next Day shift must be ignored");
    }

    @Test
    void forgot_swipeIn_on_nightShift() {
        OutputRow missedSwipeInExpectedRepresentation = OutputRow.of(Location.EP_WEIGHBRIDGE.toString(),
                "Media", "Coulibaly", null, null);
        assertTrue(outputData.contains(missedSwipeInExpectedRepresentation), "Missed swipe-in must be added");
    }

    @Test
    void swipeTimesTest() {

        OutputRow swipeInGaurav = OutputRow.of(Location.MAIN_GATE.toString(),"GAURAV", "DOGRA",
                "24/05/2021 17:47:07", "Thames LSI0903 - Turnstile North IN");
        OutputRow swipeOutGaurav = OutputRow.of(Location.MAIN_GATE.toString(),"GAURAV", "DOGRA",
                "25/05/2021 06:04:21", "Thames LSI0904 - Turnstile North OUT");
        OutputRow swipeInKayode = OutputRow.of(Location.MAIN_GATE.toString(),"Kayode", "Dairo",
                "24/05/2021 17:52:37", "Thames LSI0901 - Turnstile South IN");
        OutputRow swipeOutKayode = OutputRow.of(Location.MAIN_GATE.toString(),"Kayode", "Dairo",
                "25/05/2021 05:47:19", "Thames LSI0904 - Turnstile North OUT");

        assertTrue(outputData.contains(swipeInGaurav));
        assertTrue(outputData.contains(swipeOutGaurav));
        assertTrue(outputData.contains(swipeInKayode));
        assertTrue(outputData.contains(swipeOutKayode));
    }
}