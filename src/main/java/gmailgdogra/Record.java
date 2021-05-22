package gmailgdogra;

import java.time.LocalDateTime;

public class Record {
    private String firstName;
    private String lastName;
    private LocalDateTime swipeTime;
    private String deviceName;

    public Record(String firstName, String lastName, LocalDateTime swipeTime, String deviceName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.swipeTime = swipeTime;
        this.deviceName = deviceName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getSwipeTime() {
        return swipeTime;
    }

    public void setSwipeTime(LocalDateTime swipeTime) {
        this.swipeTime = swipeTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public String toString() {
        return getFirstName() + " " +
                getLastName() + ", " +
                getSwipeTime() + ", " +
                getDeviceName();
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
