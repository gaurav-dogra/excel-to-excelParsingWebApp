package gmailgdogra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PrepareOutputData {

    private static List<TransactionRecord> transactionRecords;

    public static List<TransactionRecord> from(String filePathAndName) throws IOException {
        transactionRecords = ReadXlsx.parse(filePathAndName);
        Set<Officer> officers = UserInput.getOfficersLocationFromUser(transactionRecords);
        return getOutputRows(officers);
    }

    private static List<TransactionRecord> getOutputRows(Set<Officer> officers) {
        List<TransactionRecord> rows = new ArrayList<>();

        for (Officer officer : officers) {
            if (officer.getLocation() != null) {
                rows.add(getSignInRow(officer));
                rows.add(getSignOutRow(officer));
            }
        }

        return rows;
    }

    private static TransactionRecord getSignInRow(Officer officer) {

        Location officerLocation = officer.getLocation();
        String officerFullName = officer.getFullName();
        String[] expectedTouchInDevice = getExpectedTouchInDevice(officerLocation);
        List<TransactionRecord> allTouchIns = new ArrayList<>();

        for (String deviceName : expectedTouchInDevice) {
            for (TransactionRecord record : transactionRecords) {

                if (record.getFullName().equals(officerFullName)
                        && deviceName.equals(record.getDeviceName())) {
                    allTouchIns.add(record);
                }
            }
        }

        return allTouchIns.stream()
                .sorted()
                .findFirst()
                .orElse(getFirstTouchAnywhere(officer.getFullName()));
    }

    private static TransactionRecord getFirstTouchAnywhere(String fullName) {
        return transactionRecords.stream()
                .filter(record -> record.getFullName().equals(fullName))
                .sorted()
                .findFirst()
                .get();
    }

    private static String[] getExpectedTouchInDevice(Location location) {

        switch (location.toString()) {
            case "MAIN_GATE":
            default:
                return new String[]{"Thames LSI0903 - Turnstile North IN",
                        "Thames LSI0901 - Turnstile South IN"};

            case "VISITORS_RECEPTION":
                return new String[]{"Thames LSI0303 - Empl East Turnstile IN",
                        "Thames LSI0301 - Empl West Turnstile IN"};

            case "EP_WEIGHBRIDGE":
                return new String[]{"Thames LSI0701 - Weighbridge IN"};

            case "TL_PLAISTOW":
                return new String[]{"PLA0102 - Turnstile West IN",
                        "PLA0104 - Turnstile East IN"};

        }
    }

    private static TransactionRecord getSignOutRow(Officer officer) {
        Location officerLocation = officer.getLocation();
        String officerFullName = officer.getFullName();
        String[] expectedTouchOutDevice = getExpectedTouchOutDevice(officerLocation);
        List<TransactionRecord> allTouchOuts = new ArrayList<>();

        for (String deviceName : expectedTouchOutDevice) {
            for (TransactionRecord record : transactionRecords) {

                if (record.getFullName().equals(officerFullName)
                        && deviceName.equals(record.getDeviceName())) {
                    allTouchOuts.add(record);
                }
            }
        }

        return allTouchOuts.stream()
                .sorted()
                .skip(allTouchOuts.size() - 1)
                .findFirst()
                .get();
    }

    private static String[] getExpectedTouchOutDevice(Location location) {

        switch (location.toString()) {
            case "MAIN_GATE":
            default:
                return new String[]{"Thames LSI0904 - Turnstile North OUT",
                        "Thames LSI0902 - Turnstile South OUT"};

            case "VISITORS_RECEPTION":
                return new String[]{"",
                        "Thames LSI0302 - Empl W Turnstile OUT",
                        "Thames LSI0304 - Empl E Turnstile OUT"}; // to be checked

            case "EP_WEIGHBRIDGE":
                return new String[]{"Thames LSI0702 - Weighbridge OUT"};

            case "TL_PLAISTOW":
                return new String[]{"PLA0103 - Turnstile West OUT",
                        "PLA0105 - Turnstile East OUT"};

        }
    }


}
