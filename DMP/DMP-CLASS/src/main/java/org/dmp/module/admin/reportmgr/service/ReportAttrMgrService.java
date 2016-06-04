package org.dmp.module.admin.reportmgr.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.dmp.module.admin.reportmgr.dao.ReportAttrMgrDao;
import org.dmp.pojo.admin.reportmgr.ReportAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service("ReportAttrMgrService")
@Scope("prototype")
public class ReportAttrMgrService {

	@Resource(name = "ReportAttrMgrDao")
	private ReportAttrMgrDao reporAttrtMgrDao;
	
	public List<ReportAttr> getReportAttrList(String reportId){

		return reporAttrtMgrDao.getReportAttrList(reportId);
	}
	
	public int addReportAttrList(List<ReportAttr> reportAttrList){
		return reporAttrtMgrDao.addReportAttr(reportAttrList);
	}
	
	@SuppressWarnings("unchecked")
	public int editReportAttrList(List<ReportAttr>  originalAttr, List<ReportAttr> reportAttrList){
		Iterator<ReportAttr> it = reportAttrList.iterator();
		int nTotal = 0;
		Set<Integer> originalIdSet = new HashSet<Integer>();
		for(int i= 0; i< originalAttr.size(); i++){
			originalIdSet.add(originalAttr.get(i).getId());
		}
		while(it.hasNext())
		{
			ReportAttr reportAttr = it.next();
			if(reportAttr.getId()==0)
			{
				reporAttrtMgrDao.insert(reportAttr);
			}
			if(originalIdSet.contains(reportAttr.getId())){
				reporAttrtMgrDao.update(reportAttr);
				originalIdSet.remove(reportAttr.getId());
			}
		}
		Iterator<Integer> idIterator = originalIdSet.iterator();
		while(idIterator.hasNext()){
			int id = idIterator.next();
			reporAttrtMgrDao.delete(reporAttrtMgrDao.getById(id));
		}
		nTotal++;
		return nTotal != 0? 1 :-1;
		
	}
	
	@SuppressWarnings("unchecked")
	public int delReportAttrList(List<ReportAttr> reportAttrList){
		return reporAttrtMgrDao.delete(reportAttrList);
	}
	
	public List<ReportAttr> lMapToReportList(List<Map<String, String>> reportAttrListMap){
		Iterator<Map<String, String>> it = reportAttrListMap.iterator();
		List<ReportAttr> reportAttrList = new ArrayList<ReportAttr>();
		while(it.hasNext())
		{
			Map<String, String> map = it.next();
			ReportAttr reportAttr = new ReportAttr();
			reportAttr.setId(Integer.valueOf(map.get("id")));
			reportAttr.setDataIndex(map.get("dataIndex"));
			reportAttr.setName(map.get("name"));
			reportAttr.setReportId(map.get("reportId"));
			reportAttr.setSortable(map.get("sortable"));
			reportAttr.setOrder(Integer.valueOf(map.get("order")));
			reportAttr.setWidth(Integer.valueOf(map.get("width")));
			reportAttr.setType(map.get("type"));
			reportAttrList.add(reportAttr);
		}
		return reportAttrList;
	}
}
