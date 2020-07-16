package util;

public class Validator {
    public static final int NAME_LENGTH = 90;
    public static final int EMAIL_LENGTH = 90;
    public static final int SHORT_LENGTH = 35;
    public static final int TINY_INT_LENGTH = 10;
    public static final int RFC_LENGTH = 15;
    public static final int LARGE_TEXT_LENGTH = 1500;
    public static final String LETTERS_AND_NUMBERS_PATTERN = "([A-Za-z0-9.-áéíóúüÁÉÍÓÚÜñÑ-]{1,}(\\ [A-Za-z0-9.-áéíóúüÁÉÍÓÚÜñÑ-]{1,})*)";
    public static final String EMAIL_PATTERN = "[A-Za-z0-9\\-ñÑ_]{1,}(\\.([A-Za-z0-9\\-ñÑ_]{1,}))*@(([a-zA-Z]{2,})(\\.([a-z]{1,})){1,})";
    public static final String NAME_PATTERN = "([A-Za-záéíóúüÁÉÍÓÚÜñÑ]{2,}(\\ [A-Za-záéíóúüÁÉÍÓÚÜñÑ]{2,})*)";
    public static final String LARGE_TEXT_PATTERN = "([^º]{1,}(\\ [^º]{1,})*)";
    public static final String NUMBER_PATTERN = "([0-9]{1,})";
    public static final String DATE_PATTERN = "(([0-9]){1,2}/([0-9]){1,2}/([0-9]){4})";
    public static final String TIME_PATTERN = "([0-9]{1,2})";
    public static final String FREE_PATTERN = "([^º]{2,})";

    /***
     * This method search for a match with a pattern
     * <p>
     * The purpose of this method it's try to match a text to a specified pattern
     * </p>
     * @param string
     * @param pattern
     * @return
     */
    public static boolean doesStringMatchPattern(String string, String pattern) {
        boolean stringMatchWithPattern = string.matches(pattern);
        return stringMatchWithPattern;
    }

    /***
     * This method check if a string is larger than a specified limit.
     * <p>
     * The purpose of tihs method it's try to match a text to a specified amount of characters
     * </p>
     * @param string
     * @param limit
     * @return
     */
    public static boolean isStringLargerThanLimitOrEmpty(String string, int limit) {
        boolean isStringEmptyOrLarger = string.length() > limit || string.equals("");
        return isStringEmptyOrLarger;
    }

}
