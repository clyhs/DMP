package org.dmp.module.admin.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.module.admin.common.dao.ComboxDao;
import org.dmp.module.admin.common.dao.YmComponentDao;
import org.dmp.pojo.admin.common.YMType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;



/**
 * toolbarCombox控件Service层
 * @author shuyang
 *
 */
@Service("ToolbarComponentService")
@Scope("prototype")
public class ToolbarComponentService {
	
	
	@Resource(name = "YmComponentDao")
	private YmComponentDao ymComponentDao;
	
	@Resource(name = "ComboxDao")
	private ComboxDao comboxDao;
	
	/**
	 * 获取动态下拉框的值
	 * @param sTable
	 * @param orderId
	 * @return
	 */
	public List<DynaBean> getComboBoxBean(String sReportId,String sComponentId){
		String comboxField = "";
		String[] arr;
		String sSql = "select S_COMBOX_FIELD as comboxField from TD_S_REPORT_COND where S_REPORTID = '" + sReportId 
			   + "' and S_COMPONENT_ID = '" + sComponentId +"'";
		List<DynaBean> list = comboxDao.getComboxBean(sSql);
		if(list !=null && list.size() == 1){
			comboxField = list.get(0).get("COMBOXFIELD").toString();
		}

		if(comboxField.length()>0){
			arr = comboxField.split("\\.");
			if(arr.length>0){
				if(arr.length==1){
					sSql = " select S_ID,S_VALUE from TD_S_REPORT_DICT where S_NAME='"+arr[0]+"' order by S_ID ";
					list = comboxDao.getComboxBean(sSql);

					System.out.println(arr[0]+"'s size is :"+list.size());
					return list;
				}
				else{
					sSql = " select * from " + arr[0] + " order by " + arr[1];
					list = comboxDao.getComboxBean(sSql);
					return list;
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * 可单选或多选的年月控件
	 * @return
	 */
	public List<YMType> getYMComboBoxList(int ymRange){
		List<DynaBean> list = ymComponentDao.getYMComboBoxList(ymRange,"mysql");
		List<YMType> ymList = new ArrayList<YMType>();
		for(int i = 0;i < list.size();i++){
			YMType ym = new YMType();
			ym.setYmId(list.get(i).get("ymId").toString());
			ym.setYmName(list.get(i).get("ymId").toString());
			ymList.add(ym);
		}
		return ymList;
	}
	
	
}
