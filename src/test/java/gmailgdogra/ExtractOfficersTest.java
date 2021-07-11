package gmailgdogra;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ExtractOfficersTest {

    private final ReadXlsxService readXlsxService;

    @Autowired
    public ExtractOfficersTest(ReadXlsxService readXlsxService) {
        this.readXlsxService = readXlsxService;
    }

    private final Set<Officer> expectedOfficers = new HashSet<>(Arrays.asList(
            new Officer("Abdul", "Khan"),
            new Officer("Anthony", "Capes"),
            new Officer("Dean", "Colquhoun"),
            new Officer("Derek", "Devlin"),
            new Officer("Fesal", "Amin"),
            new Officer("Gaurav", "Dogra"),
            new Officer("Kayode", "Dairo"),
            new Officer("Media", "Coulibaly"),
            new Officer("Omoogbolahan", "Adeola"),
            new Officer("Reehad", "Ali"),
            new Officer("Tahiru", "Haruna"),
            new Officer("Zubair", "Patel")
    ));

    @Test
    void from() {
        Path path = Paths.get("src/main/resources/testFile.xlsx");
        String name = "testFile.xlsx";
        String originalFileName = "testFile.xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        List<SwipeRecord> swipeRecords = null;
        try {
            byte[] content = Files.readAllBytes(path);
            MultipartFile result = new MockMultipartFile(name,
                    originalFileName, contentType, content);
            swipeRecords = readXlsxService.readAllRows(result.getInputStream());

        } catch (Exception e) {
            Assertions.fail("Unable to read file at " + path);
        }

        Set<Officer> officers = ExtractOfficers.from(swipeRecords);
        assertEquals(officers, expectedOfficers);
    }
}