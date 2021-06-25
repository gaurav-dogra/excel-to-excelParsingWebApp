package gmailgdogra;

import javax.validation.constraints.NotNull;

public class Shift {
    private final Officer officer;
    private Location location;
    private boolean isDayShift;

    public Shift(Officer officer) {
        this.officer = officer;
    }

    public Shift(@NotNull Officer officer, Location location, boolean isDayShift) {
        this.officer = officer;
        this.location = location;
        this.isDayShift = isDayShift;
    }

    public Officer getOfficer() {
        return officer;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isDayShift() {
        return isDayShift;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "officer=" + officer +
                ", location=" + location +
                ", isDayShift=" + isDayShift +
                '}';
    }
}
