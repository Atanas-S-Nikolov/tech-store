package com.techstore.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.techstore.constants.DateTimeConstants.LOCAL_DATE_TIME_PRECISION_FORMAT;

public class DateTimeUtils {
    public static LocalDateTime parse(String date) {
        return parse(date, LOCAL_DATE_TIME_PRECISION_FORMAT);
    }

    public static LocalDateTime parse(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(date, formatter);
    }

    public static String millisToDateString(long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PRECISION_FORMAT));
    }
}
