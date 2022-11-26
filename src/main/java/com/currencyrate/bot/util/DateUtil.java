package com.currencyrate.bot.util;

import java.time.LocalDate;

public class DateUtil {

    private static final int EPOCH_DAY_DELIMITER = 86400;

    public static LocalDate getLocalDateFromSec(long seconds) {
        return LocalDate.ofEpochDay(seconds / EPOCH_DAY_DELIMITER);
    }
}
