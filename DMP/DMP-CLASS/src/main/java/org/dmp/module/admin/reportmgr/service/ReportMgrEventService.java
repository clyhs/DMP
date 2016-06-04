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

import org.dmp.module.admin.reportmgr.dao.ReportMgrEventDao;
import org.dmp.pojo.admin.reportmgr.ReportEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service("ReportMgrEventService")
@Scope("prototype")
public class ReportMgrEventService {

	@Resource(name = "ReportMgrEventDao")
	private ReportMgrEventDao reportMgrEventDao;
	
	/**
	 * 将对象列表保存到数据库
	 * @param reportCondList
	 * @return
	 */
	public int addReportEventList(List<ReportEvent> reportEventList){
		return reportMgrEventDao.addReportEventList(reportEventList);
	}
	
	public int deleteReportEventList(List<ReportEvent> reportEventList){
		return reportMgrEventDao.deleteReportEventList(reportEventList);
	}
	
	/**
	 * 指定reportid对应的event数据库修改
	 * @param originalCond
	 * @param reportCondList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int editReportCondList(List<ReportEvent> originalEvent,List<ReportEvent> reportEventList){
		Iterator<ReportEvent> it = reportEventList.iterator();
		int nTotal = 0;
		Set<Integer> originalIdSet = new HashSet<Integer>();
		for(int i= 0; i< originalEvent.size(); i++){
			originalIdSet.add(originalEvent.get(i).getId());
		}
		while(it.hasNext())
		{
			ReportEvent reportEvent = it.next();
			if(reportEvent.getId()==0)
			{
				reportMgrEventDao.insert(reportEvent);
			}
			if(originalIdSet.contains(reportEvent.getId())){
				reportMgrEventDao.update(reportEvent);
				originalIdSet.remove(reportEvent.getId());
			}
		}
		Iterator<Integer> idIterator = originalIdSet.iterator();
		while(idIterator.hasNext()){
			int id = idIterator.next();
			reportMgrEventDao.delete(reportMgrEventDao.getById(id));
		}
		nTotal++;
		return nTotal != 0? 1 :-1;
	}
	
	public List<ReportEvent> getReportEventList(String reportId){
		return reportMgrEventDao.getReportEventList(reportId);
	}
	
	/**
	 * 将后台数据转换成reportevent对象列表
	 * @param reportEventListMap
	 * @return
	 * @throws ParseException
	 */
	public List<ReportEvent> MaptoEvent(List<Map<String, String>> reportEventListMap) throws ParseException{
		Iterator<Map<String, String>> it = reportEventListMap.iterator();
		List<ReportEvent> reportEventList = new ArrayList<ReportEvent>();
		while(it.hasNext())
		{
			Map<String, String> eventMap = it.next();
			ReportEvent reportEvent = new ReportEvent();
			reportEvent.setChartUrl(eventMap.get("chartUrl"));
			reportEvent.setEvent(eventMap.get("event"));
			reportEvent.setEventName(eventMap.get("eventName"));
			reportEvent.setId(Integer.valueOf(eventMap.get("sId")));
			reportEvent.setLayout(Integer.valueOf(eventMap.get("layout")));
			reportEvent.setReportId(eventMap.get("reportId"));
			reportEvent.setUpdateStaffId(eventMap.get("updateStaffId"));
			reportEvent.setUpdateTime(new Date());
			reportEventList.add(reportEvent);
		}
		return reportEventList;
	}
}
