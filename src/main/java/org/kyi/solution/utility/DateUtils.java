package org.kyi.solution.utility;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {
    public static LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public static Date toUtilDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    public static long monthBetween(LocalDate startDate, LocalDate endDate) {
        YearMonth yearMonth1 = YearMonth.from(startDate);
        YearMonth yearMonth2 = YearMonth.from(endDate);
        return ChronoUnit.MONTHS.between(yearMonth1, yearMonth2);
    }

}
