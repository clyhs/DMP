package org.dmp.module.admin.reportmgr.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dmp.module.admin.reportmgr.dao.ReportMgrDao;
import org.dmp.pojo.admin.reportmgr.Report;
import org.dmp.pojo.admin.reportmgr.ReportButton;
import org.dmp.pojo.admin.reportmgr.ReportCondition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("ReportMgrService")
@Scope("prototype")
public class ReportMgrService {

	@Resource(name = "ReportMgrDao")
	private ReportMgrDao reportMgrDao;
	
	public List<Report> getReportList(String reportId, String reportName){

		return reportMgrDao.getReportList(reportId, reportName);
	}
	
	public Report getReport(String reportId){
		return reportMgrDao.getReport(reportId);
	}
	
	public int addReport(Report report){
		return reportMgrDao.addReport(report);
	}
	
	public int editReport(Report report){
		return reportMgrDao.updateRport(report);
	}
	
	@SuppressWarnings("unchecked")
	public int delReport(Report report){
		return reportMgrDao.delete(report);
	}
	
	@SuppressWarnings("unchecked")
	public int delReportList(List<Report> reportList){
		return reportMgrDao.delete(reportList);
	}
	
	public List<ReportCondition> getConditionGroup(){
		return reportMgrDao.getConditionGroup();
	}
	
	public List<ReportButton> getButtonGroup(){
		return reportMgrDao.getButtonGroup();
	}
	
	/**
	 * 将前台传入数据转换成report对象
	 * @param oReportMap
	 * @return
	 * @throws ParseException
	 */
	public Report MaptoReport(Map<String,String> oReportMap) throws ParseException{
		Report report = new Report();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date updateTime = sdf.parse(oReportMap.get("updateTime"));
        report.setReportId(oReportMap.get("reportId"));
        report.setReportName(oReportMap.get("reportName"));
        report.setUpdateStaff(oReportMap.get("updateStaff"));
        report.setUpdateTime(updateTime);
        report.setReportTable(oReportMap.get("reportTable"));
        report.setSql(oReportMap.get("sql"));
        report.setTotalSql(oReportMap.get("totalSql"));
        report.setChartSql(oReportMap.get("chartSql"));
        report.setSumSql(oReportMap.get("sumSql"));
		return report;
	}
	
}
