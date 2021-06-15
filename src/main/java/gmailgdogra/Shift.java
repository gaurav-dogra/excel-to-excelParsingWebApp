package gmailgdogra;

public class Shift {
    private final Officer officer;
    private final Location location;
    private final boolean isDayShift;

    public Shift(Officer officer, Location location, boolean isDayShift) {
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
