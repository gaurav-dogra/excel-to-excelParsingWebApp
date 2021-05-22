package gmailgdogra;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UserInput {

    // returns Map<Officer Name, Location Name>
    public static Set<Officer> start(String filePathAndName) throws IOException {
        List<TransactionRecord> transactionRecords = readFile(filePathAndName);
        return getAllOfficersLocation(transactionRecords);
    }

    private static List<TransactionRecord> readFile(String filePath) throws IOException {
        return ReadXlsx.parse(filePath);
    }

    private static Set<Officer> getAllOfficersLocation(List<TransactionRecord> transactionRecords) {
        Scanner scanner = new Scanner(System.in);

        Set<Officer> officers = getAllOfficers(transactionRecords);

        for (Officer officer : officers) {
            printAllLocations();
            System.out.print("\nOfficer: " + officer.getFullName() + ", Enter Location(id): " );
            String locationAsNumber = scanner.nextLine().trim();
            Location location = convertInputToLocation(locationAsNumber);
            if (location != null) {
                officer.setLocation(location);
            }
        }
        return officers;
    }

    private static Location convertInputToLocation(String location) {
        switch(location) {
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
        System.out.println("Choose from below sites or press enter to ignore the officer");
        System.out.print("Main Gate(1), ");
        System.out.print("Visitors Reception(2), ");
        System.out.print("EP WeighBridge(3), ");
        System.out.print("TL Plaistow(4). ");
    }

    private static Set<Officer> getAllOfficers(List<TransactionRecord> transactionRecords) {
        return transactionRecords.stream()
                .map(transactionRecord -> new Officer(transactionRecord.getFirstName(), transactionRecord.getLastName()))
                .collect(Collectors.toSet());
    }
}
