package org.dmp.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/****
 * 
* @ClassName: MD54J
* @Description: java里面的MD5加密算法
* @author  zhoushubin@unioncast.com 
* @date 2013-6-26 下午2:25:20
*
 */
public class MD54J {
	static final char ac[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
			'B', 'C', 'D', 'E', 'F' };
	public static String MD5 = "MD5";
	public static String SHA = "SHA";
	
	//public static final String[] hashTypes = new String[] { "MD2", "MD5", "SHA1", "SHA-256", "SHA-384", "SHA-512" };  
	 public static byte[] eccrypt(String info,String msgType) throws NoSuchAlgorithmException{  
		        //根据MD5算法生成MessageDigest对象  		     
			try {
				   MessageDigest md5 = MessageDigest.getInstance(msgType);  
			       byte[] srcBytes;
				   srcBytes = info.getBytes("utf-8");
				   md5.update(srcBytes); 
			       return md5.digest();  
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  return null;
		       //使用srcBytes更新摘要  
		      
		       //完成哈希计算，得到result  
	 } 
	 public static String MD5BASE32(String msg){
		 byte[] resultBytes = null;
		 try {
			resultBytes = MD54J.eccrypt(msg,MD5);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		 return MD54J.toHexString(resultBytes);
	 }
	 public static String SHABASE32(String msg){
		 byte[] resultBytes = null;
		 try {
			resultBytes = MD54J.eccrypt(msg,SHA);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		 return MD54J.toHexString(resultBytes);
	 }
	/**
	 * @Title: main
	 * @Description: 
	 * @param args
	 * @return void 返回类型
	 * @throws
	 */
	public static void main(String[] args) {
		   KeysUtil md5 = new KeysUtil();
		   String msg = "123213123";
		   System.out.println("密文是SHA：" + MD54J.SHABASE32(msg)); 
			 System.out.println("密文是：" + MD54J.MD5BASE32(msg));  
			 System.out.println("另外一种是：" +  md5.getMD5ofStr(msg)); 
			 System.out.println("另外一种是：" +  md5.getMD5ofStr(msg).equals(MD54J.MD5BASE32(msg)));  

	}
	
	 private static String toHexString(byte[] b) {  
		       StringBuilder sb = new StringBuilder(b.length * 2);  
		       for (int i = 0; i < b.length; i++) {  
		           sb.append(ac[(b[i] & 0xf0) >>> 4]);  
		           sb.append(ac[b[i] & 0x0f]);  
		      }  
		     return sb.toString();  
		  } 


}
