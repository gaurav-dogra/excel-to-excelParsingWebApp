package gmailgdogra.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OutputRow implements Comparable<OutputRow> {

    private final String location;
    private final String firstName;
    private final String lastName;
    private final String eventDate;
    private final String logicalDevice;

    public static OutputRow of(@NotNull String location, @NotNull String firstName, @NotNull String lastName,
                               String eventDate, String deviceName) {
        return new OutputRow(location, firstName, lastName, eventDate, deviceName);
    }

    @Override
    public int compareTo(OutputRow o) {
        return getLocation().compareTo(o.getLocation());
    }
}
