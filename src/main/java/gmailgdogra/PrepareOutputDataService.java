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
        return ReadXlsx.parse(inputStream);
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        return EXCEL_FILE_TYPE.equals(file.getContentType());
    }

    public static List<SwipeRecord> getOutputDataFrom(List<SwipeRecord> allSwipes) {
        List<SwipeRecord> outputData = new ArrayList<>();
        Set<Officer> officers = UserInput.getOfficers(allSwipes);
        SwipeProcessor swipeProcessor = new SwipeProcessor(allSwipes);
        for (Officer officer : officers) {
            if (officer.getLocation() != null) {
                outputData.add(swipeProcessor.getFirstSwipeIn(officer)); // swipe in row
                outputData.add(swipeProcessor.getLastSwipeOut(officer)); // swipe out row
            }
        }
        return outputData;
    }
}

