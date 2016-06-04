package org.dmp.util;


/**
 * 16进制字符和byte[]互转
 * @author 张竞
 *
 */
public class HexUtil {
	 /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * 用于建立十六进制字符的输出的大写字符数组
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data byte[]
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data byte[]
     * @param toLowerCase
     * <code>true</code> 传换成小写格式 ，
     * <code>false</code> 传换成大写格式
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制char[]
     */
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.  
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data byte[]
     * @return 十六进制String
     */
    public static String encodeHexStr(byte[] data) {
        return encodeHexStr(data, true);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data byte[]
     * @param toLowerCase
     * <code>true</code> 传换成小写格式 ，
     * <code>false</code> 传换成大写格式
     * @return 十六进制String
     */
    public static String encodeHexStr(byte[] data, boolean toLowerCase) {
        return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制String
     */
    protected static String encodeHexStr(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }

    /**
     * 将十六进制字符数组转换为字节数组
     *
     * @param data 十六进制char[]
     * @return byte[]
     * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    public static byte[] decodeHex(char[] data) {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.  
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit16(data[j], j) << 4;
            j++;
            f = f | toDigit16(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * 将十六进制字符转换成一个整数
     *
     * @param ch 十六进制char
     * @param index 十六进制字符在字符数组中的位置
     * @return 一个整数
     * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    protected static int toDigit16(char ch, int index) {
        return toDigit(ch,index,16);
    }
    
    /**
     * 将十六进制字符转换成一个整数
     *
     * @param ch 十六进制char
     * @param index 十六进制字符在字符数组中的位置
     * @return 一个整数
     * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    protected static int toDigit(char ch, int index,int radix) {
        int digit = Character.digit(ch, radix);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch
                    + " at index " + index);
        }
        return digit;
    }
    
    /**
     * 实现任意进制的转换(2-36)
     * @param src
     * @param hexn
     * @return
     */
    public static String toHexN(long src,int hexn){
    	return toHexN(src,hexn,13);
    }
    
    
    /**
     * 实现任意进制的转换(2-36)
     * @param i
     * @param radix
     * @param length 返回字符串位数(0-65)
     * @return
     */
    public static String toHexN(long i,int radix,int length){
    	if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
    	    radix = 10;
            if (radix == 10)
                return Long.toString(i);
            char[] buf = {'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'};
            int charPos = 64;
            boolean negative = (i < 0);

            if (!negative) {
                i = -i;
            }

            while (i <= -radix) {
                buf[charPos--] = DIGITS[(int)(-(i % radix))];
                i = i / radix;
            }
            buf[charPos] = DIGITS[(int)(-i)];

            if (negative) { 
                buf[--charPos] = '-';
            }

            return new String(buf, 65-length, length);
    }
    
    /**
     * 实现任意进制的转换(2-36)
     * @param src
     * @param hexn
     * @return
     */
    public static String toHexN(int src,int hexn){
    	return toHexN(src,hexn,6);
    }
    
    /**
     * 实现任意进制的转换(2-36)
     * @param i
     * @param radix
     * @param length 返回字符串位数(0-33)
     * @return
     */
    public static String toHexN(int i, int radix,int length){
    	 if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
    		    radix = 10;

    		/* Use the faster version */
    		if (radix == 10) {
    		    return Integer.toString(i);
    		}

    		char buf[] = {'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'};
    		boolean negative = (i < 0);
    		int charPos = 32;

    		if (!negative) {
    		    i = -i;
    		}

    		while (i <= -radix) {
    		    buf[charPos--] = DIGITS[-(i % radix)];
    		    i = i / radix;
    		}
    		buf[charPos] = DIGITS[-i];

    		if (negative) {
    		    buf[--charPos] = '-';
    		}

    		return new String(buf, 33-length, length);
    }
    
    
    public static long hexToNum(String hex,int radix){
    	char[] hexChars=hex.toCharArray();
    	long num=0;
    	for(int i=hexChars.length-1;i>0;i--){
    		int dig=Character.digit(hexChars[i], radix);
    		num+=dig*radix;
    	}
    	num+=Character.digit(hexChars[0], radix);
    	return num;
    }
    
    
    final static char[] digits = {
    	'0' , '1' , '2' , '3' , '4' , '5' ,
    	'6' , '7' , '8' , '9' , 'a' , 'b' ,
    	'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
    	'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
    	'o' , 'p' , 'q' , 'r' , 's' , 't' ,
    	'u' , 'v' , 'w' , 'x' , 'y' , 'z'
        };
    
    final static char[] DIGITS = {
    	'0' , '1' , '2' , '3' , '4' , '5' ,
    	'6' , '7' , '8' , '9' , 'A' , 'B' ,
    	'C' , 'D' , 'E' , 'F' , 'G' , 'H' ,
    	'I' , 'J' , 'K' , 'L' , 'M' , 'N' ,
    	'O' , 'P' , 'Q' , 'R' , 'S' , 'T' ,
    	'U' , 'V' , 'W' , 'X' , 'Y' , 'Z'
        };

}
