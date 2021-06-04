package gmailgdogra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SwipeProcessor {

    private static final List<String> allSwipeInDevices = new ArrayList<>();
    private static final List<String> allSwipeOutDevices = new ArrayList<>();
    private static LocalDate firstDay;

    static {
        allSwipeInDevices.add("Thames LSI0903 - Turnstile North IN");
        allSwipeInDevices.add("Thames LSI0901 - Turnstile South IN");
        allSwipeInDevices.add("Thames LSI0303 - Empl East Turnstile IN");
        allSwipeInDevices.add("Thames LSI0301 - Empl West Turnstile IN");
        allSwipeInDevices.add("Thames LSI0701 - Weighbridge IN");
        allSwipeInDevices.add("PLA0102 - Turnstile West IN");
        allSwipeInDevices.add("PLA0104 - Turnstile East IN");

        allSwipeOutDevices.add("Thames LSI0904 - Turnstile North OUT");
        allSwipeOutDevices.add("Thames LSI0902 - Turnstile South OUT");
        allSwipeOutDevices.add("Thames LSI0302 - Empl W Turnstile OUT");
        allSwipeOutDevices.add("Thames LSI0304 - Empl E Turnstile OUT");
        allSwipeOutDevices.add("Thames LSI0702 - Weighbridge OUT");
        allSwipeOutDevices.add("PLA0103 - Turnstile West OUT");
        allSwipeOutDevices.add("PLA0105 - Turnstile East OUT");

    }

    public static List<SwipeRecord> getOutputDataFrom(List<SwipeRecord> allSwipes) {

        List<SwipeRecord> outputData = new ArrayList<>();
        List<SwipeRecord> allInOutSwipes = allSwipes.stream()
                .filter(swipe -> allSwipeInDevices.contains(swipe.getDeviceName()) ||
                        allSwipeOutDevices.contains(swipe.getDeviceName()))
                .collect(Collectors.toList());
        Set<Officer> officers = ExtractOfficers.from(allInOutSwipes);
        firstDay = getFirstDay(allInOutSwipes);

        for (Officer officer : officers) {

            SwipeRecord swipeIn = getSwipeIn(allInOutSwipes, officer);
            System.out.println("swipeIn = " + swipeIn);
            SwipeRecord swipeOut = getSwipeOut(allInOutSwipes, officer);
            System.out.println("swipeOut = " + swipeOut);
        }
        return outputData;
    }

    private static SwipeRecord getSwipeOut(List<SwipeRecord> allInOutSwipes, Officer officer) {

        Stream<SwipeRecord> recordsStream = allInOutSwipes.stream()
                .filter(swipe -> swipe.getFullName().equals(officer.getFullName()))
                .filter(swipe -> allSwipeOutDevices.contains(swipe.getDeviceName()))
                .filter(swipe -> {
                    if (swipe.getSwipeDateTime().toLocalDate().equals(firstDay)) {
                        return swipe.getSwipeDateTime().toLocalTime().getHour() >= 17;
                    } else {
                        return true;
                    }
                });

        if (isNightShift(allInOutSwipes, officer)) {
            return recordsStream
                    .filter(swipe -> swipe.getSwipeDateTime().getHour() >= 5 &&
                            swipe.getSwipeDateTime().getHour() <= 9)
                    .sorted()
                    .reduce((first, second) -> second)
                    .orElseGet(() -> new SwipeRecord(officer.getFirstName(), officer.getLastName(), null, null));
        } else {
            return recordsStream
                    .filter(swipe -> swipe.getSwipeDateTime().getHour() >= 17 &&
                            swipe.getSwipeDateTime().getHour() <= 21)
                    .sorted()
                    .reduce((first, second) -> second)
                    .orElseGet(() -> new SwipeRecord(officer.getFirstName(), officer.getLastName(), null, null));
        }
    }

    private static SwipeRecord getSwipeIn(List<SwipeRecord> allInOutSwipes, Officer officer) {

        Stream<SwipeRecord> recordsStream = allInOutSwipes.stream()
                .filter(swipe -> swipe.getFullName().equals(officer.getFullName()))
                .filter(swipe -> allSwipeInDevices.contains(swipe.getDeviceName()))
                .filter(swipe -> swipe.getSwipeDateTime().toLocalDate().isEqual(firstDay));
        System.out.println(officer + ": " + isNightShift(allInOutSwipes, officer));

        if (isNightShift(allInOutSwipes, officer)) {
            return recordsStream
                    .filter(swipe -> swipe.getSwipeDateTime().getHour() >= 17 &&
                            swipe.getSwipeDateTime().getHour() <= 21)
                    .sorted()
                    .findFirst()
                    .orElseGet(() -> new SwipeRecord(officer.getFirstName(), officer.getLastName(), null, null));
        } else {
            return recordsStream
                    .filter(swipe -> swipe.getSwipeDateTime().getHour() >= 5 &&
                            swipe.getSwipeDateTime().getHour() <= 9)
                    .sorted()
                    .findFirst()
                    .orElseGet(() -> new SwipeRecord(officer.getFirstName(), officer.getLastName(), null, null));
        }
    }

    private static boolean isNightShift(List<SwipeRecord> allInOutSwipes, Officer officer) {

        if (getStreamOfSwipesForGivenOfficer(allInOutSwipes, officer)
                .anyMatch(swipe -> swipe.getSwipeDateTime().getHour() > 21)) {
            return true;
        }

        if (getStreamOfSwipesForGivenOfficer(allInOutSwipes, officer)
                .anyMatch(swipe -> swipe.getSwipeDateTime().getHour() > 9 &&
                        swipe.getSwipeDateTime().getHour() < 17)) {
            return false;
        }

        return getStreamOfSwipesForGivenOfficer(allInOutSwipes, officer)
                .filter(swipe -> allSwipeOutDevices.contains(swipe.getDeviceName()))
                .anyMatch(swipe -> swipe.getSwipeDateTime().getHour() >= 5 &&
                        swipe.getSwipeDateTime().getHour() <= 9);
    }

    private static Stream<SwipeRecord> getStreamOfSwipesForGivenOfficer(List<SwipeRecord> allInOutSwipes, Officer officer) {
        return allInOutSwipes.stream()
                .filter(swipe -> swipe.getFullName().equals(officer.getFullName()));
    }

    private static LocalDate getFirstDay(List<SwipeRecord> allSwipes) {
        return allSwipes.stream()
                .map(swipeRecord -> swipeRecord.getSwipeDateTime().toLocalDate())
                .distinct()
                .sorted()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failure to read the data"));
    }

}
