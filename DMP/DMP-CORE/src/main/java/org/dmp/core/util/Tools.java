package org.dmp.core.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/****
 * 
 * @ClassName: Tools
 * @Description: 一些常见的工具类
 * @author zhoushubin@unioncast.cn
 * @date 2013-3-4 上午9:27:39
 * 
 */
@SuppressWarnings("all")
public final class Tools {
	
	private static final Logger logger = Logger.getLogger(Tools.class);
	
	/**
	 * 将字符串转换成MD5
	 * 
	 * @param sStr
	 * @return String
	 */
	public final static String toMD5(String sStr) {
		return AlgorithmUitl.md5(sStr);
	}

	/**
	 * 将字符编码转换成UTF-8码
	 * 
	 * @param sStr
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public final static String toUTF8(String sStr)
			throws UnsupportedEncodingException {
		return StrUtil.toUTF8(sStr);
	}

	/**
	 * 返回正则表达式的结果集
	 * 
	 * @param sStr
	 * @param sPattern
	 * @return String[]
	 */
	public final static List<String> getRegexResult(String sStr, String sPattern) {
		return StrUtil.getRegexResult(sStr, sPattern);
	}

	/**
	 * 字符串正则表达式替换
	 * 
	 * @param sSource
	 * @param sReplace
	 * @param sPattern
	 * @return String
	 */
	public final static String getRegexReplaceResult(String sSource,
			String sReplace, String sPattern) {
		return StrUtil.getRegexReplaceResult(sSource, sReplace, sPattern);
	}

	/**
	 * 正则表达式检查结果
	 * 
	 * @param sStr
	 * @param sPattern
	 * @return boolean
	 */
	public final static boolean checkMather(String sStr, String sPattern) {
		return StrUtil.checkMather(sStr, sPattern);
	}

	/**
	 * 如果字符串为空则用默认值
	 * 
	 * @param sStr
	 * @param sDefault
	 * @return String
	 */
	public final static String toStr(Object sStr, String sDefault) {
		return StrUtil.toStr(sStr, sDefault);
	}

	/**
	 * 获取0-9范围的随机数字
	 * 
	 * @return long
	 */
	public final static long getRandom() {
		return getRandom(0, 9);
	}

	/**
	 * 获取iMin-iMax范围内的随机数字
	 * 
	 * @param nMin
	 * @param nMax
	 * @return long
	 */
	public final static long getRandom(int nMin, int nMax) {
		return AlgorithmUitl.getRandom(nMin, nMax);
	}

	/**
	 * 获取日期
	 * 
	 * @param oDate
	 * @param sDateFormat
	 * @return String
	 */
	public final static String getDate(Date oDate, String sDateFormat) {
		return DateUtil.dateFormat(sDateFormat, oDate);
	}

	/**
	 * 获取日期
	 * 
	 * @param sDate
	 * @param sDateFormat
	 * @return Date
	 */
	public final static Date getDate(String sDate, String sDateFormat) {
		return DateUtil.getDate(sDate, sDateFormat);
	}

	/**
	 * 获取当前系统时间
	 * 
	 * @return String
	 */
	public final static String getNow() {
		return DateUtil.getNow();
	}

	/**
	 * 到日期yyyy-MM-dd
	 * 
	 * @return String
	 */
	public final static String getDate() {
		return DateUtil.getDate();
	}

	/**
	 * 取日期HH:mm:ss
	 * 
	 * @return String
	 */
	public final static String getTime() {
		return DateUtil.getTime();
	}

	/**
	 * 计算两个日期差
	 * 
	 * @param sDateEnd
	 *            结束时间
	 * @param sDateStart
	 *            开始时间
	 * @return long 返回秒
	 */
	public final static long getDateDiff(String sDateEnd, String sDateStart) {
		return DateUtil.getDateDiff(sDateEnd, sDateStart);
	}

	/**
	 * 日期增减
	 * 
	 * @param sDatepart
	 *            [年：year，月：month，日：day，时：hour，分：minute，秒：second]
	 * @param iExpression
	 * @param sDate
	 * @return String
	 */
	public final static String dateAdd(String sDatepart, int iExpression,
			String sDate) {
		return DateUtil.dateAdd(sDatepart, iExpression, sDate);
	}

	/**
	 * 设置日期格式
	 * 
	 * @param aList
	 * @param sJoinFlag
	 * @return String
	 */
	public final static String join(Object[] aList, String sJoinFlag) {
		return ArrayUtil.join(aList, sJoinFlag);
	}

	/**
	 * 把DynaBean转换成Map
	 * 
	 * @param oBean
	 * @return static Map<String, Object>
	 */
	public final static Map<String, Object> dynaBeanToMap(DynaBean oBean) {
		return ArrayUtil.dynaBeanToMap(oBean);
	}

	public final static List<Map<String, Object>> dynaBeanToMap(
			List<DynaBean> aBean) {
		List<Map<String, Object>> aList = new LinkedList<Map<String, Object>>();
		if (aBean != null && aBean.size() > 0) {
			for (DynaBean oBean : aBean) {
				aList.add(dynaBeanToMap(oBean));
			}
		}
		return aList;
	}

	public final static void dynaBeanToList(List<List> rowList,
			List<DynaBean> bean) {
		if (bean != null && bean.size() > 0) {
			for (DynaBean oBean : bean) {
				int i = 0;
				Map<String, Object> map = dynaBeanToMap(oBean);
				int mapSize = map.size();
				List row = new ArrayList();
				while (i < mapSize) {
					i++;
					Object value = oBean.get("F" + i);
					row.add(value);
				}
				rowList.add(row);
			}
		}
	}

	public final static void tbDynaBeanToList(List colNameList,
			List colTypeList, List colAllowNullList, List<DynaBean> bean,
			List pkList) {
		// 处理主建列不在第一列的情况
		if (pkList.size() == 1) {
			String str = pkList.get(0).toString();
			if (bean != null && bean.size() > 0) {
				for (DynaBean oBean : bean) {
					Object colName = oBean.get("COLUMN_NAME");
					Object colType = oBean.get("DATA_TYPE");
					Object colAllowNull = oBean.get("NULLABLE");
					if (str.equals(colName)) {
						colNameList.add(colName);
						colTypeList.add(colType);
						colAllowNullList.add(colAllowNull);
						break;
					}
				}
				for (DynaBean oBean : bean) {
					Object colName = oBean.get("COLUMN_NAME");
					Object colType = oBean.get("DATA_TYPE");
					Object colAllowNull = oBean.get("NULLABLE");
					if (str.equals(colName)) {
						continue;
					}
					colNameList.add(colName);
					colTypeList.add(colType);
					colAllowNullList.add(colAllowNull);
				}
			}
		} else {
			if (bean != null && bean.size() > 0) {
				for (DynaBean oBean : bean) {
					Object colName = oBean.get("COLUMN_NAME");
					Object colType = oBean.get("DATA_TYPE");
					Object colAllowNull = oBean.get("NULLABLE");
					colNameList.add(colName);
					colTypeList.add(colType);
					colAllowNullList.add(colAllowNull);
				}
			}
		}
	}

	public final static void pkDynaBeanToList(List pkList, List<DynaBean> bean) {
		if (bean != null && bean.size() > 0) {
			for (DynaBean oBean : bean) {
				Object colName = oBean.get("COLUMN_NAME");
				pkList.add(colName);
			}
		}
	}

	/**
	 * 获取应用的绝对路径
	 * 
	 * @param sRoot
	 * @return String
	 */
	public final static String getRealPath(String sRoot) {
		return System.getProperty(sRoot);
	}

	/**
	 * Paginate with the specified data.
	 * 
	 * @param totalValue
	 * @param targetValue
	 * @param nStart
	 * @param nLimit
	 * @return result list set.
	 */
	public final static List getPaginationList(int totalValue,
			List targetValue, int nStart, int nLimit) {
		/* pagination section */
		List resultList = new ArrayList(nLimit);
		int remainder = totalValue - nStart;
		if (remainder > 0 && remainder > nLimit) {
			for (int i = nStart; i < nStart + nLimit; i++) {
				resultList.add(targetValue.get(i));
			}
		} else if (remainder > 0 && remainder <= nLimit) {
			for (int i = nStart; i < nStart + remainder; i++) {
				resultList.add(targetValue.get(i));
			}
		}
		return resultList;
	}

	public final static String getRequestIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public final static String getNowStdDay() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	public final static String getJsonString(List<DynaBean> aBean) {
		if (aBean.size() > 0 && aBean != null) {
			StringBuffer bf = new StringBuffer();
			bf.append("{");
			for (DynaBean oBean : aBean) {
				int i = 0;
				Map<String, Object> map = dynaBeanToMap(oBean);
				int mapSize = map.size();
				while (i < mapSize) {
					i++;
					bf.append("'").append("F" + i).append("'").append(":")
							.append("'");
					if (i == 1) {
						bf.append("合计");
					} else {
						bf.append(oBean.get("F" + i));
					}
					bf.append("'").append(",");
				}
			}
			return bf.toString().substring(0, bf.toString().length() - 1) + "}";
		} else {
			return "";
		}
	}

	public final static String getXMLString(List<DynaBean> aBean) {
		if (aBean.size() > 0 && aBean != null) {
			StringBuffer bf = new StringBuffer();
			for (DynaBean oBean : aBean) {
				bf.append("<set name='").append(oBean.get("F1")).append("' ")
						.append("value='").append(oBean.get("F2"))
						.append("'/>");
			}
			return bf.toString();
		} else {
			return "";
		}

	}

}
