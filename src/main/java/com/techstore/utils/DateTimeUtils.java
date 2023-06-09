package com.techstore.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.techstore.constants.DateTimeConstants.LOCAL_DATE_TIME_PRECISION_FORMAT;

public class DateTimeUtils {
    public static LocalDateTime parse(String date) {
        return parse(date, LOCAL_DATE_TIME_PRECISION_FORMAT);
    }

    public static LocalDateTime parse(String date, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(date, formatter);
    }
}
