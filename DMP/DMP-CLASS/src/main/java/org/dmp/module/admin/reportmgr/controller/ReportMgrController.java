package org.dmp.module.admin.reportmgr.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.dmp.core.util.JsonUtil;
import org.dmp.core.util.StrUtil;
import org.dmp.module.admin.reportmgr.service.ReportAttrMgrService;
import org.dmp.module.admin.reportmgr.service.ReportConditionService;
import org.dmp.module.admin.reportmgr.service.ReportDictService;
import org.dmp.module.admin.reportmgr.service.ReportMgrCondService;
import org.dmp.module.admin.reportmgr.service.ReportMgrEventService;
import org.dmp.module.admin.reportmgr.service.ReportMgrService;
import org.dmp.module.admin.reportmgr.service.ReportQuotaService;
import org.dmp.module.common.form.FormResponse;
import org.dmp.module.common.grid.Grid;
import org.dmp.pojo.admin.reportmgr.Report;
import org.dmp.pojo.admin.reportmgr.ReportAttr;
import org.dmp.pojo.admin.reportmgr.ReportButton;
import org.dmp.pojo.admin.reportmgr.ReportCond;
import org.dmp.pojo.admin.reportmgr.ReportCondition;
import org.dmp.pojo.admin.reportmgr.ReportDict;
import org.dmp.pojo.admin.reportmgr.ReportEvent;
import org.dmp.pojo.admin.reportmgr.ReportQuota;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class ReportMgrController {

	@Resource(name = "ReportMgrService")
	private ReportMgrService reportMgrService;

	@Resource(name = "ReportAttrMgrService")
	private ReportAttrMgrService reportAttrMgrService;

	@Resource(name = "ReportMgrCondService")
	private ReportMgrCondService reportMgrCondService;

	@Resource(name = "ReportMgrEventService")
	private ReportMgrEventService reportMgrEventService;

	@Resource(name = "ReportQuotaService")
	private ReportQuotaService reportQuotaService;

	@Resource(name = "ReportConditionService")
	private ReportConditionService reportConditionService;

	@Resource(name = "ReportDictService")
	private ReportDictService reportDictService;

	/**
	 * 入口(报表维护)
	 * 
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/reportmgrindex")
	public String reportMgrIndex(HttpServletRequest oRequest) {
		return "/admin/reportmgr/report_mgr";
	}

	/**
	 * 获取report的列表信息
	 * 
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/getreportlist", method = RequestMethod.POST)
	@ResponseBody
	public Grid<Report> getReportList(HttpServletRequest oRequest) {
		String reportId = StrUtil.toQueryStr(oRequest.getParameter("reportId"),
				"%");
		String reportName = StrUtil.toQueryStr(
				oRequest.getParameter("reportName"), "%");
		Grid<Report> grid = new Grid<Report>();
		List<Report> reportList = reportMgrService.getReportList(reportId,
				reportName);
		grid.setData(reportList);
		return grid;
	}

	/**
	 * 获取reportattr列表
	 * 
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/getreportAttrlist", method = RequestMethod.POST)
	@ResponseBody
	public Grid<ReportAttr> getReportAttrList(HttpServletRequest oRequest) {
		String reportId = StrUtil.toStr(oRequest.getParameter("reportId"), "");
		Grid<ReportAttr> grid = new Grid<ReportAttr>();
		Report report = reportMgrService.getReport(reportId);
		List<ReportAttr> reportAttrList = reportAttrMgrService
				.getReportAttrList(reportId);
		grid.setData(reportAttrList);
		grid.setParam("report", report);
		return grid;
	}

	/**
	 * 获取所有的查询组件控件的列表信息
	 * 
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/getComboGroup", method = RequestMethod.POST)
	@ResponseBody
	public Grid<ReportCondition> getComboGroup(HttpServletRequest oRequest) {
		Grid<ReportCondition> grid = new Grid<ReportCondition>();
		List<ReportCondition> reportConditionGroup = reportMgrService
				.getConditionGroup();
		List<ReportButton> reportButtonGroup = reportMgrService
				.getButtonGroup();
		grid.setData(reportConditionGroup);
		grid.setParam("event", reportButtonGroup);
		grid.setTotal((long) reportConditionGroup.size());
		return grid;
	}

	/**
	 * 添加report和reportattr到数据库
	 * 
	 * @param oRequest
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/module/admin/reportmgr/addReport", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse addReport(HttpServletRequest oRequest)
			throws ParseException {
		String report = oRequest.getParameter("report");
		String reportAttr = oRequest.getParameter("reportAttr");
		JsonUtil jsonUtil = new JsonUtil();
		Map<String, String> oReportMap = (HashMap<String, String>) jsonUtil
				.decode(report);
		List<Map<String, String>> reportAttrMap = (List<Map<String, String>>) jsonUtil
				.decode(reportAttr);

		Report oReport = reportMgrService.MaptoReport(oReportMap);
		List<ReportAttr> reportAttrList = reportAttrMgrService
				.lMapToReportList(reportAttrMap);
		int a = reportMgrService.addReport(oReport);
		int b = reportAttrMgrService.addReportAttrList(reportAttrList);

		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(a > 0 && b > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setParam("result", "添加");
		oFormResponse.setMsg(a > 0 && b > 0 ? "报表添加成功" : "报表添加失败");

		return oFormResponse;
	}

	/**
	 * 编辑report和reportattr信息
	 * 
	 * @param oRequest
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/module/admin/reportmgr/editReport", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse editReport(HttpServletRequest oRequest)
			throws ParseException {
		String report = oRequest.getParameter("report");
		String reportAttr = oRequest.getParameter("reportAttr");
		JsonUtil jsonUtil = new JsonUtil();
		Map<String, String> oReportMap = (HashMap<String, String>) jsonUtil
				.decode(report);
		List<Map<String, String>> reportAttrMap = (List<Map<String, String>>) jsonUtil
				.decode(reportAttr);

		Report oReport = reportMgrService.MaptoReport(oReportMap);
		List<ReportAttr> reportAttrList = reportAttrMgrService
				.lMapToReportList(reportAttrMap);
		int a = reportMgrService.editReport(oReport);
		List<ReportAttr> originalAttr = reportAttrMgrService
				.getReportAttrList(oReport.getReportId());
		int b = reportAttrMgrService.editReportAttrList(originalAttr,
				reportAttrList);

		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(a > 0 && b > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setParam("result", "修改");
		oFormResponse.setMsg(a > 0 && b > 0 ? "报表修改成功" : "报表修改失败");

		return oFormResponse;
	}

	/**
	 * 删除report信息
	 * 
	 * @param oRequest
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/module/admin/reportmgr/deleteReport", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse deleteReport(HttpServletRequest oRequest) {
		String report = oRequest.getParameter("reportIdArrays");
		JsonUtil jsonUtil = new JsonUtil();
		List<String> reportIdArrays = (List<String>) jsonUtil.decode(report);
		Iterator<String> it = reportIdArrays.iterator();
		List<Report> reportList = new ArrayList<Report>();
		while (it.hasNext()) {
			reportList.add(reportMgrService.getReport(it.next()));
		}
		List<ReportAttr> reportAttrList = new ArrayList<ReportAttr>();
		Iterator<String> it2 = reportIdArrays.iterator();
		while (it2.hasNext()) {
			reportAttrList.addAll(reportAttrMgrService.getReportAttrList(it2
					.next()));
		}
		List<ReportCond> reportCondList = new ArrayList<ReportCond>();
		Iterator<String> it3 = reportIdArrays.iterator();
		while (it3.hasNext()) {
			reportCondList.addAll(reportMgrCondService.getReportCondList(it3
					.next()));
		}
		List<ReportEvent> reportEventList = new ArrayList<ReportEvent>();
		Iterator<String> it4 = reportIdArrays.iterator();
		while (it4.hasNext()) {
			reportEventList.addAll(reportMgrEventService.getReportEventList(it4
					.next()));
		}
		int a = reportMgrService.delReportList(reportList);
		int b = reportAttrMgrService.delReportAttrList(reportAttrList);
		reportMgrCondService.deleteReportCondList(reportCondList);
		reportMgrEventService.deleteReportEventList(reportEventList);

		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(a > 0 && b > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(a > 0 && b > 0 ? "报表删除成功" : "报表删除失败");

		return oFormResponse;
	}

	/**
	 * 检查报表id的数据库存在情况
	 * 
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/checkreportexit", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse checkreportexit(HttpServletRequest oRequest) {
		String reportId = oRequest.getParameter("reportId");
		Report report = reportMgrService.getReport(reportId);

		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(report != null ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(report != null ? "报表id已经存在,是否清空" : "报表id不存在");

		return oFormResponse;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/module/admin/reportmgr/addComboAndEvent", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse addComboAndEvent(HttpServletRequest oRequest)
			throws ParseException {
		String reportCondition = oRequest.getParameter("reportCondition");
		String reportButton = oRequest.getParameter("reportButton");
		JsonUtil jsonUtil = new JsonUtil();
		List<Map<String, String>> reportCondMap = (List<Map<String, String>>) jsonUtil
				.decode(reportCondition);
		List<Map<String, String>> reportEventMap = (List<Map<String, String>>) jsonUtil
				.decode(reportButton);

		List<ReportCond> reportCondList = reportMgrCondService
				.MaptoCond(reportCondMap);
		List<ReportEvent> reportEventList = reportMgrEventService
				.MaptoEvent(reportEventMap);
		List<ReportCond> originalCond = reportMgrCondService
				.getReportCondList(reportCondMap.get(0).get("reportId"));
		int a = reportMgrCondService.editReportCondList(originalCond,
				reportCondList);
		List<ReportEvent> originalEvent = reportMgrEventService
				.getReportEventList(reportCondMap.get(0).get("reportId"));
		int b = reportMgrEventService.editReportCondList(originalEvent,
				reportEventList);

		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(a > 0 && b > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setParam("result", "添加");
		oFormResponse.setMsg(a > 0 && b > 0 ? "查询配置成功" : "查询配置失败");

		return oFormResponse;
	}

	/**
	 * 修改toolbar时，获取后台数据
	 * 
	 * @param oRequest
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/module/admin/reportmgr/getreportGroupData", method = RequestMethod.POST)
	@ResponseBody
	public Grid<ReportCond> getreportGroupData(HttpServletRequest oRequest)
			throws ParseException {
		String reportId = oRequest.getParameter("reportId");
		List<ReportCond> reportCondList = reportMgrCondService
				.getReportCondList(reportId);
		List<ReportEvent> reportEventList = reportMgrEventService
				.getReportEventList(reportId);
		Grid<ReportCond> oGrid = new Grid<ReportCond>();
		oGrid.setData(reportCondList);
		oGrid.setParam("event", reportEventList);
		return oGrid;
	}

	/**
	 * 获取报表指标配置的界面信息
	 * 
	 * @param oRequest
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/module/admin/reportmgr/getreportQuotalist", method = RequestMethod.POST)
	@ResponseBody
	public Grid<ReportQuota> getreportQuotaList(HttpServletRequest oRequest)
			throws ParseException {
		String reportTable = oRequest.getParameter("reportTable");
		List<ReportQuota> reportQuotaList = reportQuotaService
				.getreportQuotalist(reportTable, true);
		Grid<ReportQuota> oGrid = new Grid<ReportQuota>();
		oGrid.setData(reportQuotaList);
		return oGrid;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/module/admin/reportmgr/addOrUpdateReportQuota", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse addOrUpdateReportQuota(HttpServletRequest oRequest)
			throws ParseException {
		String reportQuotaArray = oRequest.getParameter("reportQuotaArray");
		JsonUtil jsonUtil = new JsonUtil();
		List<Map<String, String>> reportQuotaMap = (List<Map<String, String>>) jsonUtil
				.decode(reportQuotaArray);

		List<ReportQuota> reportQuotaList = reportQuotaService
				.MaptoQuota(reportQuotaMap);
		List<ReportQuota> originalQuotaList = reportQuotaService
				.getreportQuotalist(reportQuotaMap.get(0).get("reportTable"),
						false);
		int editResult = reportQuotaService.editReportQuotaList(
				originalQuotaList, reportQuotaList);

		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(editResult > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(editResult > 0 ? "报表指标配置成功" : "查询配置失败");

		return oFormResponse;
	}

	/**
	 * 报表查询条件维护
	 * 
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/queryindex")
	public String queryIndex(HttpServletRequest oRequest) {
		return "/admin/reportmgr/query";
	}

	/**
	 * 获取查询条件的列表信息
	 * 
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/getquerylist", method = RequestMethod.POST)
	@ResponseBody
	public Grid<ReportCondition> getQueryList(HttpServletRequest oRequest) {
		String reportId = StrUtil.toQueryStr(oRequest.getParameter("queryId"),
				"%");
		String reportName = StrUtil.toQueryStr(
				oRequest.getParameter("queryName"), "%");
		Grid<ReportCondition> grid = new Grid<ReportCondition>();
		List<ReportCondition> reportList = reportConditionService
				.getReportConditionList(reportId, reportName);
		grid.setData(reportList);
		return grid;
	}

	/**
	 * 添加reportCondition和dictionary到数据库
	 * 
	 * @param oRequest
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/module/admin/reportmgr/addReportCondition", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse addReportCondition(HttpServletRequest oRequest)
			throws ParseException {
		String report = oRequest.getParameter("report");
		String reportAttr = oRequest.getParameter("reportAttr");
		JsonUtil jsonUtil = new JsonUtil();
		Map<String, String> reportConditionMap = (HashMap<String, String>) jsonUtil
				.decode(report);
		List<Map<String, String>> dictionaryMap = (List<Map<String, String>>) jsonUtil
				.decode(reportAttr);

		ReportCondition reportCondition = reportConditionService
				.MaptoMaptoReportCondition(reportConditionMap);
		List<ReportDict> dictionaryList = reportDictService.lMapToReportDictList(dictionaryMap);
		int a = reportConditionService.addReportCondition(reportCondition);
		int b = 0;
		if (dictionaryList.size() == 0) {
			b = 1;
		} else {
			b = reportDictService.addReportDictList(dictionaryList);
		}

		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(a > 0 && b > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setParam("result", "添加");
		oFormResponse.setMsg(a > 0 && b > 0 ? "控件添加成功" : "控件添加失败");

		return oFormResponse;
	}

	/**
	 * 编辑report和reportattr信息
	 * 
	 * @param oRequest
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/module/admin/reportmgr/editReportCondition", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse editReportCondition(HttpServletRequest oRequest)
			throws ParseException {
		String report = oRequest.getParameter("report");
		String reportAttr = oRequest.getParameter("reportAttr");
		String oldComboxField = oRequest.getParameter("oldComboxField");
		JsonUtil jsonUtil = new JsonUtil();
		Map<String, String> reportConditionMap = (HashMap<String, String>) jsonUtil
				.decode(report);
		List<Map<String, String>> dictionaryMap = (List<Map<String, String>>) jsonUtil
				.decode(reportAttr);

		ReportCondition oReport = reportConditionService
				.MaptoMaptoReportCondition(reportConditionMap);
		List<ReportDict> reportAttrList = reportDictService
				.lMapToReportDictList(dictionaryMap);
		int a = reportConditionService.editReportCondition(oReport);
		List<ReportDict> originalAttr = reportDictService.getReportDictList(oldComboxField);
		int b = reportDictService.editReportAttrList(originalAttr,
				reportAttrList);

		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(a > 0 && b > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setParam("result", "修改");
		oFormResponse.setMsg(a > 0 && b > 0 ? "控件修改成功" : "控件修改失败");

		return oFormResponse;
	}

	/**
	 * 获取reportattr列表
	 * 
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/getdictionarylist", method = RequestMethod.POST)
	@ResponseBody
	public Grid<ReportDict> getDictionaryList(HttpServletRequest oRequest) {
		String reportId = StrUtil.toStr(oRequest.getParameter("id"), "");
		Grid<ReportDict> grid = new Grid<ReportDict>();
		ReportCondition reportCondition = reportConditionService
				.getReportCondition(Integer.parseInt(reportId));
		List<ReportDict> reportAttrList = null;
		if (reportCondition != null) {
			reportAttrList = reportDictService
					.getReportDictList(reportCondition.getComboxField());
		}
		grid.setData(reportAttrList);
		grid.setParam("report", reportCondition);
		return grid;
	}

	/**
	 * 检查控件id的数据库存在情况
	 * 
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/checkreportconditionexit", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse checkreportconditionexit(HttpServletRequest oRequest) {
		String componentId =StrUtil.trim(oRequest.getParameter("componentId")) ;
		ReportCondition reportCondition = reportConditionService
				.getReportConditionByComponentId(componentId);
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(reportCondition != null ? Boolean.TRUE
				: Boolean.FALSE);
		oFormResponse.setMsg(reportCondition != null ? "控件id已经存在,是否清空"
				: "控件id不存在");

		return oFormResponse;
	}
	
	
	/**
	 * 检查控件id的数据库存在情况
	 * 
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/checkbynameexit", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse checkbynameexit(HttpServletRequest oRequest) {
		String conditionName =StrUtil.trim(oRequest.getParameter("conditionName")) ;
		ReportCondition reportCondition = reportConditionService
				.getReportConditionByConditionName(conditionName);
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(reportCondition != null ? Boolean.TRUE
				: Boolean.FALSE);
		oFormResponse.setMsg(reportCondition != null ? "控件名称已经存在,是否清空"
				: "控件名称不存在");

		return oFormResponse;
	}

	/**
	 * 删除report信息
	 * 
	 * @param oRequest
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/module/admin/reportmgr/deleteReportCondition", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse deleteReportCondition(HttpServletRequest oRequest) {
		String report = oRequest.getParameter("reportIdArrays");
		JsonUtil jsonUtil = new JsonUtil();
		List<Integer> reportIdArrays = (List<Integer>) jsonUtil.decode(report);
		Iterator<Integer> it = reportIdArrays.iterator();
		List<ReportCondition> reportList = new ArrayList<ReportCondition>();
		while (it.hasNext()) {
			reportList
					.add(reportConditionService.getReportCondition(it.next()));
		}
		List<ReportDict> dictionaryList = new ArrayList<ReportDict>();
		Iterator<ReportCondition> it2 = reportList.iterator();
		while (it2.hasNext()) {
			ReportCondition reportCondition = it2.next();
			if (reportCondition != null) {
				String comboxFiled = reportCondition.getComboxField();
				if (comboxFiled != null && !"".equals(comboxFiled)) {
					dictionaryList
							.addAll(reportDictService
									.getReportDictList(reportCondition
											.getComboxField()));
				}
			}
		}

		int a = reportConditionService.delReportConditionList(reportList);

		int b = 0;
		if (dictionaryList.size() == 0) {
			b = 1;
		} else {
			b = reportDictService.delReportDictList(dictionaryList);
		}
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(a > 0 && b > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(a > 0 && b > 0 ? "控件删除成功" : "控件删除失败");

		return oFormResponse;
	}

	/**
	 * 修改toolbar时，获取后台数据
	 * 
	 * @param oRequest
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/module/admin/reportmgr/getReportConditionGroupData", method = RequestMethod.POST)
	@ResponseBody
	public Grid<ReportCond> getReportConditionGroupData(
			HttpServletRequest oRequest) throws ParseException {
		String reportId = oRequest.getParameter("reportId");
		List<ReportCond> reportCondList = reportMgrCondService
				.getReportCondList(reportId);
		List<ReportEvent> reportEventList = reportMgrEventService
				.getReportEventList(reportId);
		Grid<ReportCond> oGrid = new Grid<ReportCond>();
		oGrid.setData(reportCondList);
		oGrid.setParam("event", reportEventList);
		return oGrid;
	}
}
