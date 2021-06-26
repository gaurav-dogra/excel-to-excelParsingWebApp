package gmailgdogra;

import javax.validation.constraints.NotNull;

public class UserInputDto {

    private String firstName;
    private String lastName;
    private int shiftCode;

    public UserInputDto() {
    }

    public UserInputDto(@NotNull String firstName, @NotNull String lastName, @NotNull int shiftCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.shiftCode = shiftCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getShiftCode() {
        return shiftCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setShiftCode(int shiftCode) {
        this.shiftCode = shiftCode;
    }

    @Override
    public String toString() {
        return "UserInputDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", shiftCode=" + shiftCode +
                '}';
    }
}
