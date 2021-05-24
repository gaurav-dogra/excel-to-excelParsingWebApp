package gmailgdogra;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class UserInput {

    public static Set<Officer> getOfficers(List<SwipeRecord> swipeRecords) {

        Set<Officer> officers = ExtractOfficers.from(swipeRecords);
        for (Officer officer : officers) {
            printAllLocations();
            Location location = getLocation(officer);
            boolean isDayShift = getShiftType();
            officer.setLocation(location);
            officer.setDayShift(isDayShift);
        }
        return officers;
    }

    private static Location getLocation(Officer officer) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nOfficer: " + officer.getFullName() + ", Enter Location(id): ");
        String locationAsNumber = scanner.nextLine().trim();
        return convertInputToLocation(locationAsNumber);
    }

    private static boolean getShiftType() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Is it day shift(y/n): ");
        String isDayShift = scanner.nextLine().trim().toLowerCase();
        if (isDayShift.equals("")) { return false; }
        return isDayShift.charAt(0) == 'y';
    }

    private static Location convertInputToLocation(String location) {
        switch (location) {
            case "1":
                return Location.MAIN_GATE;
            case "2":
                return Location.VISITORS_RECEPTION;
            case "3":
                return Location.EP_WEIGHBRIDGE;
            case "4":
                return Location.TL_PLAISTOW;
            default:
                return null;
        }
    }

    private static void printAllLocations() {
        System.out.println("===============================================================");
        System.out.println("Choose a number from below or press enter to ignore the officer");
        System.out.print("Main Gate(1), ");
        System.out.print("Visitors Reception(2), ");
        System.out.print("EP WeighBridge(3), ");
        System.out.print("TL Plaistow(4). ");
    }

}
