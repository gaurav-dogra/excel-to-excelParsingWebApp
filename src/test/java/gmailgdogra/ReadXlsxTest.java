package gmailgdogra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReadXlsxTest {

    private final ReadXlsx xlsxReader = new ReadXlsx();
    private List<Record> records;

    private final Record FIRST_RECORD = new Record("Abdul",
            "Khan",
            LocalDateTime.of(2021, 5, 8, 5, 43, 35),
            "PLA0102 - Turnstile West IN");

    private final Record LAST_RECORD = new Record("Tahiru",
            "Haruna",
            LocalDateTime.of(2021, 5, 9, 5, 39, 58),
            "Thames LSI0901 - Turnstile South IN");

    @BeforeEach
    void fillData() throws IOException {
        String TEST_FILE_PATH = "src/main/resources/testFile.xlsx";
        records = xlsxReader.parse(TEST_FILE_PATH);
    }

    @Test
    @DisplayName("checking total number of records")
    void parseTotalRowsCheck() {
        assertEquals(103, records.size());
    }

    @Test
    @DisplayName("checking first and last record")
    void parseFirstRowCheck() {
        assertAll("records",
                () -> assertEquals(FIRST_RECORD, records.get(0)),
                () -> assertEquals(LAST_RECORD, records.get(records.size() - 1))
        );
    }
}