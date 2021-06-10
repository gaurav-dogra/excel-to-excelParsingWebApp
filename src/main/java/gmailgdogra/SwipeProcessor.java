package gmailgdogra;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SwipeProcessor {

    private static final List<String> allSwipeInDevices = new ArrayList<>();
    private static final List<String> allSwipeOutDevices = new ArrayList<>();

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

    public static List<SwipeRecord> getOutputDataFrom(List<SwipeRecord> allSwipes, List<Shift> shifts) {

        List<SwipeRecord> outputData = new ArrayList<>();
        List<SwipeRecord> onlyInOutSwipes = filterInOutSwipes(allSwipes);

        for (Shift shift : shifts) {
            outputData.add(getSwipeIn(shift, onlyInOutSwipes));
            outputData.add(getSwipeOut(shift, onlyInOutSwipes));
        }
        return outputData;
    }

    private static List<SwipeRecord> filterInOutSwipes(List<SwipeRecord> allSwipes) {
        return allSwipes.stream()
                .filter(swipe -> allSwipeInDevices.contains(swipe.getDeviceName()) ||
                        allSwipeOutDevices.contains(swipe.getDeviceName()))
                .collect(Collectors.toList());
    }

    private static SwipeRecord getSwipeIn(Shift shift, List<SwipeRecord> onlyInOutSwipes) {
        return onlyInOutSwipes.stream()
                .filter(swipe -> swipe.getOfficer().equals(shift.getOfficer()))
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
                .orElse(new SwipeRecord(shift.getOfficer(), null, null));
    }

    private static SwipeRecord getSwipeOut(Shift shift, List<SwipeRecord> onlyInOutSwipes) {
        return onlyInOutSwipes.stream()
                .filter(swipe -> swipe.getOfficer().equals(shift.getOfficer()))
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
                .orElse(new SwipeRecord(shift.getOfficer(), null, null));
    }

}
