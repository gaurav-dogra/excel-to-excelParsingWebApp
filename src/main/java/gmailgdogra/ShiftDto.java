package gmailgdogra;

public class ShiftDto {

    private String fullName;
    private int shiftCode;

    public ShiftDto(String fullName, int shiftCode) {
        this.fullName = fullName;
        this.shiftCode = shiftCode;
    }

    public ShiftDto() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(int shiftCode) {
        this.shiftCode = shiftCode;
    }
}
