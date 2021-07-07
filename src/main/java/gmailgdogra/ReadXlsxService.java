package gmailgdogra;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ReadXlsxService {

    public static String EXCEL_FILE_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static List<SwipeRecord> readAllRows(InputStream inputStream) throws Exception {
        return ReadXlsx.readAllRows(inputStream);
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        return EXCEL_FILE_TYPE.equals(file.getContentType());
    }

}

