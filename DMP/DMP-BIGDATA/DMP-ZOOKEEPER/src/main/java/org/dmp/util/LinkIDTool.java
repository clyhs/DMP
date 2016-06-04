package org.dmp.util;

import java.util.Random;



public class LinkIDTool {
	
	
	private static  String MAC = "" ;
	private static char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
		'T', 'U', 'V', 'W', 'X', 'Y', 'Z' ,'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	static{
		try {
			MAC=SystemUtils.getMACAddress();
		} catch (Exception e) {
			MAC=toHexN(System.nanoTime(),62,12);
		}
		System.out.println("------------------------>MAC:"+MAC);
	}
	public static String getLinkID36(int lenght) {
		return toHexN(System.nanoTime(), 36, lenght);
	}

	public static String getDefaultLinkID36() {
		return MAC+toHexN(System.nanoTime(), 62, 8);
	}

	/**
	 * 实现任意进制的转换(2-62)
	 * 
	 * @param i
	 * @param radix
	 *            进制数量
	 * @param length
	 *            返回字符串位数(0-65)
	 * @return
	 */
	public static String toHexN(long i, int radix, int length) {
		if (radix < Character.MIN_RADIX || radix > 62)
			radix = 10;
		if (radix == 10)
			return Long.toString(i);
		char[] buf = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
				'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
				'0', '0' };
		int charPos = 64;
		boolean negative = (i < 0);

		if (!negative) {
			i = -i;
		}

		while (i <= -radix) {
			buf[charPos--] = DIGITS[(int) (-(i % radix))];
			i = i / radix;
		}
		buf[charPos] = DIGITS[(int) (-i)];

		if (negative) {
			buf[--charPos] = '-';
		}

		return new String(buf, 65 - length, length);
	}



	
/***
 * 
* @Title: createRandomIDWithOutInput
* 生成随机的returnsize位数的不包含input中的随机数
* 当input包含returnsize*10内全部可选时，返回负数
* 如:当你需要10以内的随机数，但是不包含{0,1,2,3,4,5,6,7,8,9}
* 也就是returnsize=1,input{0,1,2,3,4,5,6,7,8,9}
* 将返回负数
* @param returnsize 返回ID的不超过的位数,returnsize不能是小于等于0,否则返回-1
* @param input 不包含的随机数
* @return
* @return int 返回类型
* @throws
 */
	public static int createRandomIDWithOutInput(int returnsize,int ...input){
		if(returnsize<=0)return -1;
		Double number = Math.pow(10,returnsize);
		int  factor = number.intValue();
		if(input == null||(input != null&&input.length>=factor))return -1;
		int id = r.nextInt(factor);
		for(int  i  = 0;i<input.length;i++){
				if(input[i] == id) return createRandomIDWithOutInput(returnsize,input) ;
		}
		return id;
	}
	
	public static int RandomIDWithOutInput(int ...input){
		int[] copyfactors = factors.clone();
		if(input == null||(input != null&&input.length>=copyfactors.length))return -1;
		for(int  i  = 0;i<input.length;i++){
			if (input[i]<copyfactors.length) copyfactors[input[i]] = input[i];
		}
		for(int  i  = 1;i<copyfactors.length;i++){
			if(copyfactors[i] == -1) return i;
		}
		return -1;
	}
	
	
	public static   int[] factors = new int[100];
	static{
		for(int i=0;i<factors.length;i++){
			factors[i] = -1;
		}
    }
	
	public static int createRandomIDWithOutInput2(int max,int ...input){
		if(max<=0)return -1;
		if(input == null||(input != null&&input.length>=max))return -1;
		int id = r.nextInt(max);
		for(int  i  = 0;i<input.length;i++){
				if(input[i] == id) return createRandomIDWithOutInput2(max,input) ;
		}
		return id;
	}
	private static Random r = new Random();

	public static void main(String arg[]) throws Exception {
		System.out.println(DIGITS.length);
		System.out.println(SystemUtils.getMACAddress());
		System.out.println(getLinkID36(20));
		System.out.println(getLinkID36(20));
		System.out.println(getLinkID36(20));
		System.out.println(getLinkID36(20));
		System.out.println(getDefaultLinkID36());
		System.out.println(getDefaultLinkID36());
		long l=System.nanoTime();
		System.out.println(l);
		System.out.println(toHexN(l,62,13));
		System.out.println(toHexN(l,62,8));
		
		long n=HexUtil.hexToNum("2710E1EBA8", 36);
		System.out.println(n);
		
		System.out.println(toHexN(n,62,12));
//		System.out.println(getDefaultLinkID36());
//		//System.out.println(UUID.randomUUID().toString());
//		String hex=HexUtil.encodeHexStr("192.168.1.130".getBytes());
//		System.out.println(RandomIDWithOutInput(new int[]{0,1,2,3,4,5,6,7,8,9}));
//		for(int i = 0;i<factors.length;i++){
//			System.out.println(factors[i]);
//		}
//		int[] copyfactors = factors.clone();
//		System.out.println(copyfactors.toString());
//		System.out.println(factors.toString());
	}
}
