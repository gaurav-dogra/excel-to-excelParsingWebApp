package net.gdogra.service;

import net.gdogra.pojo.Officer;
import net.gdogra.pojo.Swipe;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ExtractOfficersService {

    private ExtractOfficersService() {
    }

    public static Set<Officer> from(List<Swipe> swipes) {
        return swipes.stream()
                .map(Swipe::getOfficer)
                .collect(Collectors.toSet());
    }
}
