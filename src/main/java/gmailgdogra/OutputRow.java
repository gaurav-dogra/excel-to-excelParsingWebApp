package gmailgdogra;

import javax.validation.constraints.NotNull;

public class OutputRow implements Comparable<OutputRow> {

    private final String location;
    private final String firstName;
    private final String lastName;
    private final String eventDate;
    private final String logicalDevice;

    public static OutputRow of(@NotNull String location, @NotNull String firstName, @NotNull String lastName,
                               String eventDate, String deviceName) {
        return new OutputRow(location, firstName, lastName, eventDate, deviceName);
    }

    public OutputRow(String location, String firstName, String lastName,
                     String eventDate, String logicalDevice) {
        this.location = location;
        this.firstName = firstName;
        this.lastName = lastName;
        this.eventDate = eventDate;
        this.logicalDevice = logicalDevice;
    }

    public String getLocation() {
        return location;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getLogicalDevice() {
        return logicalDevice;
    }

    @Override
    public String toString() {
        return "OutputRow{" +
                "location='" + location + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", logicalDevice='" + logicalDevice + '\'' +
                '}';
    }

    @Override
    public int compareTo(OutputRow o) {
        return getLocation().compareTo(o.getLocation());
    }
}
