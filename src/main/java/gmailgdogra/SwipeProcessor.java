package gmailgdogra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SwipeProcessor {

    private static final List<String> allSwipeInDevices = new ArrayList<>();
    private static final List<String> allSwipeOutDevices = new ArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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

    public static List<OutputRow> getOutputDataFrom(List<SwipeRecord> allSwipes, List<Shift> shifts) {

        List<OutputRow> outputData = new ArrayList<>();
        List<SwipeRecord> onlyInOutSwipes = filterInOutSwipes(allSwipes);

        for (Shift shift : shifts) {

            List<SwipeRecord> swipesOfAnOfficer = onlyInOutSwipes.stream()
                    .filter(swipe -> shift.getOfficer().equals(swipe.getOfficer()))
                    .collect(Collectors.toList());

            OutputRow swipeIn = getSwipeIn(shift, swipesOfAnOfficer);
            OutputRow swipeOut = getSwipeOut(shift, swipesOfAnOfficer);
            outputData.add(swipeIn);
            outputData.add(swipeOut);
        }
        return outputData;
    }

    private static List<SwipeRecord> filterInOutSwipes(List<SwipeRecord> allSwipes) {
        return allSwipes.stream()
                .filter(swipe -> allSwipeInDevices.contains(swipe.getDeviceName()) ||
                        allSwipeOutDevices.contains(swipe.getDeviceName()))
                .collect(Collectors.toList());
    }

    private static OutputRow getSwipeIn(Shift shift, List<SwipeRecord> onlyInOutSwipes) {
        Officer shiftOfficer = shift.getOfficer();

        SwipeRecord record = onlyInOutSwipes.stream()
                .filter(swipe -> allSwipeInDevices.contains(swipe.getDeviceName()))
                .filter(swipe -> {
                    if (shift.isDayShift()) {
                        return swipe.getSwipeDateTime().getHour() >= 4 &&
                                swipe.getSwipeDateTime().getHour() <= 7;
                    } else {
                        return swipe.getSwipeDateTime().getHour() >= 16 &&
                                swipe.getSwipeDateTime().getHour() <= 19;
                    }
                })
                .sorted()
                .findFirst()
                .orElse(new SwipeRecord(shiftOfficer, null, null));

        return OutputRow.of(shift.getLocation().toString(), shiftOfficer.getFirstName(), shiftOfficer.getLastName(),
                parseDateTime(record.getSwipeDateTime()), record.getDeviceName());

    }

    private static OutputRow getSwipeOut(Shift shift, List<SwipeRecord> onlyInOutSwipes) {
        Officer shiftOfficer = shift.getOfficer();

        SwipeRecord record = onlyInOutSwipes.stream()
                .filter(swipe -> allSwipeOutDevices.contains(swipe.getDeviceName()))
                .filter(swipe -> {
                    if (shift.isDayShift()) {
                        return swipe.getSwipeDateTime().getHour() >= 16 &&
                                swipe.getSwipeDateTime().getHour() <= 19;
                    } else {
                        return swipe.getSwipeDateTime().getHour() >= 4 &&
                                swipe.getSwipeDateTime().getHour() <= 7;
                    }
                })
                .sorted()
                .reduce((first, second) -> second)
                .orElse(new SwipeRecord(shiftOfficer, null, null));

        return OutputRow.of(shift.getLocation().toString(), shiftOfficer.getFirstName(), shiftOfficer.getLastName(),
                parseDateTime(record.getSwipeDateTime()), record.getDeviceName());

    }

    private static String parseDateTime(LocalDateTime swipeDateTime) {
        if (swipeDateTime == null) {
            return null;
        }

        return swipeDateTime.format(formatter);
    }
}
