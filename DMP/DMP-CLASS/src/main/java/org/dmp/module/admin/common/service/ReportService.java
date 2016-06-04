package org.dmp.module.admin.common.service;

import javax.annotation.Resource;

import org.dmp.module.admin.common.dao.ReportDao;
import org.dmp.pojo.admin.reportmgr.Report;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service("ReportService")
@Scope("prototype")
public class ReportService {

	@Resource(name="ReportDao")
	private ReportDao rd;
	
	public Report getReport(String reportId){
		return rd.getReport(reportId);
	}
}
