package gmailgdogra;

import javax.validation.constraints.NotNull;

public class OutputRow {
    private final String firstName;
    private final String lastName;
    private final String swipeTime;
    private final String deviceName;

    public static OutputRow of(String firstName, String lastName, String swipeTime, String deviceName) {
        return new OutputRow(firstName, lastName, swipeTime, deviceName);
    }

    public OutputRow(@NotNull final String firstName, @NotNull final String lastName,
                     String swipeTime, String deviceName) {
        this.firstName = firstName;
        this.lastName = lastName;
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
