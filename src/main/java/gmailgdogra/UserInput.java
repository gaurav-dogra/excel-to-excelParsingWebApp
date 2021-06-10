package gmailgdogra;

import java.util.*;

public class UserInput {

    public static List<Shift> getShiftDetails(Set<Officer> allOfficers) {
        List<Shift> shifts = new ArrayList<>();

        for (Officer officer : allOfficers) {
            System.out.println("==============");
            System.out.println(officer);
            System.out.println("==============");
            System.out.println("Please select appropriate number from below");
            System.out.println("MG Day Shift(1)");
            System.out.println("MG Night Shift(2)");
            System.out.println("EP WB Day Shift(3)");
            System.out.println("EP WB Night Shift(4)");
            System.out.println("Plaistow Day Shift(5)");
            System.out.println("Plaistow Night Shift(6)");
            System.out.println("Visitors Reception Shift(7)");
            System.out.println("Press enter if the officer does not have any shift");
            System.out.print(">");
            int n = getInput();
            Optional<Shift> shiftInfo = getShiftInfo(n, officer);
            shiftInfo.ifPresent(shifts::add);
        }
        System.out.println("All inputs recorded");
        return shifts;
    }

    private static Optional<Shift> getShiftInfo(int n, Officer officer) {

        switch (n) {
            case 1:
                return Optional.of(new Shift(officer, Location.MAIN_GATE, true));
            case 2:
                return Optional.of(new Shift(officer, Location.MAIN_GATE, false));
            case 3:
                return Optional.of(new Shift(officer, Location.EP_WEIGHBRIDGE, true));
            case 4:
                return Optional.of(new Shift(officer, Location.EP_WEIGHBRIDGE, false));
            case 5:
                return Optional.of(new Shift(officer, Location.TL_PLAISTOW, true));
            case 6:
                return Optional.of(new Shift(officer, Location.TL_PLAISTOW, false));
            case 7:
                return Optional.of(new Shift(officer, Location.VISITORS_RECEPTION, true));
            default:
                return Optional.empty();
        }
    }

    private static int getInput() {
        Scanner scanner = new Scanner(System.in);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }

}
