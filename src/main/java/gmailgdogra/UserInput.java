package gmailgdogra;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UserInput {

    public void start() throws IOException {
        String filePath = getFilePath();
        List<Record> records = readFile(filePath);
        Map<String, String> officerAndLocation = getAllOfficersLocation(records);
        officerAndLocation.keySet()
                .forEach(key -> System.out.println(key + ":" + officerAndLocation.get(key)));
    }

    private String getFilePath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path: ");
        return scanner.nextLine();
    }

    private List<Record> readFile(String filePath) throws IOException {
        return ReadXlsx.parse(filePath);
    }

    private Map<String, String> getAllOfficersLocation(List<Record> records) {
        Scanner scanner = new Scanner(System.in);

        Set<String> officersNames = getAllOfficersNames(records);
        Map<String, String> officerAndLocation = new HashMap<>();

        for (String officerName : officersNames) {
            printAllLocations();
            System.out.print("\nOfficer: " + officerName + ", Enter Location(id): " );
            String locationAsNumber = scanner.nextLine().trim();
            String locationFullName = convertInputToFullName(locationAsNumber);
            if (locationFullName != null) {
                officerAndLocation.put(officerName, locationFullName);
            }
        }
        return officerAndLocation;
    }

    private String convertInputToFullName(String location) {
        switch(location) {
            case "1":
                return "Main Gate";
            case "2":
                return "Visitors Reception";
            case "3":
                return "EP WeighBridge";
            case "4":
                return "Plaistow";
            default:
                return null;
        }
    }

    private void printAllLocations() {
        System.out.println("Choose from below sites or press enter to ignore the officer");
        System.out.print("Main Gate(1), ");
        System.out.print("Visitors Reception(2), ");
        System.out.print("EP WeighBridge(3), ");
        System.out.print("Plaistow(4). ");
    }

    private Set<String> getAllOfficersNames(List<Record> records) {
        return records.stream()
                .map(Record::getFullName)
                .collect(Collectors.toSet());
    }
}
