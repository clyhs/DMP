package org.dmp.module.admin.quartz.kettle;

import java.util.List;

import org.dmp.pojo.admin.quartz.MonitorDataBean;

public abstract interface IKettleEngine {
	
	public static final String TYPE_TRANS = "ktr";
	public static final String TYPE_JOB = "kjb";

	/**
	 * 根据本地文件运行转换/作业
	 * @param filePath
	 * @param fileType
	 * @return
	 */
	public abstract boolean execute(String filePath, String fileType);

	/**
	 * 在资源库运行转换/作业
	 * @param repName
	 * @param username
	 * @param password
	 * @param filePath
	 * @param fileName
	 * @param fileType
	 * @return
	 */
	public abstract boolean execute(String repName, String username, String password, 
			String filePath, String fileName, String fileType);

	/**
	 * 获取资源库列表
	 * @return
	 */
	public abstract List getReps();
	
	/**
	 * 获取资源库的目录结构
	 * @param paramString1
	 * @param paramString2
	 * @param paramString3
	 * @return
	 */
	public abstract String getRepTreeJSON(String Repositories, String userName, String password);

	
	public abstract int checkRepLogin(String repName, String userName, String password);

	public abstract List<MonitorDataBean> getLogData(String repName, String userName,
			String passWord, String actionPath, String actionRef,
			String fileType);
}
