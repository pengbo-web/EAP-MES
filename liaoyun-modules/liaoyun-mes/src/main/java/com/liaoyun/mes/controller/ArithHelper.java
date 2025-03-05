package com.liaoyun.mes.controller;

import java.math.BigDecimal;
import java.util.Date;

public class ArithHelper {
    /**
     * 相加
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {

        return v1.add(v2);
    }
    public static String calculateTimeDifference(Date startDate, Date endDate) {
        long diffInMilliSeconds = endDate.getTime() - startDate.getTime();

        // 计算天数
        long diffDays = diffInMilliSeconds / (24 * 60 * 60 * 1000);
        diffInMilliSeconds = diffInMilliSeconds % (24 * 60 * 60 * 1000);

        // 计算小时
        long diffHours = diffInMilliSeconds / (60 * 60 * 1000);
        diffInMilliSeconds = diffInMilliSeconds % (60 * 60 * 1000);

        // 计算分钟
        long diffMinutes = diffInMilliSeconds / (60 * 1000);

        return diffDays + " 天 " + diffHours + " 小时 " + diffMinutes + " 分钟";
    }

    /**
     * 相减
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        return v1.subtract(v2);
    }

    /**
     * 相乘
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        return v1.multiply(v2);
    }

    /**
     * 相除
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
        return v1.divide(v2, 4, BigDecimal.ROUND_HALF_UP);
    }
}
