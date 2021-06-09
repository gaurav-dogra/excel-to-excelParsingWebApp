package gmailgdogra;

public class Shift {
    private Officer officer;
    private Location location;
    private boolean isDayShift;

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
}
