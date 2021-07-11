package gmailgdogra.pojo;

import java.time.LocalDateTime;
import java.util.Objects;

public class SwipeRecord implements Comparable<SwipeRecord> {
    private final Officer officer;
    private final LocalDateTime swipeTime;
    private final String deviceName;

    public SwipeRecord(Officer officer, LocalDateTime swipeTime, String deviceName) {
        this.officer = officer;
        this.swipeTime = swipeTime;
        this.deviceName = deviceName;
    }

    public Officer getOfficer() {
        return officer;
    }

    public LocalDateTime getSwipeDateTime() {
        return swipeTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String toString() {
        return officer + ", " +
                getSwipeDateTime() + ", " +
                getDeviceName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SwipeRecord that = (SwipeRecord) o;

        if (!getOfficer().equals(that.getOfficer())) return false;
        if (!Objects.equals(swipeTime, that.swipeTime)) return false;
        return getDeviceName() != null ? getDeviceName().equals(that.getDeviceName()) : that.getDeviceName() == null;
    }

    @Override
    public int hashCode() {
        int result = getOfficer().hashCode();
        result = 31 * result + (swipeTime != null ? swipeTime.hashCode() : 0);
        result = 31 * result + (getDeviceName() != null ? getDeviceName().hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(SwipeRecord o) {
        return this.swipeTime.compareTo(o.swipeTime);
    }
}
