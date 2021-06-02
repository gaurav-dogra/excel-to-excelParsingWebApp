package gmailgdogra;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExtractOfficers {

    public static Set<Officer> from(List<SwipeRecord> swipeRecords) {
        return swipeRecords.stream()
                .map(swipeRecord -> new Officer(swipeRecord.getFirstName(),
                        swipeRecord.getLastName()))
                .collect(Collectors.toSet());
    }
}
