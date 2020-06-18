package com.precisource.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    public static void main(String[] args) {
    }
}
