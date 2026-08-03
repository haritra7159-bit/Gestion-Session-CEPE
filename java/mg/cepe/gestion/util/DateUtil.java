package mg.cepe.gestion.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    private static final DateTimeFormatter F=DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateUtil(){}
    public static String format(LocalDate d){return d!=null?d.format(F):"";}
}
