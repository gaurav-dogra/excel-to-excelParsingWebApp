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
            LocalDateTime.of(2021, 5, 7, 17, 45, 35),
            "PLA0102 - Turnstile West IN");

    private final SwipeRecord LAST_RECORD = new SwipeRecord("Tahiru",
            "Haruna",
            LocalDateTime.of(2021, 5, 9, 5, 39, 58),
            "Thames LSI0901 - Turnstile South IN");

    @BeforeEach
    void fillData() throws IOException {
        Path path = Paths.get("src/main/resources/testFile.xlsx");
        String name = "testFile.xlsx";
        String originalFileName = "testFile.xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException ignored) {
        }
        MultipartFile result = new MockMultipartFile(name,
                originalFileName, contentType, content);
        swipeRecords = ReadXlsx.parse(result.getInputStream());
    }

    @Test
    @DisplayName("checking total number of records")
    void parseTotalRowsCheck() {
        assertEquals(109, swipeRecords.size());
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