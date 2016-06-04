package org.dmp.module.admin.quartz.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.module.admin.quartz.dao.TransLogDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service("TransLogService")
@Scope("prototype")
public class TransLogService {
	
	@Resource(name = "TransLogDao")
	private TransLogDao transLogDao;
	
	/**
	 * 获取transformation的指定转换日志
	 * @param transName
	 * @return
	 */
	public List<DynaBean> getTransList(String transName,int start, int offset){
		
		return transLogDao.getTransList(transName, start, offset);
	}
	
	public List<DynaBean> getStepList(String jobName, int start, int offset){
		
		return transLogDao.getStepList(jobName, start, offset);
	}

}
