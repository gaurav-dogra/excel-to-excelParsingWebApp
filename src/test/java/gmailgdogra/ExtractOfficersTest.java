package gmailgdogra;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    void from() {
        Path path = Paths.get("src/main/resources/testFile.xlsx");
        String name = "testFile.xlsx";
        String originalFileName = "testFile.xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            Assertions.fail("Unable to read file at " + path);
        }

        MultipartFile result = new MockMultipartFile(name,
                originalFileName, contentType, content);
        List<SwipeRecord> swipeRecords = null;
        try {
            swipeRecords = ReadXlsx.parse(result.getInputStream());
        } catch (IOException e) {
            Assertions.fail("Unable to parse the file " + result.getName());
        }
        Set<Officer> officers = ExtractOfficers.from(swipeRecords);
        assertEquals(officers, expectedOfficers);
    }
}