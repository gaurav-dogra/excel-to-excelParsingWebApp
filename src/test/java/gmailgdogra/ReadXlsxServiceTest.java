package gmailgdogra;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReadXlsxServiceTest {

    @Test
    void hasExcelFormat_FailCase() throws Exception {
        File file = new File("file.txt");

        file.createNewFile();
        FileInputStream inputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(),
                "text/plain",
                IOUtils.toByteArray(inputStream));
        boolean result = ReadXlsxService.hasExcelFormat(multipartFile);
        assertFalse(result);
    }

    @Test
    void hasExcelFormat_PassCase() throws Exception {
        File file = new File("testFile.xlsx");

        file.createNewFile();
        FileInputStream inputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                IOUtils.toByteArray(inputStream));
        boolean result = ReadXlsxService.hasExcelFormat(multipartFile);
        assertTrue(result);
    }
}
