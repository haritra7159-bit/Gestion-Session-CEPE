package mg.cepe.gestion.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateUtil() {}
    public static String format(LocalDate date) {
        return date != null ? date.format(FORMATTER) : "";
    }
}
