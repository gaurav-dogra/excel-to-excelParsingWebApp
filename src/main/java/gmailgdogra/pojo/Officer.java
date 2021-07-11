package gmailgdogra.pojo;

public class Officer {

    private final String firstName;
    private final String lastName;

    public Officer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Officer officer = (Officer) o;

        if (!getFirstName().equals(officer.getFirstName())) return false;
        return getLastName().equals(officer.getLastName());
    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}
