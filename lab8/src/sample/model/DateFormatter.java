package sample.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatter {

    /**
     * String that handles pattern for data formatting
     */
    private static final String DATE_FORMAT_PATTERN = "dd.MM.yyyy";

    /**
     * Data formatter used in project to make data to easy format
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

    /**
     * Trays to get date from string argument in format defined by FORMATTER
     * @param date time in formation defined in FORMATTER
     * @return formatted time in defined in FORMATTER
     * @throws DateTimeParseException
     */
    public static LocalDate parseToDate(String date) throws DateTimeParseException {
        return LocalDate.parse(date,FORMATTER);
    }

    public static String parseToString(LocalDate date) {
        return FORMATTER.format(date);
    }

    public static String getDataFormat() {
        return DATE_FORMAT_PATTERN;
    }
}
