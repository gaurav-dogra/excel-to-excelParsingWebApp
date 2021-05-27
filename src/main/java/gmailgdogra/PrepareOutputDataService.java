package gmailgdogra;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PrepareOutputDataService {

    public static String EXCEL_FILE_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static List<SwipeRecord> from(InputStream inputStream) throws IOException {
        List<SwipeRecord> outputData = new ArrayList<>();
        List<SwipeRecord> swipeRecords = ReadXlsx.parse(inputStream);
        Set<Officer> officers = UserInput.getOfficers(swipeRecords);
        SwipeProcessor swipeProcessor = new SwipeProcessor(swipeRecords);
        for (Officer officer : officers) {
            if (officer.getLocation() != null) {
                outputData.add(swipeProcessor.getFirstSwipeIn(officer)); // one row
                outputData.add(swipeProcessor.getLastSwipeOut(officer)); // another row
            }
        }
        return outputData;
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        return EXCEL_FILE_TYPE.equals(file.getContentType());
    }
}

