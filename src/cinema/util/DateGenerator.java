package cinema.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

public final class DateGenerator {

    public static LocalDateTime getDate() {
        Random random = new Random();
        return LocalDateTime.of(LocalDate.now().plusDays(random.nextInt(8) + 3),
                LocalTime.NOON.plusHours(random.nextInt(10)));
    }
}