package gmailgdogra;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtractOfficersTest {

    private final Set<Officer> expectedOfficers = new HashSet<>(List.of(
            new Officer("Abdul", "Khan"),
            new Officer("Anthony", "Capes"),
            new Officer("Basharat", "Iqbal"),
            new Officer("GAURAV", "DOGRA"),
            new Officer("Kayode", "Dairo"),
            new Officer("Media", "Coulibaly"),
            new Officer("Omoogbolahan", "Adeola"),
            new Officer("Tahiru", "Haruna")
    ));

    @Test
    void from() throws IOException {
        List<SwipeRecord> swipeRecords = ReadXlsx.parse("src/main/resources/testFile.xlsx");
        Set<Officer> officers = ExtractOfficers.from(swipeRecords);
        assertEquals(officers, expectedOfficers);
    }
}