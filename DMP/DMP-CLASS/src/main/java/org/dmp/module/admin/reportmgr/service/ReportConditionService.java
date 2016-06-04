package org.dmp.module.admin.reportmgr.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dmp.core.util.StrUtil;
import org.dmp.module.admin.reportmgr.dao.ReportConditionDao;
import org.dmp.pojo.admin.reportmgr.ReportButton;
import org.dmp.pojo.admin.reportmgr.ReportCondition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service("ReportConditionService")
@Scope("prototype")
public class ReportConditionService {

	@Resource(name = "ReportConditionDao")
	private ReportConditionDao reportConditionDao;

	public List<ReportCondition> getReportConditionList(String reportId,
			String reportName) {

		return reportConditionDao.getReportConditionList(reportId, reportName);
	}

	public ReportCondition getReportCondition(int reportId) {
		return reportConditionDao.getReportCondition(reportId);
	}
	
	public ReportCondition getReportConditionByComponentId(String componentId) {
		return reportConditionDao.getReportConditionByCId(componentId);
	}

	public int addReportCondition(ReportCondition report) {
		return reportConditionDao.addReportCondition(report);
	}

	public int editReportCondition(ReportCondition report) {
		return reportConditionDao.updateRport(report);
	}

	@SuppressWarnings("unchecked")
	public int delReportCondition(ReportCondition report) {
		return reportConditionDao.delete(report);
	}

	@SuppressWarnings("unchecked")
	public int delReportConditionList(List<ReportCondition> reportList) {
		return reportConditionDao.delete(reportList);
	}

	public List<ReportCondition> getConditionGroup() {
		return reportConditionDao.getConditionGroup();
	}

	public List<ReportButton> getButtonGroup() {
		return reportConditionDao.getButtonGroup();
	}

	/**
	 * 将前台传入数据转换成report对象
	 * 
	 * @param oReportMap
	 * @return
	 * @throws ParseException
	 */
	public ReportCondition MaptoMaptoReportCondition(
			Map<String, String> oReportMap) throws ParseException {
		ReportCondition reportCondition = new ReportCondition();
		String layout = StrUtil.toQueryStr(oReportMap.get("layout"), "0");
		String id = oReportMap.get("id");
		if (id == null || "".equals(id)) {
			reportCondition.setId(null);
		}else{
			reportCondition.setId(Integer.parseInt(id));
		}
		reportCondition.setComponentId(StrUtil.trim(oReportMap.get("componentId")));
		reportCondition.setConditionName(StrUtil.trim(oReportMap.get("conditionName")));
		reportCondition.setComponentType(oReportMap.get("componentType"));
		reportCondition.setFormat(StrUtil.trim(oReportMap.get("format")));
		reportCondition.setLayout(Long.parseLong(layout));
		reportCondition.setComboxField(StrUtil.trim(oReportMap.get("comboxField")));
		return reportCondition;
	}

	public ReportCondition getReportConditionByConditionName(
			String conditionName) {
		return reportConditionDao.getReportConditionByConditionName(conditionName);
	}

}
