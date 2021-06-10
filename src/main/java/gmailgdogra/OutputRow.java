package gmailgdogra;

import javax.validation.constraints.NotNull;

public class OutputRow {
    private final String firstName;
    private final String lastName;
    private final String swipeTime;
    private final String deviceName;

    public static OutputRow of(Officer officer, String swipeTime, String deviceName) {
        return new OutputRow(officer, swipeTime, deviceName);
    }

    public OutputRow(@NotNull Officer officer, String swipeTime, String deviceName) {
        this.firstName = officer.getFirstName();
        this.lastName = officer.getLastName();
        this.swipeTime = swipeTime;
        this.deviceName = deviceName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSwipeTime() {
        return swipeTime;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
