package gmailgdogra;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class UserInput {

    public static Set<Officer> getOfficersLocationFromUser(List<TransactionRecord> transactionRecords) {

        Scanner scanner = new Scanner(System.in);
        Set<Officer> officers = ExtractOfficers.from(transactionRecords);

        for (Officer officer : officers) {
            printAllLocations();
            System.out.print("\nOfficer: " + officer.getFullName() + ", Enter Location(id): ");
            String locationAsNumber = scanner.nextLine().trim();
            Location location = convertInputToLocation(locationAsNumber);
            officer.setLocation(location);
        }
        return officers;
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
        System.out.println("Choose a number from below or press enter to ignore the officer");
        System.out.print("Main Gate(1), ");
        System.out.print("Visitors Reception(2), ");
        System.out.print("EP WeighBridge(3), ");
        System.out.print("TL Plaistow(4). ");
    }

}
