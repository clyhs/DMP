package org.dmp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/***
 * 
* @ClassName: Validation
* @Description: 验证相关的帮助类
* @author  zhoushubin@unioncast.com 
* @date 2013-3-12 下午2:04:12
*
 */
public class Validation {
	public static final String email_reg = "^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$";//验证电子邮件的正则表达式语句
	public static final String phone_reg = "^([0,\\\\+]){0,1}8{0,1}6{0,1}((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0-1,5-9]))\\d{8}$";//验证手机的正则表达式语句,包含了86和不是86的
	public static final String phone86_reg = "^([0,\\\\+]){0,1}86((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0-1,5-9]))\\d{8}$";//验证手机的正则表达式语句是否带了86，086，+86开头
	public static final String XLT_reg = "^0[0-9]{9,11}$";//验证手机的正则表达式语句
	public static final String date_reg = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";//验证日期的正则
	public static final String ip_reg = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";//验证IP的正则
	public static final String number_reg = "^[\\\\-]{0,1}[0-9]*$";//判断是数字
	public static final String e_word_reg = "^.[A-Za-z]+$";//判断只能是英文
	public static final String word_number_reg = "^.[A-Za-z0-9]+$";//只能是英文和数字
	public static final String c_word_reg = "^[\u4e00-\u9fa5]{0,}$";//汉字
	public static final String dns_reg = "^[a-zA-Z0-9]+([a-zA-Z0-9\\-\\.]+)?\\.(com|org|net|cn|com.cn|edu.cn|grv.cn|)$";//验证域名
	public static final String tel_reg = "^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$";//固话
	public static final String crbtid_reg = "^8100[1,2,3,4][0-9]{7}";//81001,81002,81003,81004
	public static final String diyid_reg = "^81007[0-9]{7}";//81007,
	public static final String plusid_reg = "^81006[0-9]{7}";//81006,
	public static final String boxid_reg = "^81009[1-9][0-9]{6}";//810099,普通铃音盒
	public static final String diyboxid_reg = "^810090[0-9]{6}";//810090,diy专属铃音盒
	public static Map<String,Pattern> pMap = new HashMap<String,Pattern>();//缓存正则表达式的数据

	static {
		pMap.put(email_reg, Pattern.compile(email_reg));
		pMap.put(phone_reg, Pattern.compile(phone_reg));
		pMap.put(date_reg, Pattern.compile(date_reg));
		pMap.put(ip_reg, Pattern.compile(ip_reg));
		pMap.put(c_word_reg, Pattern.compile(c_word_reg));
		pMap.put(number_reg, Pattern.compile(number_reg));
		pMap.put(e_word_reg, Pattern.compile(e_word_reg));
		pMap.put(word_number_reg, Pattern.compile(word_number_reg));
		pMap.put(dns_reg, Pattern.compile(dns_reg));
		pMap.put(crbtid_reg, Pattern.compile(crbtid_reg));
		pMap.put(diyid_reg, Pattern.compile(diyid_reg));
		pMap.put(plusid_reg, Pattern.compile(plusid_reg));
		pMap.put(boxid_reg, Pattern.compile(boxid_reg));
		pMap.put(phone86_reg,  Pattern.compile(phone86_reg));
	}
	
	/***
	 * 
	* @Title: isCWord
	* @Description: 判断是否为中文
	* @param @param word
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public static boolean isCWord(String word){
		return validation(c_word_reg,word);
	}
	public static boolean isCRBT(String word){
		return validation(crbtid_reg,word);
	}
	public static boolean isPLUS(String word){
		return validation(plusid_reg,word);
	}
	public static boolean isBOX(String word){
		return validation(boxid_reg,word);
	}
	public static boolean isDIYBOX(String word){
		return validation(diyboxid_reg,word);
	}
	/***
	 * 
	* @Title: isEWord
	* @Description: 判断是否是全是英文单词，
	* @param @param word
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public static boolean isEWord(String word){
		return validation(e_word_reg,word);
	}
	/****
	 * 
	* @Title: isNumber
	* @Description: 判断是否是号码
	* @param @param number
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public static boolean isNumber(String number){
		return validation(number_reg,number);
	}
	/***
	 * 
	* @Title: isNEWord
	* @Description: 判断数组和英文的组合
	* @param @param word
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public static boolean isNEWord(String word){
		return validation(word_number_reg,word);
	}
	/***
	 * 
	* @Title: isDNS
	* @Description: 判断是否是域名
	* @param @param dns
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public static boolean isDNS(String dns){
		return validation(dns_reg,dns_reg);
	}
	/****
	 * 
	* @Title: validation 正则验证
	* @Description: 验证正则信息  
	* @param @param reg 正则表达式
	* @param @param value  需要验证的值
	* @param @return    设定文件 
	* @return boolean    返回类型 如果符合，返回TRUE 
	* @throws
	 */
	public static boolean validation(String reg,String value){
		 Pattern p = pMap.get(reg);
		 if(p == null){
			 p = Pattern.compile(reg);   
			 pMap.put(reg, p);
		 }
	     return p.matcher(value).matches(); 
	}
	/****
	 * 
	* @Title: isEmail
	* @Description: 验证是否是邮箱
	* @param @param email
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public static boolean isEmail(String email){
		return validation(email_reg,email);
	}
	/***
	 * 
	* @Title: isDate
	* @Description: 判断是否是日期
	* @param @param date
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public static boolean isDate(String date){
		return validation(date_reg,date);
	}
	/***
	 * 
	* @Title: isPhone
	* @Description: 验证是否是手机号码
	* @param @param phone
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public static boolean isPhone(String phone){
		return validation(phone_reg,phone);
	}
	
	/***
	 * 
	* Title: isPhone86
	* Description: 开头一定带86的电话号码
	* @param phone
	* @return
	* @return boolean 返回类型
	* @throws
	 */
	public static boolean isPhone86(String phone){
		return validation(phone86_reg,phone);
	}

	/****
	 * 
	* @Title: isXLT
	* @Description: 验证是否是小灵通
	* @param phone
	* @return
	* @return boolean 返回类型
	* @throws
	 */
	public static boolean isXLT(String phone){
		return validation(XLT_reg,phone);
	}
	/***
	 * 
	* @Title: isTEL
	* @Description: 判断固话
	* @param phone
	* @return
	* @return boolean 返回类型
	* @throws
	 */
	public static boolean isTEL(String phone){
		return validation(tel_reg,phone);
	}
	/***
	 * 
	* @Title: isIP
	* @Description: 验证是否是IP地址
	* @param @param ip
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public static boolean isIP(String ip){
		return validation(ip_reg,ip);
	}
	
	/***
	 * 
	* @Title: islongThenNum
	* @Description: 判断字符串str的长度是否小于等于 size
	* @param @param str
	* @param @param size
	* @param @return    设定文件
	* @return boolean    返回类型 如果小于，返回true
	* @throws
	 */
	public static boolean isLongThenNum(String str,int size){
		if(str == null ) return Boolean.FALSE;
		if(size == 0) return Boolean.FALSE;
		return str.length()<=size;
	}
	/***
	 * 
	* Title: isPhonesub86
	* Description: 去掉号码中附带了86,086，+86的
	* 如+8613333333333 --->13333333333
	* @param phone
	* @return
	* @return String 返回类型
	* @throws
	 */
	public static String isPhonesub86(String phone){
		if(Validation.isPhone86(phone)&&!Validation.isEmail(phone))//电话号码，但是不是邮箱
		{
			
				if(phone.startsWith("086")){
					return phone.replaceFirst("086", "");
				}else if(phone.startsWith("+86")) {
					return  phone.replaceFirst("+86", "");
				}else if(phone.startsWith("86")){
					return phone.replaceFirst("86", "");
				}
		}
	return phone;
}
	
	/**
	 * @param args
	 */
	public static void main(String[] args){
//		long st = System.currentTimeMillis();
//		System.out.println(st);
//		for(int i= 0;i<1000000000;i++){
//			Validation.isEmail("loverightzhou@hotmail.com");
//		}
//		System.out.println(Validation.isLongThenNum("dddd", 4));
//		System.out.println( (long)System.currentTimeMillis()-st);
//		String sal = "32";
//		String reg = "0{3,}|1{3,}|2{3,}|3{3,}|4{3,}|5{3,}|6{3,}|7{3,}|8{3,}|9{3,}";
//		//reg = /(\d)(?=\1{2,})/;
//		System.out.println(sal.split(reg).length>1);
//		System.out.println(Validation.isPhone86("13331130917"));
		System.out.println(Validation.isNumber("100"));
	}

}
