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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (!getFirstName().equals(record.getFirstName())) return false;
        if (!getLastName().equals(record.getLastName())) return false;
        if (!getSwipeTime().equals(record.getSwipeTime())) return false;
        return getDeviceName().equals(record.getDeviceName());
    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getSwipeTime().hashCode();
        result = 31 * result + getDeviceName().hashCode();
        return result;
    }
}
