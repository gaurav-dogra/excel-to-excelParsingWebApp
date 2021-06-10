package gmailgdogra;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class WriteOutputToXlsxTest {

    @Test
    void writeTest() throws IOException {
        WriteOutputToXlsx.write(null, null);
    }

}