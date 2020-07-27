package com.precisource.util;

import com.google.common.collect.Lists;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: xinput
 * LocalDate 工具类
 */
public class LocalDateUtils {

    /**
     * String -> LocalDate
     */
    public static LocalDate asLocalDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * Date -> LocalDate
     */
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 计算两个日期相隔天数
     */
    public static int differentDays(LocalDate localDate1, LocalDate localDate2) {
        if (localDate1 == null || localDate2 == null) {
            throw new RuntimeException("日期不能为空");
        }

        /**
         * Period 用于计算时间间隔
         */
        return Period.between(localDate1, localDate2).getDays();
    }

    public static List<LocalDate> getMiddleLocalDate(LocalDate begin, LocalDate end) {
        if (end.isBefore(begin)) {
            return Lists.newArrayList();
        }
        List<LocalDate> localDateList = new ArrayList<>();
        long length = end.toEpochDay() - begin.toEpochDay();
        for (long i = length; i >= 0; i--) {
            localDateList.add(end.minusDays(i));
        }
        return localDateList;
    }

    public static boolean isAfter(LocalDate date1, LocalDate date2) {
        if (date1 == null) {
            return false;
        }

        if (date2 == null) {
            return true;
        }

        return date1.isAfter(date2);
    }

    public static boolean isEqual(LocalDate date1, LocalDate date2) {
        if (date1 == null && date2 == null) {
            return true;
        }

        if (date1 != null && date2 != null) {
            return date1.isEqual(date2);
        }

        return false;
    }

    public static Integer calculateAge(LocalDate birthday) {
        if (birthday == null) {
            return null;
        }

        return birthday.until(LocalDate.now()).getYears();
    }
}
