package gmailgdogra;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwipeProcessor {

    private final List<SwipeRecord> swipeRecords;
    private static final Map<Location, List<String>> allSwipeInDevices = new HashMap<>();
    private static final Map<Location, List<String>> allSwipeOutDevices = new HashMap<>();

    static {
        allSwipeInDevices.put(Location.MAIN_GATE,
                List.of("Thames LSI0903 - Turnstile North IN",
                        "Thames LSI0901 - Turnstile South IN"));
        allSwipeInDevices.put(Location.VISITORS_RECEPTION,
                List.of("Thames LSI0303 - Empl East Turnstile IN",
                        "Thames LSI0301 - Empl West Turnstile IN"));
        allSwipeInDevices.put(Location.EP_WEIGHBRIDGE,
                List.of("Thames LSI0701 - Weighbridge IN"));
        allSwipeInDevices.put(Location.TL_PLAISTOW,
                List.of("PLA0102 - Turnstile West IN",
                        "PLA0104 - Turnstile East IN"));

        allSwipeOutDevices.put(Location.MAIN_GATE,
                List.of("Thames LSI0904 - Turnstile North OUT",
                        "Thames LSI0902 - Turnstile South OUT"));
        allSwipeOutDevices.put(Location.VISITORS_RECEPTION,
                List.of("Thames LSI0302 - Empl W Turnstile OUT",
                        "Thames LSI0304 - Empl E Turnstile OUT"));
        allSwipeOutDevices.put(Location.EP_WEIGHBRIDGE,
                List.of("Thames LSI0702 - Weighbridge OUT"));
        allSwipeOutDevices.put(Location.TL_PLAISTOW,
                List.of("PLA0103 - Turnstile West OUT",
                        "PLA0105 - Turnstile East OUT"));

    }

    public SwipeProcessor(List<SwipeRecord> swipeRecords) {
        this.swipeRecords = swipeRecords;
    }

    public SwipeRecord getFirstSwipeIn(Officer officer) {
        var allInOutSwipesOfAnOfficer = getAllInOutSwipesOfAnOfficer(officer);
        return allInOutSwipesOfAnOfficer.stream()
                .filter(swipeRecord -> allSwipeInDevices.get(officer.getLocation()).contains(swipeRecord.getDeviceName()))
                .filter(swipeRecord -> {
                    if (officer.isDayShift()) {
                        return swipeRecord.getSwipeTime().getHour() <= 12; // touch-in is before 12 noon
                    } else {
                        return swipeRecord.getSwipeTime().getHour() > 12; // touch in should be after 12 noon
                    }
                })
                .sorted()
                .findFirst()
                .orElseGet(() -> new SwipeRecord(officer.getFirstName(), officer.getLastName(), null, null));
    }

    public SwipeRecord getLastSwipeOut(Officer officer) {
        var allInOutSwipesOfAnOfficer = getAllInOutSwipesOfAnOfficer(officer); // duplicate work
        return allInOutSwipesOfAnOfficer.stream()
                .filter(swipeRecord -> allSwipeOutDevices.get(officer.getLocation()).contains(swipeRecord.getDeviceName()))
                .filter(swipeRecord -> {
                    if (officer.isDayShift()) {
                        return swipeRecord.getSwipeTime().getHour() >= 12; // touch out must be 12 noon or after
                    } else {
                        return swipeRecord.getSwipeTime().getHour() < 12; // touch out must be before 12 noon
                    }
                })
                .sorted()
                .reduce((first, second) -> second)
                .orElseGet(() -> new SwipeRecord(officer.getFirstName(), officer.getLastName(), null, null));
    }

    private List<SwipeRecord> getAllInOutSwipesOfAnOfficer(Officer officer) {
        List<SwipeRecord> allInOutSwipesOfAnOfficer = new ArrayList<>();
        for (SwipeRecord record : swipeRecords) {
            if (record.getFullName().equals(officer.getFullName())) {
                String firstName = officer.getFirstName();
                String lastName = officer.getLastName();
                String deviceName = record.getDeviceName();
                LocalDateTime swipeTime = record.getSwipeTime();
                if (allSwipeInDevices.get(officer.getLocation()).contains(deviceName) ||
                        allSwipeOutDevices.get(officer.getLocation()).contains(deviceName)) {
                    allInOutSwipesOfAnOfficer.add(new SwipeRecord(firstName, lastName, swipeTime, deviceName));
                }
            }
        }
        return allInOutSwipesOfAnOfficer;
    }
}
