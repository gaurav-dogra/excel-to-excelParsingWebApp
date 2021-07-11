package gmailgdogra;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ExtractOfficers {

    public static Set<Officer> from(List<SwipeRecord> swipeRecords) {
        return swipeRecords.stream()
                .map(SwipeRecord::getOfficer)
                .collect(Collectors.toSet());
    }
}
