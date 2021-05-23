package gmailgdogra;

public class Officer {

    private String firstName;
    private String lastName;
    private Location location;
    private boolean isDayShift;

    public Officer(String firstName, String lastName, Location location, boolean isDayShift) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.isDayShift = isDayShift;
    }

    public Officer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Officer officer = (Officer) o;

        if (!getFirstName().equals(officer.getFirstName())) return false;
        if (!getLastName().equals(officer.getLastName())) return false;
        return getLocation() == officer.getLocation();
    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return firstName + " " +
                lastName + ":" +
                location;
    }

    public boolean isDayShift() {
        return isDayShift;
    }

    public void setDayShift(boolean dayShift) {
        isDayShift = dayShift;
    }
}
