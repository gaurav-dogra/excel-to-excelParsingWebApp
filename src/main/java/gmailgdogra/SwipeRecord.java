package gmailgdogra;

import java.time.LocalDateTime;

public class SwipeRecord implements Comparable<SwipeRecord> {
    private String firstName;
    private String lastName;
    private LocalDateTime swipeTime;
    private String deviceName;

    public SwipeRecord(String firstName, String lastName, LocalDateTime swipeTime, String deviceName) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SwipeRecord swipeRecord = (SwipeRecord) o;

        if (!getFirstName().equals(swipeRecord.getFirstName())) return false;
        if (!getLastName().equals(swipeRecord.getLastName())) return false;
        if (!getSwipeTime().equals(swipeRecord.getSwipeTime())) return false;
        return getDeviceName().equals(swipeRecord.getDeviceName());
    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getSwipeTime().hashCode();
        result = 31 * result + getDeviceName().hashCode();
        return result;
    }

    @Override
    public int compareTo(SwipeRecord o) {
        return this.swipeTime.compareTo(o.swipeTime);
    }
}
