package org.dmp.core.util;

import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.log4j.Logger;

/****
 * 
 * @ClassName: ArrayUtil
 * @Description: 数组操作类
 * @author zhoushubin@unioncast.cn
 * @date 2013-3-4 上午9:23:32
 * 
 */
public class ArrayUtil {
	
	private static final Logger logger = Logger.getLogger(ArrayUtil.class);
	/****
	 * 
	 * @Title: join
	 * @Description: 把数组通过规定符号连接为字符串，
	 * @param @param aList
	 * @param @param sJoinFlag
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String join(Object[] aList, String sJoinFlag) {
		StringBuffer sJoin = new StringBuffer();
		for (int i = 0, nTotal = aList.length; i < nTotal; i++) {
			if (i > 0)
				sJoin.append(sJoinFlag);
			sJoin.append(aList[i].toString());
		}

		return sJoin.toString();
	}

	/****
	 * 
	 * @Title: join
	 * @Description: 把两个数据连接起来，生成一个新的数据，
	 * @param @param aList1
	 * @param @param aList2
	 * @param @return 设定文件
	 * @return Object[] 返回类型
	 * @throws
	 */
	public final static Object[] join(Object[] aList1, Object[] aList2) {
		Object[] aList = new Object[aList1.length + aList2.length];
		System.arraycopy(aList1, 0, aList, 0, aList1.length);
		System.arraycopy(aList2, 0, aList, aList1.length, aList2.length);
		return aList;
	}

	/***
	 * 
	 * @Title: dynaBeanToMap
	 * @Description: 把DynaBean转换成Map
	 * @param @param oBean
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public final static Map<String, Object> dynaBeanToMap(DynaBean oBean) {
		Map<String, Object> aMap = null;
		if (oBean != null) {
			LazyDynaBean oLazyDynaBean = (LazyDynaBean) oBean;
			aMap = oLazyDynaBean.getMap();
		}
		return aMap;
	}
}
