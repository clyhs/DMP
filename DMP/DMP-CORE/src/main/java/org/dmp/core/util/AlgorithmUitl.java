package org.dmp.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.log4j.Logger;

/***
 * 
 * @ClassName: AlgorithmUitl
 * @Description: 算法类
 * @author zhoushubin@unioncast.cn
 * @date 2013-3-4 上午9:22:12
 * 
 */
public final class AlgorithmUitl {
	private static final Logger m_oLog = Logger.getLogger(AlgorithmUitl.class);

	private static char[] m_aBase64EncodeChars = new char[] { 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
			'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', '+', '/' };

	private static byte[] m_aBase64DecodeChars = new byte[] { -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58,
			59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8,
			9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
			-1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
			37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1,
			-1, -1, -1 };

	/****
	 * 
	 * @Title: md5
	 * @Description: 获取MD5加密串
	 * @param @param sStr
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String md5(String sStr) {
		try {
			byte[] aInput = sStr.getBytes();
			MessageDigest oMD5Inst = MessageDigest.getInstance("MD5");
			oMD5Inst.update(aInput);
			byte[] aMD5 = oMD5Inst.digest();
			StringBuffer sMD5 = new StringBuffer();
			for (int i = 0; i < aMD5.length; i++) {
				int nTemp = ((int) aMD5[i]) & 0xff;
				if (nTemp < 16) {
					sMD5.append("0");
				}
				sMD5.append(Integer.toHexString(nTemp));
			}
			return sMD5.toString();
		} catch (Exception e) {
			m_oLog.error(e.getMessage(), e.getCause());
			return null;
		}
	}

	/***
	 * 
	 * @Title: encodeBase64
	 * @Description: 生成64字节的加密串
	 * @param @param sStr
	 * @param @return
	 * @param @throws UnsupportedEncodingException 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String encodeBase64(String sStr)
			throws UnsupportedEncodingException {
		byte[] aData = sStr.getBytes("iso8859-1");
		StringBuffer sEncodeBase64 = new StringBuffer();
		int nLen = aData.length;
		int i = 0;
		int b1, b2, b3;
		while (i < nLen) {
			b1 = aData[i++] & 0xff;
			if (i == nLen) {
				sEncodeBase64.append(m_aBase64EncodeChars[b1 >>> 2]);
				sEncodeBase64.append(m_aBase64EncodeChars[(b1 & 0x3) << 4]);
				sEncodeBase64.append("==");
				break;
			}

			b2 = aData[i++] & 0xff;
			if (i == nLen) {
				sEncodeBase64.append(m_aBase64EncodeChars[b1 >>> 2]);
				sEncodeBase64.append(m_aBase64EncodeChars[((b1 & 0x03) << 4)
						| ((b2 & 0xf0) >>> 4)]);
				sEncodeBase64.append(m_aBase64EncodeChars[(b2 & 0x0f) << 2]);
				sEncodeBase64.append("=");
				break;
			}

			b3 = aData[i++] & 0xff;
			sEncodeBase64.append(m_aBase64EncodeChars[b1 >>> 2]);
			sEncodeBase64.append(m_aBase64EncodeChars[((b1 & 0x03) << 4)
					| ((b2 & 0xf0) >>> 4)]);
			sEncodeBase64.append(m_aBase64EncodeChars[((b2 & 0x0f) << 2)
					| ((b3 & 0xc0) >>> 6)]);
			sEncodeBase64.append(m_aBase64EncodeChars[b3 & 0x3f]);
		}

		return sEncodeBase64.toString();
	}

	/***
	 * 
	 * @Title: decodeBase64
	 * @Description: 以Base64解密
	 * @param @param sStr
	 * @param @return
	 * @param @throws UnsupportedEncodingException 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String decodeBase64(String sStr)
			throws UnsupportedEncodingException {
		StringBuffer sData = new StringBuffer();
		byte[] aData = sStr.getBytes("US-ASCII");
		int nLen = aData.length;
		int i = 0;
		int b1, b2, b3, b4;
		while (i < nLen) {
			do {
				b1 = m_aBase64DecodeChars[aData[i++]];
			} while (i < nLen && b1 == -1);
			if (b1 == -1)
				break;

			do {
				b2 = m_aBase64DecodeChars[aData[i++]];
			} while (i < nLen && b2 == -1);
			if (b2 == -1)
				break;
			sData.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

			do {
				b3 = aData[i++];
				if (b3 == 61)
					return sData.toString();// .getBytes("iso8859-1");
				b3 = m_aBase64DecodeChars[b3];
			} while (i < nLen && b3 == -1);
			if (b3 == -1)
				break;
			sData.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

			do {
				b4 = aData[i++];
				if (b4 == 61)
					return sData.toString();// .getBytes("iso8859-1");
				b4 = m_aBase64DecodeChars[b4];
			} while (i < nLen && b4 == -1);
			if (b4 == -1)
				break;
			sData.append((char) (((b3 & 0x03) << 6) | b4));
		}

		return sData.toString();// .getBytes("iso8859-1");
	}

	/***
	 * 
	 * @Title: getRandom
	 * @Description: 获取nMin-nMax范围内的随机数字
	 * @param @param nMin
	 * @param @param nMax
	 * @param @return 设定文件
	 * @return long 返回类型
	 * @throws
	 */
	public final static long getRandom(int nMin, int nMax) {
		return Math.round(nMin + (nMax - nMin) * Math.random());
	}

}
