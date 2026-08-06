package mg.cepe.gestion.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CodeFormat {
    public static final String PREFIX_ECOLE = "ECO";
    public static final String PREFIX_ELEVE = "ELV";
    public static final String PREFIX_MATIERE = "MAT";
    public static final String REGEX_ECOLE = "^ECO-\\d{4}$";
    public static final String REGEX_ELEVE = "^ELV-\\d{4}$";
    public static final String REGEX_MATIERE = "^MAT-\\d{4}$";
    public static final String REGEX_ANNEE = "^\\d{4}-\\d{4}$";
    public static final String REGEX_NOM = "^[A-Za-z\\u00C0-\\u017F\\s\\-']{2,150}$";
    private static final Pattern SUFFIX = Pattern.compile("^[A-Z]{3}-(\\d{4})$");
    private CodeFormat() {}
    public static String nextCode(String prefix, Collection<String> existingIds) {
        int max = 0;
        String p = prefix == null ? "" : prefix.trim().toUpperCase();
        if (existingIds != null) {
            for (String id : existingIds) {
                if (id == null) continue;
                String u = id.trim().toUpperCase();
                Matcher m = SUFFIX.matcher(u);
                if (m.matches() && u.startsWith(p + "-")) {
                    max = Math.max(max, Integer.parseInt(m.group(1)));
                } else if (u.startsWith(p) && u.length() > p.length()) {
                    String tail = u.substring(p.length()).replaceAll("[^0-9]", "");
                    if (!tail.isEmpty()) {
                        try { max = Math.max(max, Integer.parseInt(tail)); } catch (NumberFormatException ignored) {}
                    }
                }
            }
        }
        return String.format("%s-%04d", p, max + 1);
    }
    public static boolean matches(String value, String regex) {
        return value != null && value.trim().matches(regex);
    }
}
