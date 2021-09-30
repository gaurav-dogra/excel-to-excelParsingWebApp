package gmailgdogra.service;

import gmailgdogra.pojo.Officer;
import gmailgdogra.pojo.Swipe;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ExtractOfficersService {

    public static Set<Officer> from(List<Swipe> swipes) {
        return swipes.stream()
                .map(Swipe::getOfficer)
                .collect(Collectors.toSet());
    }
}
