package gmailgdogra;

import gmailgdogra.pojo.SwipeRecord;
import gmailgdogra.service.ReadXlsxService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static gmailgdogra.AppConstants.ROW_COUNT_TEST_FILE_1;

@SpringBootTest
class ReadXlsxServiceTest {

    private final ReadXlsxService readXlsxService;

    @Autowired
    public ReadXlsxServiceTest(ReadXlsxService readXlsxService) {
        this.readXlsxService = readXlsxService;
    }

    @Test
    public void test_readAllRows() {
        List<SwipeRecord> rows = new ArrayList<>();
        Path path = Paths.get("src/test/resources/test file 1.xlsx");

        try {
            InputStream stream = new FileInputStream(path.toFile());
            rows = readXlsxService.readAllRows(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(rows.size(), ROW_COUNT_TEST_FILE_1, "Rows count does not match");
    }
}
