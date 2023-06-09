package com.techstore.constants;

public interface DateTimeConstants {
    String LOCAL_DATE_TIME_PRECISION_FORMAT = "yyyy-MM-dd HH:mm:ss";

    String LOCAL_DATE_TIME_NOW_SpEL = "#{ T(java.time.LocalDateTime).now().format( T(java.time.format.DateTimeFormatter)" +
            ".ofPattern('" + LOCAL_DATE_TIME_PRECISION_FORMAT + "') ) }";

    String LOCAL_DATE_TIME_EPOCH = "1970-01-01 00:00:00";

    long ONE_MINUTE = 1000L * 60L;
    long FIVE_MINUTES = ONE_MINUTE * 5L;
    long ONE_HOUR = ONE_MINUTE *  60L;
    long ONE_DAY = ONE_HOUR * 24L;
    long FOUR_WEEKS = ONE_DAY * 28L;
}
