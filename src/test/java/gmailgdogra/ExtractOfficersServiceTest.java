package gmailgdogra;

import gmailgdogra.pojo.Officer;
import gmailgdogra.pojo.SwipeRecord;
import gmailgdogra.service.ExtractOfficersService;
import gmailgdogra.service.ReadXlsxService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
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
            new Officer("Officer", "One"),
            new Officer("Officer", "Two"),
            new Officer("Officer", "Three"),
            new Officer("Officer", "Four"),
            new Officer("Officer", "Five"),
            new Officer("Officer", "Six"),
            new Officer("Officer", "Seven"),
            new Officer("Officer", "Eight"),
            new Officer("Officer", "Nine"),
            new Officer("Officer", "Ten"),
            new Officer("Officer", "Eleven"),
            new Officer("Officer", "Twelve")
    ));

    @Test
    void test_from() {
        Path path = Paths.get("src/main/resources/test file 1.xlsx");
        Set<Officer> officers = null;

        try {
            List<SwipeRecord> swipeRecords = readXlsxService.readAllRows(new FileInputStream(path.toFile()));
            officers = ExtractOfficersService.from(swipeRecords);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail("Unable to read file at " + path);
        }

        assertTrue(officers.containsAll(expectedOfficers));
    }
}