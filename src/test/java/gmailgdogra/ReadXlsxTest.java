package gmailgdogra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReadXlsxTest {

    private List<SwipeRecord> swipeRecords;

    private final SwipeRecord FIRST_RECORD = new SwipeRecord("Abdul",
            "Khan",
            LocalDateTime.of(2021, 5, 24, 17, 42, 37),
            "PLA0101 - Barrier IN");

    private final SwipeRecord LAST_RECORD = new SwipeRecord("Zubair",
            "Patel",
            LocalDateTime.of(2021, 5, 25, 10, 28, 27),
            "Thames LSI0306 - Reception Dr In/Out");

    @BeforeEach
    void fillData() {
        Path path = Paths.get("src/main/resources/testFile.xlsx");
        String name = "testFile.xlsx";
        String originalFileName = "testFile.xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content;
        try {
            content = Files.readAllBytes(path);
            MultipartFile result = new MockMultipartFile(name,
                    originalFileName, contentType, content);
            swipeRecords = ReadXlsx.readAllRows(result.getInputStream());
        } catch (IOException e) {
            fail("Unable to read the file: " + name);
        }
    }

    @Test
    @DisplayName("checking total number of records")
    void parseTotalRowsCheck() {
        final int EXPECTED_NO_OF_RECORDS = 136;
        assertEquals(EXPECTED_NO_OF_RECORDS, swipeRecords.size());
    }

    @Test
    @DisplayName("checking first and last record")
    void parseFirstAndLastRowCheck() {
        assertAll("records",
                () -> assertEquals(FIRST_RECORD, swipeRecords.get(0)),
                () -> assertEquals(LAST_RECORD, swipeRecords.get(swipeRecords.size() - 1))
        );
    }
}