package gmailgdogra;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getSwipeDateTime() {
        return swipeTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String toString() {
        return getFirstName() + " " +
                getLastName() + ", " +
                getSwipeDateTime() + ", " +
                getDeviceName();
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SwipeRecord that = (SwipeRecord) o;

        if (!getFirstName().equals(that.getFirstName())) return false;
        if (!getLastName().equals(that.getLastName())) return false;
        if (!Objects.equals(swipeTime, that.swipeTime)) return false;
        return getDeviceName() != null ? getDeviceName().equals(that.getDeviceName()) : that.getDeviceName() == null;
    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + (swipeTime != null ? swipeTime.hashCode() : 0);
        result = 31 * result + (getDeviceName() != null ? getDeviceName().hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(SwipeRecord o) {
        return this.swipeTime.compareTo(o.swipeTime);
    }
}
