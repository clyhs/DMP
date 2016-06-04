package org.dmp.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

@SuppressWarnings("all")
public final class FileUtil {
	private static final Logger logger = Logger.getLogger(FileUtil.class);

	/***
	 * 
	 * @Title: createFile
	 * @Description: 新建文件
	 * @param @param filePathAndName
	 * @param @param fileContent 文件内容
	 * @return void 返回类型
	 * @throws
	 */
	public static void createFile(String filePathAndName, String fileContent) {
		try {
			File file = new File(filePathAndName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter resultFile = new FileWriter(file);
			PrintWriter writer = new PrintWriter(resultFile);
			writer.println(fileContent);
			resultFile.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(new JsonBuilder().appendException(String.format(
					"create new file(fileName:%s ,fileContent:%s ) faild ",
					filePathAndName, fileContent)));
		}
	}

	/***
	 * 
	 * @Title: getRelativeDir
	 * @Description: 获取文件所在的相对目录
	 * @param @param sFile
	 * @param @param sDefaultDir
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String getRelativeDir(String sFile, String sDefaultDir) {
		if (sDefaultDir == null)
			sDefaultDir = getCurrentDir();
		StringBuffer sPrePath = new StringBuffer();
		String[] aFile = sFile.replace('\\', '/').split("/");
		for (String sValue : aFile) {
			if (sValue.equals("."))
				continue;
			if (sValue.equals(".."))
				sPrePath.append("../");
		}

		sFile = sDefaultDir;
		if (sPrePath.length() > 0) {
			try {
				File oFile = new File(sPrePath.toString());
				sFile = oFile.getCanonicalPath();
			} catch (Exception oException) {
				logger.error(oException.getMessage(), oException.getCause());
			}
		}

		return sFile.replace("//", "/");
	}

	/****
	 * 
	 * @Title: getAbsoluteFile
	 * @Description: 获取绝对路径文件
	 * @param @param sFile
	 * @param @param sDefaultDir
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String getAbsoluteFile(String sFile, String sDefaultDir) {
		if (sDefaultDir == null)
			sDefaultDir = getCurrentDir();
		StringBuffer sPrePath = new StringBuffer();
		StringBuffer sTempFile = new StringBuffer();
		String[] aFile = sFile.replace('\\', '/').split("/");
		for (String sValue : aFile) {
			if (sValue.equals("."))
				continue;
			if (sValue.equals("..")) {
				sPrePath.append("../");
				continue;
			}
			sTempFile.append("/" + sValue);
		}
		if (sPrePath.length() == 0) {
			sFile = sDefaultDir + sTempFile.toString();
		} else {
			try {
				File oFile = new File(sPrePath.toString());
				sFile = oFile.getCanonicalPath() + sTempFile.toString();
			} catch (Exception oException) {
				logger.error(oException.getMessage(), oException.getCause());
			}
		}

		return sFile.replace("//", "/");
	}

	/***
	 * 
	 * @Title: getCurrentDir
	 * @Description: 获取当前路径
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String getCurrentDir() {
		return System.getProperty("user.dir");
	}

	/***
	 * 
	 * @Title: getFileName
	 * @Description: 获取文件名
	 * @param @param sFile
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public final static String getFileName(String sFile) {
		String[] aFile = sFile.replace('\\', '/').split("/");

		return aFile[aFile.length - 1];
	}

	/***
	 * 
	 * @Title: newFolder
	 * @Description: 创建目录
	 * @param @param folderPath 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void newFolder(String folderPath) {
		try {
			File file = new File(folderPath);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			logger.error(new JsonBuilder().appendException(String.format(
					"create folder:%s  faild", folderPath)), e);
		}
	}

	/****
	 * 
	 * @Title: delFile
	 * @Description: 删除文件
	 * @param @param filePathAndName 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void delFile(String filePathAndName) {
		try {
			File myDelFile = new File(filePathAndName);
			if (myDelFile.exists()) {
				myDelFile.delete();
				// myDelFile.deleteOnExit()
			}
		} catch (Exception e) {
			logger.error(new JsonBuilder().appendException(String.format(
					"delete filePathAndName:%s  faild", filePathAndName)), e);
		}
	}

	/****
	 * 
	 * @Title: delFolder
	 * @Description: 删除指定目录下的所有文件和目录
	 * @param @param folderPath 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			logger.error(new JsonBuilder().appendException(String.format(
					"delete filePathAndName:%s  faild", folderPath)), e);
		}
	}

	private static void delFolder(File file) {
		if (!file.exists()) {
			return;
		}
		try {
			if (file.exists() && file.isDirectory()) {
				String folderPath = file.getAbsolutePath();
				delFolder(folderPath);
			}
		} catch (Exception e) {
			logger.error(new JsonBuilder().appendException(String.format(
					"deleteFolder  file:%s  faild", file)), e);
		}
	}

	/***
	 * 
	 * @Title: delAllFile
	 * @Description: 删除目录下的所有文件
	 * @param @param path 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			String[] tempList = file.list();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				if (path.endsWith(File.separator)) {
					temp = new File(path + tempList[i]);
				} else {
					temp = new File(path + File.separator + tempList[i]);
				}
				if (temp.isFile()) {
					temp.delete();
				}
				if (temp.isDirectory()) {
					delAllFile(temp);// 先删除文件夹里面的文件
					delFolder(temp);
					// delFolder(temp);// 再删除空文件夹
				}
			}
		}
	}

	/***
	 * 
	 * @Title: delAllFile
	 * @Description: 删除文件夹里面的文件
	 * @param @param file 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void delAllFile(File file) {
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				delAllFile(files[i]);
			}
		} else {
			file.delete();
		}
	}

	/***
	 * 
	 * @Title: copyFile
	 * @Description: 复制文件 单个文件的复制
	 * @param @param srcPathAndName 原文件路径
	 * @param @param destPathAndName 复制后的路径
	 * @return void 返回类型
	 * @throws
	 */
	public static void copyFile(String srcPathAndName, String destPathAndName) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(srcPathAndName);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(srcPathAndName); // 读入原文件
				FileOutputStream fs = new FileOutputStream(destPathAndName);
				int length = (int) oldfile.length();
				byte[] buffer = new byte[length];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				if (inStream != null) {
					inStream.close();
				}
			}
		} catch (Exception e) {
			logger.error(new JsonBuilder().appendException(String.format(
					"copy   file:%s --> %s  faild", srcPathAndName,
					destPathAndName)), e);
		}
	}

	/***
	 * 
	 * @Title: copyFolder
	 * @Description: 复制整个文件夹
	 * @param @param srcPath
	 * @param @param destPath 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void copyFolder(String srcPath, String destPath) {
		File src = new File(srcPath);
		File dest = new File(destPath);
		try {
			if (!dest.exists()) {
				dest.mkdir();
			}
			// (new File(destPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			String[] file = null;
			if (src != null) {
				file = src.list();
			}
			File temp = null;
			if (file != null) {
				for (int i = 0; i < file.length; i++) {
					if (srcPath.endsWith(File.separator)) {
						temp = new File(srcPath + file[i]);
					} else {
						temp = new File(srcPath + File.separator + file[i]);
					}
					if (temp.isFile()) {
						FileInputStream input = new FileInputStream(temp);
						FileOutputStream output = new FileOutputStream(destPath
								+ "/ " + (temp.getName()).toString());
						byte[] b = new byte[1024 * 5];
						int len;
						while ((len = input.read(b)) != -1) {
							output.write(b, 0, len);
						}
						output.flush();
						output.close();
						input.close();
					}
					if (temp.isDirectory()) {// 如果是子文件夹
						copyFolder(srcPath + "/" + file[i], destPath + "/"
								+ file[i]);
					}
				}
			}
		} catch (Exception e) {
			logger.error(new JsonBuilder().appendException(String.format(
					"copy   fold:%s --> %s  faild", srcPath, destPath)), e);
		}
	}

	/***
	 * 
	 * @Title: moveFile
	 * @Description: 移动单个文件到指定的目录下
	 * @param @param srcPathAndName
	 * @param @param destPathAndName 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void moveFile(String srcPathAndName, String destPathAndName) {
		File file = new File(destPathAndName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error(new JsonBuilder().appendException(String.format(
						"move file :%s --> %s  faild", srcPathAndName,
						destPathAndName)), e);
			}
		}
		copyFile(srcPathAndName, destPathAndName);
		delFile(srcPathAndName);
	}

	/****
	 * 
	 * @Title: moveFolder
	 * @Description: 移动目录下的所有文件指定路径
	 * @param @param srcPath
	 * @param @param destPath 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void moveFolder(String srcPath, String destPath) {
		copyFolder(srcPath, destPath);
		delFolder(srcPath);
	}

	/****
	 * 
	 * @Title: zipFile
	 * @Description:压缩文件
	 * @param @param srcPath 需要压缩的文件夹
	 * @param @param destPathAndName 压缩后的文件名称
	 * @return void 返回类型
	 * @throws
	 */
	public static void zipFile(String srcPath, String destPathAndName) {
		if (srcPath == null || srcPath.length() == 0) {
			logger.error(new JsonBuilder().appendException(String.format(
					"args  srcPath :%s is null or length is less 0", srcPath)));
		}
		File file = new File(srcPath);
		if (!file.exists()) {
			logger.error(new JsonBuilder().appendException(String.format(
					"args  file :%s is not exists", srcPath)));
		}
		String baseDirPath = file.getAbsolutePath();
		File targetFile = new File(destPathAndName);
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					targetFile));
			if (file.isFile()) {
				fileToZip(baseDirPath, file, out);
			} else {
				dirToZip(baseDirPath, file, out);
			}
			out.close();
		} catch (IOException e) {
			logger.error(new JsonBuilder().appendException(String.format(
					"ZIP fold( %s--zip-->%s  ) is faild! ", srcPath,
					destPathAndName)), e);
		}
	}

	/***
	 * 
	 * @Title: unzipFile
	 * @Description: 解压文件
	 * @param @param zipFilePathAndName
	 * @param @param destPath 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void unzipFile(String zipFilePathAndName, String destPath) {
		if (!destPath.endsWith(File.separator)) {
			destPath += File.separator;
		}
		try {
			ZipFile zipFile = new ZipFile(zipFilePathAndName);
			ZipEntry entry = null;
			String entryName = null;
			String targetFileName = null;
			byte[] buffer = new byte[4096];
			int bytes_read;
			Enumeration entrys = zipFile.entries();
			while (entrys.hasMoreElements()) {
				entry = (ZipEntry) entrys.nextElement();
				entryName = entry.getName();
				targetFileName = destPath + entryName;
				if (entry.isDirectory()) {
					new File(targetFileName).mkdirs();
					continue;
				} else {
					new File(targetFileName).getParentFile().mkdirs();
				}
				File targetFile = new File(targetFileName);
				FileOutputStream os = new FileOutputStream(targetFile);
				InputStream is = zipFile.getInputStream(entry);
				while ((bytes_read = is.read(buffer)) != -1) {
					os.write(buffer, 0, bytes_read);
				}
				os.close();
				is.close();
			}
		} catch (IOException e) {
			logger.error(new JsonBuilder().appendException(String.format(
					"ZIP fold( %s--unzip-->%s  ) is faild! ",
					zipFilePathAndName, destPath)), e);
		}
	}

	private static void dirToZip(String baseDirPath, File dir,
			ZipOutputStream out) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files.length == 0) {
				ZipEntry entry = new ZipEntry(getEntryName(baseDirPath, dir));
				try {
					out.putNextEntry(entry);
					out.closeEntry();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					fileToZip(baseDirPath, files[i], out);
				} else {
					dirToZip(baseDirPath, files[i], out);
				}
			}
		}
	}

	private static void fileToZip(String baseDirPath, File file,
			ZipOutputStream out) {
		FileInputStream in = null;
		ZipEntry entry = null;
		byte[] buffer = new byte[4096];
		int bytes_read;
		if (file.isFile()) {
			try {
				in = new FileInputStream(file);
				entry = new ZipEntry(getEntryName(baseDirPath, file));
				out.putNextEntry(entry);
				while ((bytes_read = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytes_read);
				}
				out.closeEntry();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String getEntryName(String baseDirPath, File file) {
		if (!baseDirPath.endsWith(File.separator)) {
			baseDirPath += File.separator;
		}
		String filePath = file.getAbsolutePath();
		if (file.isDirectory()) {
			filePath += "/";
		}
		int index = filePath.indexOf(baseDirPath);
		return filePath.substring(index + baseDirPath.length());
	}

}
