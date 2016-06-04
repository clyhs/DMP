package org.dmp.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/****
 * 
* @ClassName: StrUtil 
* @Description: 字符串操作类
* @author zhoushubin@unioncast.cn
* @date 2013-3-4 上午9:27:10 
*
 */
public final class StrUtil
{
	/** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块 */
	private static final String US_ASCII = "US-ASCII";

	/** ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1 */
	private static final String ISO_8859_1 = "ISO-8859-1";

	/** 8 位 UCS 转换格式 */
	private static final String UTF_8 = "UTF-8";

	/** 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序 */
	private static final String UTF_16BE = "UTF-16BE";

	/** 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序 */
	private static final String UTF_16LE = "UTF-16LE";

	/** 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识 */
	private static final String UTF_16 = "UTF-16";

	/** 中文超大字符集 */
	private static final String GBK = "GBK";


/***
 * 
* @Title: toGBK 
* @Description: 将字符编码转换成GBK码
* @param @param sStr
* @param @return
* @param @throws UnsupportedEncodingException    设定文件 
* @return String    返回类型 
* @throws
 */
	public final static String toGBK(String sStr) throws UnsupportedEncodingException
	{
		 return changeCharset(sStr, GBK);
	}

	/**
	 * 将字符编码转换成UTF-8码
	 * @param sStr
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public final static String toUTF8(String sStr) throws UnsupportedEncodingException
	{
		return changeCharset(sStr, UTF_8);
	}

	/**
	 * 将字符编码转换成US-ASCII码
	 * @param sStr
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public final static String toASCII(String sStr) throws UnsupportedEncodingException
	{
		return changeCharset(sStr, US_ASCII);
	}

	/**
	 * 将字符编码转换成UTF-16码
	 * @param sStr
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public final static String toUTF16(String sStr) throws UnsupportedEncodingException
	{
		return changeCharset(sStr, UTF_16);
	}

	/**
	 * 字符串编码转换的实现方法
	 * @param sStr 待转换编码的字符串
	 * @param sNewCharset 目标编码
	 * @return String
	 * @throws UnsupportedEncodingException
	*/
	public final static String changeCharset(String sStr, String sNewCharset)
		throws UnsupportedEncodingException
	{
		//用默认字符编码解码字符串。
		byte[] aBits = sStr.getBytes();
		//用新的字符编码生成字符串
		return new String(aBits, sNewCharset);
	}

	/**
	 * 将字符编码转换成ISO-8859-1码
	 * @param sStr
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public final static String toISO_8859_1(String sStr) throws UnsupportedEncodingException
	{
		return changeCharset(sStr, ISO_8859_1);
	}

	/**
	 * 将字符编码转换成UTF-16BE码
	 * @param sStr
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public final static String toUTF16BE(String sStr) throws UnsupportedEncodingException
	{
		return changeCharset(sStr, UTF_16BE);
	}

	/**
	 * 将字符编码转换成UTF-16LE码
	 * @param sStr
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public final static String toUTF16LE(String sStr) throws UnsupportedEncodingException
	{
		return changeCharset(sStr, UTF_16LE);
	}

	/**
	 * 字符串编码转换的实现方法
	 * @param sStr  待转换编码的字符串
	 * @param sOldCharset 原编码
	 * @param sNewCharset 目标编码
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public final static String changeCharset(String sStr, String sOldCharset, String sNewCharset)
		throws UnsupportedEncodingException
	{
		//用旧的字符编码解码字符串。解码可能会出现异常。
		byte[] aBits = sStr.getBytes(sOldCharset);
		//用新的字符编码生成字符串
		return new String(aBits, sNewCharset);
	}

	/**
	 * 返回正则表达式的结果集
	 * @param sStr
	 * @param sPattern
	 * @return List<String>
	 */
	public final static List<String> getRegexResult(String sStr, String sPattern)
	{
		ArrayList<String> aList = new ArrayList<String>();
		Pattern oPattern = Pattern.compile(sPattern);
		Matcher oMatcher = oPattern.matcher(sStr);
		while(oMatcher.find())
		{
			int nTotal = oMatcher.groupCount();
			if(nTotal==0){
				aList.add(oMatcher.group());
			}
			for(int i = 1; i <= nTotal; i++)
			{
				aList.add(oMatcher.group(i));
			}
		}

		return aList;
	}
	
	/**
	 * 字符串正则表达式替换
	 * @param sSource
	 * @param sReplace
	 * @param sPattern
	 * @return String
	 */
	public final static String getRegexReplaceResult(String sSource, String sReplace, String sPattern)
	{
		Pattern oPattern = Pattern.compile(sPattern);
		Matcher oMatcher = oPattern.matcher(sSource);

		return oMatcher.replaceAll(sReplace);
	}	

	/**
	 * 正则表达式检查结果
	 * @param sStr
	 * @param sPattern
	 * @return boolean
	 */
	public final static boolean checkMather(String sStr, String sPattern)
	{
		Pattern oPattern = Pattern.compile(sPattern);
		Matcher oMather = oPattern.matcher(sStr);
		return oMather.matches();
	}
	
	/**
	 * 如果字符串为空则用默认值
	 * @param sStr
	 * @param sDefault
	 * @return String
	 */
	public final static String toStr(Object sStr, String sDefault)
	{
		return sStr == null ? sDefault : sStr.toString();
	}
	
	/**
	 * 把字符串转换成整数
	 * @param sValue
	 * @param nDefault
	 * @return int
	 */
	public final static int toInt(String sValue, int nDefault)
	{
		if(sValue == null || sValue.isEmpty()) return nDefault;
		return Integer.valueOf(sValue);
	}

	/**
	 * 把字符串转换成小数
	 * @param sValue
	 * @param nDefault
	 * @return Float
	 */
	public final static Float toFloat(String sValue, Float nDefault)
	{
		if(sValue == null || sValue.isEmpty()) return nDefault;
		return Float.valueOf(sValue);
	}
	/**
	 * 把字符串转换成浮点
	 * @param sValue
	 * @param nDefault
	 * @return float
	 */
	public final static float toFloat(String sValue, float nDefault)
	{
		if(sValue == null || sValue.isEmpty()) return nDefault;
		return Float.valueOf(sValue);
	}
	
	/**
	 * 把字符串转换成 布尔型
	 * @param sValue
	 * @param nDefault
	 * @return boolean
	 */
	public final static boolean toBoolean(String sValue, boolean nDefault)
	{
		if(sValue == null || sValue.isEmpty()) return nDefault;
		return Boolean.valueOf(sValue);
	}

    /**
     * Gets the date format with "yyyy-MM-dd" form.
     * @param date
     * @return
     */
    public final static String getDateFormat(String date){
        return date.trim().substring(0, 10);
    }

    public final static String getDateMonthFormat(String date){
        return date.trim().substring(0, 7);
    }

    /**
     * Gets the date time with the format 'yyyy-MM-dd'
     * @param date
     * @return
     */
    public final static String getDate2DayFormat(Date date){
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dataFormat.format(date);
    }

    /**
     * Formats the date and gets the final hour value.
     * @param date
     * @return
     */
    public final static String getDate2HourFormat(Date date){
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH24");
//        System.out.println(dataFormat.format(date));
        return dataFormat.format(date);
    }

    /**
     * Formats the date and gets the final month value.
     * @param date
     * @return
     */
    public final static String getDate2MonthFormat(Date date){
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM");
//        System.out.println(dataFormat.format(date));
        return dataFormat.format(date);
    }

    /**
     *  Formats the date and gets the final year value.
     * @param date
     * @return
     */
    public final static String getDate2YearFormat(Date date){
        DateFormat dataFormat = new SimpleDateFormat("yyyy");
//        System.out.println(dataFormat.format(date));
        return dataFormat.format(date);
    }

    public final static int getMaxDay(String year, String month){
        /*The month has 31 days*/
        int y = Integer.valueOf(year);
        int []array = {1,3,5,7,8,10,12}  ;
        int m = Integer.valueOf(month);
        System.out.println("month:" + m);
        for(int j : array) {
             if(j == m){
                 return 31;
             }
        }
        if(m == 2 && (y%4 == 0) && ((y % 100 != 0)||(y % 400 == 0))) {
              return 29;
        } else if(m == 2){
            return 28;
        }
        return 30;
    }
    
    public final static  String trim(String str){
    	if(null!=str){
    		return str.trim();
    	}
    	return str;
    }
    
    public static final boolean isEmpty(String str){
    	if(null==str||str.length()==0){
    		return true;
    	}
    	return false;
    }
}
