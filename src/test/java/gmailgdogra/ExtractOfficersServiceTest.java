package gmailgdogra;

import gmailgdogra.pojo.Officer;
import gmailgdogra.pojo.SwipeRecord;
import gmailgdogra.service.ExtractOfficersService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtractOfficersServiceTest {

    private final Officer officerOne = new Officer("Officer", "One");
    private final Officer officerTwo = new Officer("Officer", "Two");

    private final Set<Officer> expected = new HashSet<>(Arrays.asList(
            officerTwo,
            officerOne
            ));

    private final List<SwipeRecord> inputList = Arrays.asList(
            new SwipeRecord(officerOne, null, null),
            new SwipeRecord(officerOne, null, null),
            new SwipeRecord(officerTwo, null, null)
    );

    @Test
    public void test_distinctReturn() {
        assertEquals(expected, ExtractOfficersService.from(inputList));
    }

}
