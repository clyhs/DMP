package org.dmp.module.admin.common.service;


import java.util.List;

import javax.annotation.Resource;

import org.dmp.module.admin.common.dao.ReportAttrDao;
import org.dmp.pojo.admin.reportmgr.ReportAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("ReportAttrService")
@Scope("prototype")
public class ReportAttrService {

	@Resource(name="ReportAttrDao")
	private ReportAttrDao rad;
	
	
	public List<ReportAttr> getReportAttr(String reportId){
		return rad.getReportAttr(reportId);
	}
}
