package gmailgdogra;

import gmailgdogra.service.ReadXlsxService;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ReadXlsxServiceTest {

    private final ReadXlsxService readXlsxService;

    @Autowired
    public ReadXlsxServiceTest(ReadXlsxService readXlsxService) {
        this.readXlsxService = readXlsxService;
    }

}
