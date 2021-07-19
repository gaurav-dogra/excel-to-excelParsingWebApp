package gmailgdogra.service;

import gmailgdogra.pojo.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SwipeProcessorService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final Map<String, Location> swipeInDevicesMap;
    public static final Map<String, Location> swipeOutDevicesMap;
    private static LocalDate dayOne;
    private List<OutputRow> inOutSwipesPrevTwoShifts = new ArrayList<>();
    private List<OutputRow> inSwipesCurrentShift = new ArrayList<>();

    static {
        swipeInDevicesMap = new HashMap<>();
        swipeInDevicesMap.put("Thames LSI0903 - Turnstile North IN", Location.MAIN_GATE);
        swipeInDevicesMap.put("Thames LSI0901 - Turnstile South IN", Location.MAIN_GATE);
        swipeInDevicesMap.put("Thames LSI0303 - Empl East Turnstile IN", Location.VISITORS_RECEPTION);
        swipeInDevicesMap.put("Thames LSI0301 - Empl West Turnstile IN", Location.VISITORS_RECEPTION);
        swipeInDevicesMap.put("Thames LSI0701 - Weighbridge IN", Location.EP_WEIGHBRIDGE);
        swipeInDevicesMap.put("PLA0102 - Turnstile West IN", Location.TL_PLAISTOW);
        swipeInDevicesMap.put("PLA0104 - Turnstile East IN", Location.TL_PLAISTOW);

        swipeOutDevicesMap = new HashMap<>();
        swipeOutDevicesMap.put("Thames LSI0904 - Turnstile North OUT", Location.MAIN_GATE);
        swipeOutDevicesMap.put("Thames LSI0902 - Turnstile South OUT", Location.MAIN_GATE);
        swipeOutDevicesMap.put("Thames LSI0302 - Empl W Turnstile OUT", Location.VISITORS_RECEPTION);
        swipeOutDevicesMap.put("Thames LSI0304 - Empl E Turnstile OUT", Location.VISITORS_RECEPTION);
        swipeOutDevicesMap.put("Thames LSI0702 - Weighbridge OUT", Location.EP_WEIGHBRIDGE);
        swipeOutDevicesMap.put("PLA0103 - Turnstile West OUT", Location.TL_PLAISTOW);
        swipeOutDevicesMap.put("PLA0105 - Turnstile East OUT", Location.TL_PLAISTOW);
    }

    public void prepareData(List<SwipeRecord> allSwipes, List<Shift> shifts) {
        dayOne = getDayOneFromSwipes(allSwipes);
        inOutSwipesPrevTwoShifts = getPrevShiftInOutSwipes(allSwipes, shifts);
        inSwipesCurrentShift = getCurrentShiftInSwipes(allSwipes, shifts);
        System.out.println("swipeInsCurrentShift = " + inSwipesCurrentShift);
    }

    private List<OutputRow> getCurrentShiftInSwipes(List<SwipeRecord> allSwipes, List<Shift> shifts) {
        Set<Officer> currentShiftOfficers = getCurrentShiftOfficers(allSwipes, shifts);
        List<OutputRow> returnedValueList = new ArrayList<>();

        List<SwipeRecord> selectedSwipes = allSwipes.stream()
                .filter(swipe -> swipe.getSwipeDateTime().toLocalDate().isAfter(dayOne))
                .filter(swipe -> swipeInDevicesMap.containsKey(swipe.getDeviceName()))
                .filter(swipe -> swipe.getSwipeDateTime().getHour() >= 4 &&
                        swipe.getSwipeDateTime().getHour() <= 7)
                .collect(Collectors.toList());

        for (Officer officer : currentShiftOfficers) {
            OutputRow row = getCurrentShiftSwipeIn(officer, selectedSwipes);
            returnedValueList.add(row);
        }
        return returnedValueList;
    }

    private OutputRow getCurrentShiftSwipeIn(Officer officer, List<SwipeRecord> selectedSwipes) {
        SwipeRecord swipeInRecord = selectedSwipes.stream()
                .filter(swipe -> officer.equals(swipe.getOfficer()))
                .sorted()
                .findFirst()
                .orElseThrow(RuntimeException::new);
        String location = swipeInDevicesMap.get(swipeInRecord.getDeviceName()).toString();
        return OutputRow.of(location, officer.getFirstName(), officer.getLastName(),
                parseDateTime(swipeInRecord.getSwipeDateTime()), swipeInRecord.getDeviceName());
    }

    private Set<Officer> getCurrentShiftOfficers(List<SwipeRecord> allSwipes, List<Shift> shifts) {
        Set<Officer> prevNightOfficers = getPreviousNightOfficers(shifts);
        return allSwipes.stream()
                .filter(swipe -> swipe.getSwipeDateTime().toLocalDate().isAfter(dayOne))
                .filter(swipe -> swipeInDevicesMap.containsKey(swipe.getDeviceName()))
                .filter(swipe -> !prevNightOfficers.contains(swipe.getOfficer()))
                .filter(swipe -> swipe.getSwipeDateTime().getHour() >= 4 &&
                        swipe.getSwipeDateTime().getHour() <= 7)
                .map(SwipeRecord::getOfficer)
                .collect(Collectors.toSet());
    }

    private Set<Officer> getPreviousNightOfficers(List<Shift> shifts) {
        return shifts.stream()
                .filter(shift -> !shift.isDayShift())
                .map(Shift::getOfficer)
                .collect(Collectors.toSet());
    }

    private List<OutputRow> getPrevShiftInOutSwipes(List<SwipeRecord> allSwipes, List<Shift> shifts) {
        List<OutputRow> dailyReportDataList = new ArrayList<>();
        for (Shift shift : shifts) {
            OutputRow swipeIn = getSwipeIn(shift, allSwipes);
            OutputRow swipeOut = getSwipeOut(shift, allSwipes);
            dailyReportDataList.add(swipeIn);
            dailyReportDataList.add(swipeOut);
        }
        Collections.sort(dailyReportDataList);
        return dailyReportDataList;
    }

    private LocalDate getDayOneFromSwipes(List<SwipeRecord> allSwipes) {
        Set<LocalDate> allDates = allSwipes.stream()
                .map(swipe -> swipe.getSwipeDateTime().toLocalDate())
                .collect(Collectors.toSet());
        return Collections.min(allDates);

    }

    private OutputRow getSwipeIn(Shift shift, List<SwipeRecord> allSwipes) {
        Officer officer = shift.getOfficer();
        Location location = shift.getLocation();

        SwipeRecord record = allSwipes.stream()
                .filter(swipe -> officer.equals(swipe.getOfficer()))
                .filter(swipe -> swipeInDevicesMap.containsKey(swipe.getDeviceName()))
                .filter(swipe -> location == swipeInDevicesMap.get(swipe.getDeviceName()))
                .filter(swipe -> swipe.getSwipeDateTime().toLocalDate().isEqual(dayOne))
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

    private OutputRow getSwipeOut(Shift shift, List<SwipeRecord> allSwipes) {
        Officer officer = shift.getOfficer();
        Location location = shift.getLocation();

        SwipeRecord record = allSwipes.stream()
                .filter(swipe -> officer.equals(swipe.getOfficer()))
                .filter(swipe -> swipeOutDevicesMap.containsKey(swipe.getDeviceName()))
                .filter(swipe -> location == swipeOutDevicesMap.get(swipe.getDeviceName()))
                .filter(swipe -> {
                    if (shift.isDayShift()) {
                        return swipe.getSwipeDateTime().getHour() >= 16 &&
                                swipe.getSwipeDateTime().getHour() <= 19;
                    } else {
                        return swipe.getSwipeDateTime().getHour() >= 4 &&
                                swipe.getSwipeDateTime().getHour() <= 7 &&
                                swipe.getSwipeDateTime().toLocalDate().isEqual(dayOne.plusDays(1));
                    }
                })
                .sorted()
                .reduce((first, second) -> second)
                .orElse(new SwipeRecord(officer, null, null));

        return OutputRow.of(location.toString(), officer.getFirstName(), officer.getLastName(),
                parseDateTime(record.getSwipeDateTime()), record.getDeviceName());
    }

    private String parseDateTime(LocalDateTime swipeDateTime) {
        if (swipeDateTime == null) {
            return null;
        }
        return swipeDateTime.format(formatter);
    }

    public List<OutputRow> getInOutSwipesPrevTwoShifts() {
        return inOutSwipesPrevTwoShifts;
    }

    public List<OutputRow> getInSwipesCurrentShift() {
        return inSwipesCurrentShift;
    }

}
