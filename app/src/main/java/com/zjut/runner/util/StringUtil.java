package com.zjut.runner.util;

import android.content.Context;

import com.zjut.runner.Model.GenderType;
import com.zjut.runner.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Phuylai on 2016/10/4.
 */
public class StringUtil {
    /**
     * check if the string is null
     * @param str
     *
     */

    public static boolean isNull(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }

        return str.equalsIgnoreCase("null");

    }

    /**
     * check if the string is not null
     * @param str
     */

    public static boolean notNull(String str) {
        return !isNull(str);
    }


    /**
     * convert int to string
     * @param value
     */
    public static String convertIntegerToString(Integer value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
     * convert number to string
     * @param value
     */
    public static String convertNumberToString(Number value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
     * convert string to date
     * @param value
     */
    public static Date getDate(String value){
        if(value == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date date = simpleDateFormat.parse(value);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateHourMinute(String value){
        if(value == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
        try {
            return simpleDateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getHourMinute(String time){
        if(time == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm",Locale.CHINA);
        try {
            return simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateTime(String value){
        if(value == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date date = simpleDateFormat.parse(value);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String today = simpleDateFormat.format(new Date());
        return today;
    }

    public static Date getDateWithTime() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return simpleDateFormat.parse(simpleDateFormat.format(date));
    }

    public static String getDateTimeString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public static String twoDigit(int minute) {
        if(minute < 10){
            StringBuilder sb =  new StringBuilder();
            sb.append(0);
            sb.append(minute);
            return sb.toString();
        }
        return String.valueOf(minute);
    }

    /*
    convert from Date to String
    * */
    public static String dateToString(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(date);
    }

    public static String dateTimeToString(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public static String dateString(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     *get current time string
     */
    public static String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return year + "/" + (month + 1) + "/" + day + "  " +
                twoDigit(hour) + ":" + twoDigit(minute);
    }

    /**
     *format currency string
     */
    public static String currencyFormat(double unitPrice){
        DecimalFormat format = new DecimalFormat("#,###,###.##");
        return format.format(unitPrice);
    }

    /*Gender to String*/
    public static String genderString(Context context,GenderType genderType) {
        String gender = "";
        if(genderType != null){
            switch (genderType){
                case FEMALE:
                    gender = context.getString(R.string.str_female);
                    break;
                case MALE:
                    gender = context.getString(R.string.str_male);
                    break;
            }
        }
        return gender;
    }
}
