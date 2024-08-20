package ir.ac.kntu.util;

import java.time.Instant;

public final class Calendar {
    public static final int TIME_SPEED = 6000;

    private static Instant start = Instant.now();

    private Calendar() {
    }

    public static Instant now() {
        return Instant.ofEpochMilli(start.toEpochMilli() + (Instant.now().toEpochMilli() - start.toEpochMilli()) * TIME_SPEED);
    }
}