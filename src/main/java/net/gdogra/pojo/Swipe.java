package net.gdogra.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Swipe implements Comparable<Swipe> {
    private final Officer officer;
    private final LocalDateTime swipeDateTime;
    private final String deviceName;

    @Override
    public int compareTo(Swipe o) {
        return this.swipeDateTime.compareTo(o.swipeDateTime);
    }
}
