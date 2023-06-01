package com.techstore.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.techstore.constants.DateConstants.LOCAL_DATE_TIME_PRECISION_FORMAT;

public class DateTimeUtils {
    public static LocalDateTime parse(String date) {
        return parse(date, LOCAL_DATE_TIME_PRECISION_FORMAT);
    }

    public static LocalDateTime parse(String date, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String format1 = LocalDateTime.now().format(formatter);
        return LocalDateTime.parse(date, formatter);
    }
}
