package gmailgdogra;

import gmailgdogra.pojo.Officer;
import gmailgdogra.pojo.SwipeRecord;
import gmailgdogra.service.ExtractOfficersService;
import gmailgdogra.service.ReadXlsxService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ExtractOfficersServiceTest {

    private final ReadXlsxService readXlsxService;

    @Autowired
    public ExtractOfficersServiceTest(ReadXlsxService readXlsxService) {
        this.readXlsxService = readXlsxService;
    }

    private final Set<Officer> expectedOfficers = new HashSet<>(Arrays.asList(
            new Officer("Officer 1", "Officer 1"),
            new Officer("Officer 2", "Officer 2"),
            new Officer("Officer 3", "Officer 3"),
            new Officer("Officer 4", "Officer 4"),
            new Officer("Officer 5", "Officer 5"),
            new Officer("Officer 6", "Officer 6"),
            new Officer("Officer 7", "Officer 7"),
            new Officer("Officer 8", "Officer 8"),
            new Officer("Officer 9", "Officer 9"),
            new Officer("Officer 10", "Officer 10"),
            new Officer("Officer 11", "Officer 11"),
            new Officer("Officer 12", "Officer 12")
    ));

    @Test
    void from() {
        Path path = Paths.get("src/main/resources/test file 1.xlsx");
        String name = "test file 1.xlsx";
        String originalFileName = "test file 1.xlsx";
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

        Set<Officer> officers = ExtractOfficersService.from(swipeRecords);

        assertTrue(officers.containsAll(expectedOfficers));
    }
}