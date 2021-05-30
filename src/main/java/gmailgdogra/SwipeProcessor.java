package gmailgdogra;

import java.util.*;

public class SwipeProcessor {

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

    public static List<SwipeRecord> getOutputDataFrom(List<SwipeRecord> allSwipes) {

        List<SwipeRecord> outputData = new ArrayList<>();
        Set<Officer> officers = UserInput.getOfficers(allSwipes);
        for (Officer officer : officers) {
            if (officer.getLocation() != null) {
                outputData.add(getFirstSwipeIn(officer, allSwipes)); // swipe in row
                outputData.add(getLastSwipeOut(officer, allSwipes)); // swipe out row
            }
        }
        return outputData;
    }

    public static SwipeRecord getFirstSwipeIn(Officer officer, List<SwipeRecord> allSwipes) {
        Location loc = officer.getLocation();
        return allSwipes.stream()
                .filter(swipe -> officer.getFullName().equals(swipe.getFullName()))
                .filter(swipe -> allSwipeInDevices.get(loc).contains(swipe.getDeviceName()))
                .filter(swipe -> {
                    if (officer.isDayShift()) {
                        return swipe.getSwipeTime().getHour() <= 12; // touch-in is before 12:59 afternoon
                    } else {
                        return swipe.getSwipeTime().getHour() > 12; // touch-in on or after 13:00
                    }
                })
                .sorted()
                .findFirst()
                .orElseGet(() -> new SwipeRecord(officer.getFirstName(), officer.getLastName(), null, null));
    }

    public static SwipeRecord getLastSwipeOut(Officer officer, List<SwipeRecord> allSwipes) {
        Location loc = officer.getLocation();
        return allSwipes.stream()
                .filter(swipe -> officer.getFullName().equals(swipe.getFullName()))
                .filter(swipe -> allSwipeOutDevices.get(loc).contains(swipe.getDeviceName()))
                .filter(swipe -> {
                    if (officer.isDayShift()) {
                        return swipe.getSwipeTime().getHour() >= 12; // touch out must be 12 noon or after
                    } else {
                        return swipe.getSwipeTime().getHour() < 12; // touch out must be before 12 noon
                    }
                })
                .sorted()
                .reduce((first, second) -> second)
                .orElseGet(() -> new SwipeRecord(officer.getFirstName(), officer.getLastName(), null, null));
    }

}
