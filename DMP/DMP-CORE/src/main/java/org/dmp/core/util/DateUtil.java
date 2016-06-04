package org.dmp.core.util;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/***
 * 
 * @ClassName: DateUtil
 * @Description: 时间操作类
 * @author zhoushubin@unioncast.cn
 * @date 2013-3-4 上午9:25:51
 * 
 */
public final class DateUtil {
	private static final Logger logger = Logger.getLogger(DateUtil.class);
	public static final int YEAR = 0;
	public static final int MONTH = 1;
	public static final int DAY = 2;
	public static final int HOURS = 3;
	public static final int MINUTES = 4;
	public static final int SECONDS = 5;
	public static final int MS = 6;
	public static final int WEEK = 7;
	public static final int APM = 8;

	/**
	 * 获取日期中的时间 12小时制度
	 * 
	 * @param type
	 *            Date中的枚举类型
	 * @param date
	 * @return String
	 * @throws UtilException
	 */
	public static String getYMDformDate12(int type, Date date) {
		if (type < 0)
			logger.error(new JsonBuilder().appendException(String.format(
					" args type   %s  can not  is negative.", type)));
		if (date == null)
			logger.error(new JsonBuilder().appendException(String.format(
					"args date  %s can not is nul.", date)));

		return dateFormat("yyyy:MM:dd:hh:mm:ss:SSS:E:a", date).split(":")[type];
	}

	/**
	 * 获取日期中的时间 24小时制度
	 * 
	 * @param type
	 *            中的枚举类型
	 * @param date
	 * @return String
	 * @throws UtilException
	 */
	public static String getYMDformDate24(int type, Date date) {
		if (type < 0)
			logger.error(new JsonBuilder().appendException(String.format(
					" args type   %s  can not  is negative.", type)));
		if (date == null)
			logger.error(new JsonBuilder().appendException(String.format(
					"args date  %s can not is nul.", date)));

		return dateFormat("yyyy:MM:dd:HH:mm:ss:SSS:E:a", date).split(":")[type];
	}

	/***
	 * 
	 * @Title: dateFormat
	 * @Description: 格式化输出时间
	 * @param @param format
	 * @param @param date
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String dateFormat(String format, Date date) {
		if (date == null) {
			logger.error(new JsonBuilder().appendException(String.format(
					" args date   %s  can not  is null.", date)));
		}
		if (format == null || format.trim().length() == 0) {
			logger.error(new JsonBuilder().appendException(String.format(
					" args format   %s  can not  is null.", format)));
		}
		return new SimpleDateFormat(format).format(date);
	}

	private static Date gotoDate(boolean type, int dateType, Date indexDate,
			int index) {

		if (indexDate == null)
			logger.error(new JsonBuilder().appendException(String.format(
					"args indexDate :  %s   can net be null.", indexDate)));
		if (dateType < YEAR || dateType > WEEK)
			logger.error(new JsonBuilder().appendException(String.format(
					"args dateType :  %s  is outOfTimeType.", dateType)));
		BigInteger bi = null;
		switch (dateType) {
		case YEAR:
			// 这个还有更好的算法，，以后再说
			String[] datestr = dateFormat("yyyy:MM:dd:HH:mm:ss:SSS", indexDate)
					.split(":");
			int year = Integer.parseInt(datestr[YEAR]);
			if (type) {
				year = year - index;
			} else {
				year = year + index;
			}
			datestr[YEAR] = year + "";
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < datestr.length; i++) {
				sb.append(datestr[i]);
			}
			return StringToDate("yyyyMMddHHmmssSSS", sb.toString());
		case MONTH:
			// 哎 时间太少，，有BUG 不要怪我 吃饭先 ==在来 OK了
			String[] datestr2 = dateFormat("yyyy:MM:dd:HH:mm:ss:SSS", indexDate)
					.split(":");
			int menths = Integer.parseInt(datestr2[YEAR]) * 12
					+ Integer.parseInt(datestr2[MONTH]);
			if (type) {
				// 哎 时间太少，，有BUG 但问题不大 吃饭先 ==在来 OK了
				datestr2 = dateFormat("yyyy:MM:dd:HH:mm:ss:SSS", indexDate)
						.split(":");
				menths = Integer.parseInt(datestr2[YEAR]) * 12
						+ Integer.parseInt(datestr2[MONTH]);
				if (type) {
					menths = menths - index;
				} else {
					menths = menths + index;
				}
				int men = menths % 12;
				if (men == 0) {
					datestr2[YEAR] = menths / 12 - 1 + "";
					datestr2[MONTH] = 12 + "";
				} else {
					datestr2[YEAR] = menths / 12 + "";
					if (men < 10) {
						datestr2[MONTH] = "0" + men;
					} else {
						datestr2[MONTH] = "" + men;
					}
				}
				StringBuffer sb2 = new StringBuffer();
				for (int i = 0; i < datestr2.length; i++) {
					sb2.append(datestr2[i]);
				}
				return StringToDate("yyyyMMddHHmmssSSS", sb2.toString());
			}
		case DAY:
			// 计算天数多少毫秒
			bi = new BigInteger(24 * 60 * 60 * 1000 + "");
			break;
		case HOURS:
			bi = new BigInteger(60 * 60 * 1000 + "");
			break;
		case MINUTES:
			bi = new BigInteger(60 * 1000 + "");
			break;
		case SECONDS:
			bi = new BigInteger(1000 + "");
			break;
		case MS:
			bi = new BigInteger(1 + "");
			break;
		case WEEK:
			bi = new BigInteger(24 * 60 * 60 * 1000 * 7 + "");
			break;
		}
		if (bi != null) {
			bi = bi.multiply(new BigInteger(index + ""));
			BigInteger bi2 = new BigInteger(indexDate.getTime() + "");
			if (type) {
				bi2 = bi2.add(bi.negate());
			} else {
				bi2 = bi2.add(bi);
			}
			return new Date(bi2.longValue());
		}
		return null;
	}

	/***
	 * 
	 * @Title: StringToDate
	 * @Description: 按照指定格式转换字符串成时间对象
	 * @param @param format
	 * @param @param dateStr
	 * @param @return 设定文件
	 * @return Date 返回类型
	 * @throws
	 */
	public static Date StringToDate(String format, String dateStr) {
		if (dateStr == null || dateStr.trim().length() == 0) {
			logger.error(new JsonBuilder().appendException(String
					.format("  args error,dateStr :%sis null or dateStr's length is 0.",
							dateStr)));
		}
		if (format == null || format.trim().length() == 0) {
			logger.error(new JsonBuilder().appendException(String.format(
					"  args error,format :%sis null or dateStr's length is 0.",
					format)));
		}
		try {
			return new SimpleDateFormat(format).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error(new JsonBuilder().appendException(String.format(
					"%s, %s ParseException ...", format, dateStr)), e);
		}
		return null;
	}

	/***
	 * 
	 * @Title: previous
	 * @Description: 上几（年，月，日，时，分，秒,星期），求当前时间的前几（年，月，日，时，分，秒,星期）的时间
	 * @param @param dateType 时间类型 类型可以可以是：年，月，日，时，分，秒,星期
	 * @param @param indexDate 基准时间 /当前时间
	 * @param @param index 几
	 * @param @return 设定文件
	 * @return Date 返回类型
	 * @throws
	 */
	public static Date previous(int dateType, Date indexDate, int index) {
		return gotoDate(true, dateType, indexDate, index);
	}

	/**
	 * 比较时间差 求商 取出余数
	 * 
	 * @param type
	 *            时间差返回类型，支持（年 --365天算，月--30天一个月，日 ，星期，时，分，秒，毫秒）
	 * @param startDate
	 * @param endDate
	 * @return 差几天/年/等
	 */
	public static long timeLag(int type, Date startDate, Date endDate) {
		return timeLagImp(
				type,
				new BigInteger(endDate.getTime() + "").add(
						new BigInteger(startDate.getTime() + "").negate())
						.longValue(), false);
	}

	/***
	 * 
	 * @Title: timeLagReturnRemnant
	 * @Description: 比较时间差 求余数
	 * @param @param type --表示你想返回的余数的类型，求2个日期的时间差，单位是可以是： 时间差返回类型，支持（年
	 *        --365天算，月--30天一个月，日 ，星期，时，分，秒，毫秒）
	 * @param @param startDate
	 * @param @param endDate
	 * @param @return 设定文件
	 * @return long 返回类型
	 * @throws
	 */
	public static long timeLagReturnRemnant(int type, Date startDate,
			Date endDate) {
		return timeLagImp(
				type,
				new BigInteger(endDate.getTime() + "").add(
						new BigInteger(startDate.getTime() + "").negate())
						.longValue(), true);
	}

	private static long timeLagImp(int type, long time, boolean isRemnant) {
		BigInteger bi = null;
		switch (type) {
		case YEAR:
			bi = new BigInteger(365 * 24 * 60 * 60 * 1000 + "");
			break;
		case MONTH:
			bi = new BigInteger(30 * 24 * 60 * 60 * 1000 + "");
			break;
		case DAY:
			bi = new BigInteger(24 * 60 * 60 * 1000 + "");
			break;
		case HOURS:
			bi = new BigInteger(60 * 60 * 1000 + "");
			break;
		case MINUTES:
			bi = new BigInteger(60 * 1000 + "");
			break;
		case SECONDS:
			bi = new BigInteger(1000 + "");
			break;
		case MS:
			bi = new BigInteger(1 + "");
			break;
		case WEEK:
			bi = new BigInteger(24 * 60 * 60 * 1000 * 7 + "");
			break;
		}
		if (bi != null) {
			if (isRemnant) {
				// 求余数
				return new BigInteger(time + "").remainder(bi).longValue();
			} else {
				// 求商
				return new BigInteger(time + "").divide(bi).longValue();
			}
		}
		return 0L;
	}

	/***
	 * 
	 * @Title: isLeapYeay
	 * @Description: 判断闰年
	 * @param @param date
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isLeapYeay(Date date) {
		return isLeapYeay(getYMDformDate24(YEAR, date));
	}

	/***
	 * 
	 * @Title: isLeapYeay
	 * @Description: 判断闰年
	 * @param @param yearstr
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isLeapYeay(String yearstr) {
		return isLeapYeay(Integer.parseInt(yearstr));
	}

	/***
	 * 
	 * @Title: isLeapYeay
	 * @Description: 判断闰年
	 * @param @param year
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isLeapYeay(int year) {
		boolean falg = Boolean.FALSE;
		if (year % 4 != 0) {
			falg = Boolean.FALSE;
		} else if (year % 100 != 0) {
			falg = Boolean.TRUE;
		} else if (year % 400 != 0) {
			falg = Boolean.FALSE;
		} else {
			falg = Boolean.TRUE;
		}
		return falg;
	}

	/***
	 * 
	 * @Title: getDayWithMonth
	 * @Description: 判断该月的天数
	 * @param @param format
	 * @param @param datestr
	 * @param @return
	 * @param @throws UtilException 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int getDayWithMonth(String format, String datestr) {
		Date date = StringToDate(format, datestr);
		return isBigOrSmailDay(Integer.parseInt(getYMDformDate24(MONTH, date)),
				Integer.parseInt(getYMDformDate24(YEAR, date)));
	}

	/***
	 * 
	 * @Title: getDayWithMonth
	 * @Description: 判断该月的天数
	 * @param @param date
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int getDayWithMonth(Date date) {

		return isBigOrSmailDay(Integer.parseInt(getYMDformDate24(MONTH, date)),
				Integer.parseInt(getYMDformDate24(YEAR, date)));
	}

	/***
	 * 
	 * @Title: isBigOrSmailDay
	 * @Description: 判断月份的天数
	 * @param @param month
	 * @param @param year
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int isBigOrSmailDay(int month, int year) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			return 31;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		} else {
			if (isLeapYeay(year)) {
				return 29;
			} else {
				return 28;
			}
		}
	}

	// /****
	// *
	// * @Title: getDate
	// * @Description: 格式化获取时间
	// * @param @param oDate
	// * @param @param sDateFormat
	// * @param @return 设定文件
	// * @return String 返回类型
	// * @throws
	// */
	// public final static String getDate(Date oDate, String sDateFormat)
	// {
	// if (oDate == null) return "";
	// SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat(sDateFormat);
	//
	// return oSimpleDateFormat.format(oDate).toString();
	// }

	/****
	 * 
	 * @Title: getDate
	 * @Description: 格式化获取系统当前时间
	 * @param @param sDateFormat
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String getDate(String sDateFormat) {
		return dateFormat(sDateFormat, new Date());
	}

	/****
	 * 
	 * @Title: getDate
	 * @Description: 格式化当前时间
	 * @param @param sDate
	 * @param @param sDateFormat
	 * @param @return 设定文件
	 * @return Date 返回类型
	 * @throws
	 */
	public final static Date getDate(String sDate, String sDateFormat) {
		return getDateFromString(sDate, sDateFormat);
	}

	/****
	 * 
	 * @Title: getNow
	 * @Description: 获取当前系统时间yyyy-MM-dd HH:mm:ss
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String getNow() {
		return getDate("yyyy-MM-dd HH:mm:ss");
	}

	/****
	 * 
	 * @Title: getDate
	 * @Description: 获取当前系统时间yyyy-MM-dd
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/***
	 * 
	 * @Title: getTime
	 * @Description:获取当前系统时间HH:mm:ss
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String getTime() {
		return getDate("HH:mm:ss");
	}

	/***
	 * 
	 * @Title: getDateFromString
	 * @Description: 字符串转换成日期
	 * @param @param sDate
	 * @param @param sFormat
	 * @param @return 设定文件
	 * @return Date 返回类型
	 * @throws
	 */
	public final static Date getDateFromString(String sDate, String sFormat) {
		try {
			SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat(sFormat);
			return oSimpleDateFormat.parse(sDate);
		} catch (Exception oException) {
			logger.error(oException.getMessage(), oException.getCause());
		}
		return null;
	}

	/***
	 * 
	 * @Title: getDateDiff
	 * @Description: 计算两个日期差
	 * @param @param sDateEnd
	 * @param @param sDateStart
	 * @param @return 设定文件
	 * @return long 返回类型 秒
	 * @throws
	 */
	public final static long getDateDiff(String sDateEnd, String sDateStart) {
		long nDateDiff = -1;
		SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		try {
			Date dDateEnd = oSimpleDateFormat.parse(sDateEnd);
			Date dDateStart = oSimpleDateFormat.parse(sDateStart);
			nDateDiff = (dDateEnd.getTime() - dDateStart.getTime()) / 1000;
		} catch (Exception oException) {
			logger.error(oException.getMessage(), oException.getCause());
		}

		return nDateDiff;
	}

	/****
	 * 
	 * @Title: dateAdd
	 * @Description: 日期增加或者减少
	 * @param @param sDatepart [年：year，月：month，日：day，时：hour，分：minute，秒：second]
	 * @param @param nExpression
	 * @param @param sDate
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String dateAdd(String sDatepart, int nExpression,
			String sDate) {
		String sFormat = "yyyy-MM-dd HH:mm:ss";
		sDatepart = sDatepart.toLowerCase();
		Calendar oCalendar = Calendar.getInstance();
		oCalendar.setTime(getDateFromString(sDate, sFormat));
		Map<String, Integer> aMap = new HashMap<String, Integer>() {
			private static final long serialVersionUID = -763618247875050322L;
			{
				put("year", new Integer(Calendar.YEAR));
				put("month", new Integer(Calendar.MONTH));
				put("day", new Integer(Calendar.DATE));
				put("hour", new Integer(Calendar.HOUR));
				put("minute", new Integer(Calendar.MINUTE));
				put("second", new Integer(Calendar.SECOND));
			}
		};
		oCalendar.add(aMap.get(sDatepart), nExpression);

		return dateFormat(sFormat, oCalendar.getTime());
	}

	/****
	 * 
	 * @Title: thisMonth
	 * @Description: 得到当前月份月初 格式为：xxxx-yy-zz
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String thisMonth() {
		int x; // 日期属性：年
		int y; // 日期属性：月
		Calendar localTime = Calendar.getInstance(); // 当前日期

		String strY = null;
		x = localTime.get(Calendar.YEAR);
		y = localTime.get(Calendar.MONTH) + 0;
		strY = y >= 10 ? String.valueOf(y) : ("0" + y);
		return x + "-" + strY + "-01 00:00:00";
	}

}
