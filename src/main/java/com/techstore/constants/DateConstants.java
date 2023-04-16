package com.techstore.constants;

public interface DateConstants {
    String LOCAL_DATE_TIME_NOW_SpEL = "#{T(java.time.LocalDateTime).now()}";

    // 1970-01-01 00:00:00
    String LOCAL_DATE_TIME_EPOCH_SpEL = "#T{java.time.LocalDateTime.of(java.time.LocalDate.EPOCH, java.time.LocalTime.MIDNIGHT)}";

    String LOCAL_DATE_TIME_PRECISION_FORMAT = "yyyy-MM-dd-HH-mm-ss-ns";
}
