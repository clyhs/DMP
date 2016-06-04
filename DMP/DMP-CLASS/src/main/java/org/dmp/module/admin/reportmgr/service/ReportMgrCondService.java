package org.dmp.module.admin.reportmgr.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.dmp.module.admin.reportmgr.dao.ReportMgrCondDao;
import org.dmp.pojo.admin.reportmgr.ReportCond;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service("ReportMgrCondService")
@Scope("prototype")
public class ReportMgrCondService {
	
	@Resource(name = "ReportMgrCondDao")
	private ReportMgrCondDao reportMgrCondDao;
	
	public int addReportCondList(List<ReportCond> reportCondList){
		return reportMgrCondDao.addReportCond(reportCondList);
	}
	
	@SuppressWarnings("unchecked")
	public int deleteReportCondList(List<ReportCond> reportCondList){
		return reportMgrCondDao.delete(reportCondList);
	}
	
	/**
	 * 报表的toolbar的配置
	 * @param reportCondList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int editReportCondList(List<ReportCond> originalCond,List<ReportCond> reportCondList){
		Iterator<ReportCond> it = reportCondList.iterator();
		int nTotal = 0;
		Set<Integer> originalIdSet = new HashSet<Integer>();
		for(int i= 0; i< originalCond.size(); i++){
			originalIdSet.add(originalCond.get(i).getId());
		}
		while(it.hasNext())
		{
			ReportCond reportCond = it.next();
			if(reportCond.getId()==0)
			{
				reportMgrCondDao.insert(reportCond);
			}
			if(originalIdSet.contains(reportCond.getId())){
				reportMgrCondDao.update(reportCond);
				originalIdSet.remove(reportCond.getId());
			}
		}
		Iterator<Integer> idIterator = originalIdSet.iterator();
		while(idIterator.hasNext()){
			int id = idIterator.next();
			reportMgrCondDao.delete(reportMgrCondDao.getById(id));
		}
		nTotal++;
		return nTotal != 0? 1 :-1;
	}
	
	public List<ReportCond> getReportCondList(String reportId){
		return reportMgrCondDao.getReportCondList(reportId);
	}
	
	/**
	 * 将后台数据转换成reportcond对象
	 * @param reportCondListMap
	 * @return
	 * @throws ParseException
	 */
	public List<ReportCond> MaptoCond(List<Map<String, String>> reportCondListMap) throws ParseException{
		Iterator<Map<String, String>> it = reportCondListMap.iterator();
		List<ReportCond> reportCondList = new ArrayList<ReportCond>();
		while(it.hasNext())
		{
			Map<String, String> oReportMap = it.next();
			ReportCond reportCond = new ReportCond();
			reportCond.setReportId(oReportMap.get("reportId"));
			reportCond.setComponentId(oReportMap.get("componentId"));
			reportCond.setComponentType(oReportMap.get("componentType"));
			reportCond.setConditionName(oReportMap.get("conditionName"));
			reportCond.setFormat(oReportMap.get("format"));
			reportCond.setId(Integer.valueOf(oReportMap.get("sId")));
			reportCond.setLayout(Integer.valueOf(oReportMap.get("layout")));
			reportCond.setUpdateStaffId(oReportMap.get("updateStaffId"));
			reportCond.setComboxField(oReportMap.get("comboxField"));
			reportCond.setUpdateTime(new Date());
			reportCondList.add(reportCond);
		}
		return reportCondList;
	}

}
