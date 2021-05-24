package gmailgdogra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PrepareOutputData {

    public static List<SwipeRecord> from(String filePathAndName) throws IOException {
        List<SwipeRecord> outputData = new ArrayList<>();
        List<SwipeRecord> swipeRecords = ReadXlsx.parse(filePathAndName);
        Set<Officer> officers = UserInput.getOfficers(swipeRecords);
        SwipeProcessor swipeProcessor = new SwipeProcessor(swipeRecords);
        for (Officer officer : officers) {
            if (officer.getLocation() != null) {
                outputData.add(swipeProcessor.getFirstSwipeIn(officer));
                outputData.add(swipeProcessor.getLastSwipeOut(officer));
            }
        }
        return outputData;
    }
}

