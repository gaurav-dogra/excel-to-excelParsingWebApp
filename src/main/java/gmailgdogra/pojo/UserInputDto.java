package gmailgdogra.pojo;

import javax.validation.constraints.NotNull;

public class UserInputDto {

    private final String firstName;
    private final String lastName;
    private final int shiftCode;

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

    @Override
    public String toString() {
        return "UserInputDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", shiftCode=" + shiftCode +
                '}';
    }
}
