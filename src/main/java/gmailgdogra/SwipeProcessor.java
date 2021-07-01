package gmailgdogra;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class SwipeProcessor {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final Map<String, Location> swipeInDevices;
    public static final Map<String, Location> swipeOutDevices;
    private static LocalDate swipeInDay;


    static {
        swipeInDevices = new HashMap<>();
        swipeInDevices.put("Thames LSI0903 - Turnstile North IN", Location.MAIN_GATE);
        swipeInDevices.put("Thames LSI0901 - Turnstile South IN", Location.MAIN_GATE);
        swipeInDevices.put("Thames LSI0303 - Empl East Turnstile IN", Location.VISITORS_RECEPTION);
        swipeInDevices.put("Thames LSI0301 - Empl West Turnstile IN", Location.VISITORS_RECEPTION);
        swipeInDevices.put("Thames LSI0701 - Weighbridge IN", Location.EP_WEIGHBRIDGE);
        swipeInDevices.put("PLA0102 - Turnstile West IN", Location.TL_PLAISTOW);
        swipeInDevices.put("PLA0104 - Turnstile East IN", Location.TL_PLAISTOW);

        swipeOutDevices = new HashMap<>();
        swipeOutDevices.put("Thames LSI0904 - Turnstile North OUT", Location.MAIN_GATE);
        swipeOutDevices.put("Thames LSI0902 - Turnstile South OUT", Location.MAIN_GATE);
        swipeOutDevices.put("Thames LSI0302 - Empl W Turnstile OUT", Location.VISITORS_RECEPTION);
        swipeOutDevices.put("Thames LSI0304 - Empl E Turnstile OUT", Location.VISITORS_RECEPTION);
        swipeOutDevices.put("Thames LSI0702 - Weighbridge OUT", Location.EP_WEIGHBRIDGE);
        swipeOutDevices.put("PLA0103 - Turnstile West OUT", Location.TL_PLAISTOW);
        swipeOutDevices.put("PLA0105 - Turnstile East OUT", Location.TL_PLAISTOW);
    }

    public static List<OutputRow> getOutputDataFrom(List<SwipeRecord> allSwipes, List<Shift> shifts) {
        List<OutputRow> outputData = new ArrayList<>();
        Set<LocalDate> allDates = allSwipes.stream()
                .map(swipe -> swipe.getSwipeDateTime().toLocalDate())
                .collect(Collectors.toSet());
        swipeInDay = Collections.min(allDates);

        for (Shift shift : shifts) {
            OutputRow swipeIn = getSwipeIn(shift, allSwipes);
            OutputRow swipeOut = getSwipeOut(shift, allSwipes);
            outputData.add(swipeIn);
            outputData.add(swipeOut);
        }
        return outputData;
    }

    private static OutputRow getSwipeIn(Shift shift, List<SwipeRecord> allSwipes) {
        Officer officer = shift.getOfficer();
        Location location = shift.getLocation();

        SwipeRecord record = allSwipes.stream()
                .filter(swipe -> officer.equals(swipe.getOfficer()))
                .filter(swipe -> swipeInDevices.containsKey(swipe.getDeviceName()))
                .filter(swipe -> location == swipeInDevices.get(swipe.getDeviceName()))
                .filter(swipe -> swipe.getSwipeDateTime().toLocalDate().isEqual(swipeInDay))
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
                .orElse(new SwipeRecord(officer, null, null));

        return OutputRow.of(location.toString(), officer.getFirstName(), officer.getLastName(),
                parseDateTime(record.getSwipeDateTime()), record.getDeviceName());
    }

    private static OutputRow getSwipeOut(Shift shift, List<SwipeRecord> allSwipes) {
        Officer officer = shift.getOfficer();
        Location location = shift.getLocation();

        SwipeRecord record = allSwipes.stream()
                .filter(swipe -> officer.equals(swipe.getOfficer()))
                .filter(swipe -> swipeOutDevices.containsKey(swipe.getDeviceName()))
                .filter(swipe -> location == swipeOutDevices.get(swipe.getDeviceName()))
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
                .orElse(new SwipeRecord(officer, null, null));

        return OutputRow.of(location.toString(), officer.getFirstName(), officer.getLastName(),
                parseDateTime(record.getSwipeDateTime()), record.getDeviceName());
    }

    private static String parseDateTime(LocalDateTime swipeDateTime) {
        if (swipeDateTime == null) {
            return null;
        }
        return swipeDateTime.format(formatter);
    }
}
