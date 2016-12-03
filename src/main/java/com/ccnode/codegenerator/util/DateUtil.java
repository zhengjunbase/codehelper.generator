package com.ccnode.codegenerator.util;

import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtil {


	private static final int INSTANCE_COUNT = 20;

	public  static final SimpleDateFormat[] formatYYYYMMDD = createDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat[] formatLong = createDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final SimpleDateFormat[] insureFormat = createDateFormat("yyyy-MM-dd|HH:mm:ss");
	private static final SimpleDateFormat[] formatShort = createDateFormat("yyyyMMdd");
	private static final SimpleDateFormat[] formatTime = createDateFormat("HH:mm:ss");
	private static final SimpleDateFormat[] formatYYYYMMDDSSS = createDateFormat("yyMMddHHmmssSSS");
	private static final SimpleDateFormat[] formatYYYYMMDDHHMMSS = createDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat[] formatYYYYMMDDHHMMSSS = createDateFormat("yyyyMMddHHmmsss");

	private static final SimpleDateFormat[] formatMMDD = createDateFormat("MMdd");
	private static final SimpleDateFormat[] formatYYYYMMDDHHMM = createDateFormat("yyyy-MM-dd HH:mm");

	private static final String parseYYYYMMDD = "yyyy-MM-dd HH:mm:ss";

	private static int currentIndex = 0; // 不需要考虑多线程问题，节省性能开销。


    private DateUtil() {
    }


    private static SimpleDateFormat[] createDateFormat(String fmt) {
    	SimpleDateFormat[] ret = new SimpleDateFormat[INSTANCE_COUNT];
    	for (int i = 0; i < ret.length; i++) {
    		ret[i] = new SimpleDateFormat(fmt);
    		ret[i].setLenient(false);
    	}
		return ret;
	}


	private final static int getIndex() {
    	int n = currentIndex++;
    	if (n < 0) { // 处理溢出
    		currentIndex = 0;
    		n = 0;
    	}
    	return n % INSTANCE_COUNT;
    }

    public static String formatYYYYMMDDHHMM() {
    	SimpleDateFormat fmt = formatYYYYMMDDHHMM[getIndex()];
    	synchronized(fmt){
    		return fmt.format(new Date());
    	}
    }

    public static Date paseYYYYMMDDHHMM(String strDate) {
		if (strDate == null || strDate.indexOf("null") >= 0)
			return null;

		Date date = null;
		try {
			SimpleDateFormat fmt = formatYYYYMMDDHHMM[getIndex()];
			synchronized(fmt){
				date = fmt.parse(strDate);
			}
		} catch (Exception e) {
			return null;
		}

		return date;
    }

	public static String formatMMDD() {
		SimpleDateFormat fmt = formatMMDD[getIndex()];
		synchronized(fmt){
			return fmt.format(new Date());
		}
	}

	public static String formatCurTime() {
		SimpleDateFormat fmt = formatYYYYMMDDSSS[getIndex()];
		synchronized(fmt){
			return fmt.format(new Date());
		}
	}
	public static String formatCurTimeLong() {
		SimpleDateFormat fmt = formatYYYYMMDDHHMMSS[getIndex()];
		synchronized(fmt){
			return fmt.format(new Date());
		}
	}
	public static String formatYYYYMMDDHHMMSS(Date date) {
		SimpleDateFormat fmt = formatYYYYMMDDHHMMSS[getIndex()];
		synchronized(fmt){
			return fmt.format(date);
		}
	}

	public static String formatYYYYMMDDHHMMSSS(Date date) {
		SimpleDateFormat fmt = formatYYYYMMDDHHMMSSS[getIndex()];
		synchronized(fmt){
			return fmt.format(date);
		}
	}

	public static Date convertYYYYMMDDHHMMSS(String strDate) {
		if (strDate == null || strDate.indexOf("null") >= 0)
			return null;

		Date date = null;
		try {
			SimpleDateFormat fmt = formatYYYYMMDDHHMMSS[getIndex()];
			synchronized(fmt){
				date = fmt.parse(strDate);
			}
		} catch (Exception e) {
			return null;
		}

		return date;
	}

	public static Date convertYYYYMMDD(String strDate) {
		if (strDate == null || strDate.indexOf("null") >= 0)
			return null;

		Date date = null;
		try {
			SimpleDateFormat fmt = formatYYYYMMDD[getIndex()];
			synchronized(fmt){
				date = fmt.parse(strDate);
			}
		} catch (Exception e) {
			return null;
		}

		return date;
	}

	public static Date convertShort(String strDate) {
		if (strDate == null || strDate.indexOf("null") >= 0)
			return null;

		Date date = null;
		try {
			SimpleDateFormat fmt = formatShort[getIndex()];
			synchronized (fmt) {
				date = fmt.parse(strDate);
			}
		} catch (Exception e) {
			return null;
		}

		return date;
	}

	public static Date convertLong(String strDate) {
		if (strDate == null || strDate.indexOf("null") >= 0)
			return null;

		Date date = null;
		try {
			SimpleDateFormat fmt = formatLong[getIndex()];
			synchronized(fmt){
				date = fmt.parse(strDate);
			}
		} catch (Exception e) {
			return null;
		}

		return date;
	}

	public static String formatYYYYMMDD(Date date) {
		SimpleDateFormat fmt = formatYYYYMMDD[getIndex()];
		synchronized(fmt){
			return fmt.format(date);
		}
	}

	public static String insureFormat(Date date) {
		SimpleDateFormat fmt = insureFormat[getIndex()];
		synchronized(fmt){
			return fmt.format(date).replaceAll("\\|", "T");
		}
	}


	public static String formatTime(Date date) {
		SimpleDateFormat fmt = formatTime[getIndex()];
		synchronized (fmt) {
			return fmt.format(date);
		}
	}


	public static String formatShort(Date date) {
		SimpleDateFormat fmt = formatShort[getIndex()];
		synchronized (fmt) {
			return fmt.format(date);
		}
	}

	public static String formatLong(Date date) {
		SimpleDateFormat fmt = formatLong[getIndex()];
		synchronized(fmt){
			return fmt.format(date);
		}
	}

	public static String formatDDMMMYY(Date date) {
		return String.format(Locale.US, "%1$td%1$tb%1$ty", date);
	}

	public static String convertYYYYMMDDToDDMMMYY(String date) {
		Date d = convertYYYYMMDD(date);
		return null == d ? "" : formatDDMMMYY(d);
	}

    public static String formatTodyDate(String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static Date parse(String date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			return null;
		}
    }

	/**
	 * 取得两个日期的时间间隔,相差的天数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getDayBetween(Date d1, Date d2) {
		Calendar before = Calendar.getInstance();
		Calendar after = Calendar.getInstance();
		if (d1.before(d2)) {
			before.setTime(d1);
			after.setTime(d2);
		} else {
			before.setTime(d2);
			after.setTime(d1);
		}
		int days = 0;

		int startDay = before.get(Calendar.DAY_OF_YEAR);
		int endDay = after.get(Calendar.DAY_OF_YEAR);

		int startYear = before.get(Calendar.YEAR);
		int endYear = after.get(Calendar.YEAR);
		before.clear();
		before.set(startYear, 0, 1);

		while (startYear != endYear) {
			before.set(startYear++, Calendar.DECEMBER, 31);
			days += before.get(Calendar.DAY_OF_YEAR);
		}
		return days + endDay - startDay;
	}

	/**
	 * 取得两个日期的时间间隔,相差的天数,后面减前面，可能为负
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getDayBetweenD(Date d1, Date d2) {
        if (d1.before(d2)) {
            return getDayBetween(d1, d2);
        } else {
            return -getDayBetween(d1, d2);
        }
	}
	/**
	 * 取得时间间隔,相差的时间，XX小时XX分钟
	 * @return 不会超过24小时
	 */
	public static String getTimeBetween(String time1,String time2) {
		String[] t1 = time1.split(":");
		String[] t2 = time2.split(":");

		int minute = Integer.parseInt(t2[1])-Integer.parseInt(t1[1]);
		int hour = Integer.parseInt(t2[0])-Integer.parseInt(t1[0]);

		if(minute<0){
			minute += 60;
			hour -= 1;
		}
		if(hour<0){
			hour += 24;
		}

		if(hour == 0){
			return minute + "分钟";
		}
		if(minute == 0){
			return hour + "小时";
		}
		return hour + "小时" + minute + "分钟";
	}

    public static Date addDay(Date myDate, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.DAY_OF_MONTH, amount);
        return cal.getTime();
    }

    public static Date addMinute(Date myDate, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.MINUTE, amount);
        return cal.getTime();
    }
    public static Date addYear(Date myDate, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.YEAR, amount);
        return cal.getTime();
    }
    public static String dateFormatStr(String dateByyyyyMMddStr)
    {
    	if(dateByyyyyMMddStr != null && dateByyyyyMMddStr.length() == 8)
    	{
    		String year = dateByyyyyMMddStr.substring(0, 4);
    		String month = dateByyyyyMMddStr.substring(4, 6);
    		String day = dateByyyyyMMddStr.substring(6, 8);
    		return year + "-" + month + "-" + day;
    	}else{
    		return "";
    	}
    }

    public static String long2DateStr(long msec, String pattern)
    {
    	Date date = new Date();
		date.setTime(msec);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
    }

    public static boolean beforeCurrentTime(String param){
       Calendar nowcal = Calendar.getInstance();
 	   Calendar paramcal = Calendar.getInstance();
 	   nowcal.setTime(new Date());
 	   paramcal.setTime(parse(param,parseYYYYMMDD));
 	   return nowcal.after(paramcal);
    }

	public static Date getBeforeNowDays(int days){
		return DateUtils.add(new Date(), Calendar.DAY_OF_YEAR, days);
	}

	public static boolean isAddDay(Date date1,Date date2) {
		if(date1 == null || date2 == null)
			return false;
		String time1 = formatTime(date1);
		String time2 = formatTime(date2);

        try{
            if(time1.compareToIgnoreCase(time2)>= 0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
	}

	public static Date addHour(Date myDate, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDate);
		cal.add(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}
	public static Date getAfterNowDaysWithoutHHmmss(int days) {
		return DateUtil.addDay(getNowWithoutHHmmss(), days);
	}
	public static Date getBeforeNowDaysWithoutHHmmss(int days) {
		return DateUtil.addDay(getNowWithoutHHmmss(), -days);
	}


	public static Date getDateWithoutHHmmss(Date date) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getNowWithoutHHmmss() {
		return getDateWithoutHHmmss(new Date());
	}
}