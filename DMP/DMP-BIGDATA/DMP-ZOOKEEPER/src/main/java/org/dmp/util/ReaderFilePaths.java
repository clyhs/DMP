
package org.dmp.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: ReaderFilePaths
 * Description: TODO(这里用一句话描述这个类的作用)
 * @author  zhoushubin@unioncast.com 
 * @date 2013-11-6 上午10:30:23
 * 
 */
public class ReaderFilePaths {

	/**
	 * Title: main
	 * Description: TODO(这里用一句话描述这个方法的作用)
	 * @param args
	 * @return void 返回类型
	 * @throws
	 */
	public static void main(String[] args) {
		
		List<String> list = readFilePath("C:\\Users\\zhou\\Desktop\\sss\\MUP\\code_1.1\\MUP_WEB");
		for(String s:list){
			System.out.println(s.replace("C:\\Users\\zhou\\Desktop\\sss\\MUP\\code_1.1\\"," "));
		}
	}

	public static List<String> readFilePath(String root){
		List<String> list = new ArrayList<String>();
		File f = new File(root);
		return foo(f,list);
	} 
	
	
	private static List<String> foo(File f,List<String> list){
		File[] flist = f.listFiles();//获取所有的文件和文件夹
		//遍历
		for(File ff:flist){
			if(ff.isDirectory()){
				list = foo(ff,list);
			}else{
				list.add(ff.getAbsolutePath());
			}
		}
		return list;
	}
}
