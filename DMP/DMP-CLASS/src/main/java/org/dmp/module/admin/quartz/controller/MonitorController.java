package org.dmp.module.admin.quartz.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.util.StrUtil;
import org.dmp.core.util.Tools;
import org.dmp.module.admin.quartz.service.IMonitorService;
import org.dmp.module.common.grid.Grid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MonitorController {

	@Resource(name = "MonitorService")
	private IMonitorService monitorService;
	
	@RequestMapping(value = "/module/admin/quartz/monitor")
	public String monitorIndex(HttpServletRequest oRequest)
	{
		return "/admin/quartz/monitor";
	}
	
	@RequestMapping(value = "/module/admin/quartz/monitordata")
	public String monitordataIndex(HttpServletRequest oRequest)
	{
		return "/admin/quartz/monitordata";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/module/admin/quartz/getMonitorList", method = RequestMethod.POST)
	@ResponseBody
	public Grid<Map<String,Object>> getMonitorList(HttpServletRequest oRequest)
	{
		List<DynaBean> monitorList = monitorService.getMonitors();
		Grid<Map<String,Object>> oGrid = new Grid<Map<String,Object>>();
		oGrid.setData(Tools.dynaBeanToMap(monitorList));
		return oGrid;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/module/admin/quartz/getMonitorDataList", method = RequestMethod.POST)
	@ResponseBody
	public Grid<Map<String,Object>> getMonitorDataList(HttpServletRequest oRequest)
	{
		String jobName = StrUtil.toStr(oRequest.getParameter("currentJobName"), "");
		List monitorDataList = monitorService.getMonitorJob(jobName);
		Grid<Map<String,Object>> oGrid = new Grid<Map<String,Object>>();
		oGrid.setData(monitorDataList);
		return oGrid;
	}
}
