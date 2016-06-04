package org.dmp.module.admin.reportmgr.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.dmp.module.admin.reportmgr.dao.ReportQuotaDao;
import org.dmp.pojo.admin.reportmgr.ReportQuota;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service("ReportQuotaService")
@Scope("prototype")
public class ReportQuotaService {
	
	@Resource(name = "ReportQuotaDao")
	private ReportQuotaDao reportQuotaDao;
	
	/**
	 * 获取报表指标配置列表
	 * @param reportTable
	 * @return
	 */
	public List<ReportQuota> getreportQuotalist(String reportTable, Boolean tableconstruct){
		return reportQuotaDao.getreportQuotalist(reportTable ,tableconstruct);
	}
	
	/**
	 * 将前台传过来的grid列表转换为目标数据
	 * @param reportQuotaListMap
	 * @return
	 * @throws ParseException
	 */
	public List<ReportQuota> MaptoQuota(List<Map<String, String>> reportQuotaListMap) throws ParseException{
		Iterator<Map<String, String>> it = reportQuotaListMap.iterator();
		List<ReportQuota> reportQuotaList = new ArrayList<ReportQuota>();
		while(it.hasNext())
		{
			Map<String, String> oReportMap = it.next();
			ReportQuota reportQuota = new ReportQuota();
			reportQuota.setsId(Integer.valueOf(oReportMap.get("sId")));
			reportQuota.setReportTable(oReportMap.get("reportTable"));
			reportQuota.setColumnName(oReportMap.get("columnName"));
			reportQuota.setMinValue(Long.valueOf(oReportMap.get("minValue")));
			reportQuota.setMaxValue(Long.valueOf(oReportMap.get("maxValue")));
			reportQuota.setConfigType(oReportMap.get("configType"));
			reportQuotaList.add(reportQuota);
		}
		return reportQuotaList;
	}
	
	public int editReportQuotaList(List<ReportQuota> originalQuotaList, List<ReportQuota> reportQuotaList){
		Iterator<ReportQuota> it = reportQuotaList.iterator();
		int nTotal = 0;
		Set<Integer> originalIdSet = new HashSet<Integer>();
		for(int i= 0; i< originalQuotaList.size(); i++){
			originalIdSet.add(originalQuotaList.get(i).getsId());
		}
		while(it.hasNext())
		{
			ReportQuota reportQuota = it.next();
			if(originalIdSet.contains(reportQuota.getsId())){
				reportQuotaDao.update(reportQuota);
				originalIdSet.remove(reportQuota.getsId());
			}
			if(reportQuota.getsId()==0)
			{
				reportQuotaDao.insert(reportQuota);
			}
		}
		Iterator<Integer> idIterator = originalIdSet.iterator();
		while(idIterator.hasNext()){
			int id = idIterator.next();
			reportQuotaDao.delete(reportQuotaDao.getById(id));
		}
		nTotal++;
		return nTotal != 0? 1 :-1;
	}

}
