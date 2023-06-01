package com.techstore.constants;

public interface DateConstants {
    String LOCAL_DATE_TIME_PRECISION_FORMAT = "yyyy-MM-dd HH:mm:ss";

    String LOCAL_DATE_TIME_NOW_SpEL = "#{ T(java.time.LocalDateTime).now().format( T(java.time.format.DateTimeFormatter)" +
            ".ofPattern('" + LOCAL_DATE_TIME_PRECISION_FORMAT + "') ) }";

    String LOCAL_DATE_TIME_EPOCH = "1970-01-01 00:00:00";
}
