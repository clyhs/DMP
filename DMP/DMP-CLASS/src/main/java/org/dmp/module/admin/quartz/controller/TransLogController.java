package org.dmp.module.admin.quartz.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.util.StrUtil;
import org.dmp.core.util.Tools;
import org.dmp.module.admin.quartz.service.TransLogService;
import org.dmp.module.common.grid.Grid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * 
 * @author killy
 *
 */

@Controller
public class TransLogController {
	
	@Resource(name = "TransLogService")
	private TransLogService transLogService;
	
	@RequestMapping(value = "/module/admin/quartz/translog")
	public String  transIndex(HttpServletRequest oRequest){
		return "/admin/quartz/translog";
	}
	
	@RequestMapping(value = "/module/admin/quartz/steplog")
	public String  stepIndex(HttpServletRequest oRequest){
		return "/admin/quartz/steplog";
	}
	
	
	/**
	 * 获取转换日志的列表
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/quartz/getTransLogList")
	@ResponseBody
	public Grid<Map<String,Object>> getTransLogList(HttpServletRequest oRequest){
		String transLogName = StrUtil.toStr(oRequest.getParameter("transLogName"), ""); 
		int start = StrUtil.toInt(oRequest.getParameter("start"), 0); 
		int offset = StrUtil.toInt(oRequest.getParameter("limit"), 100);
		
		List<DynaBean> transLogList = transLogService.getTransList(transLogName, start, offset); 
		long total = transLogService.getTransList(transLogName, 0, 0).size();
		
		Grid<Map<String,Object>> oGrid = new Grid<Map<String,Object>>();
		oGrid.setData(Tools.dynaBeanToMap(transLogList));
		oGrid.setTotal(total);
		oGrid.setStart(start);
		return oGrid;
	}

	/**
	 * 转换步骤的列表
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/quartz/getStepLogList")
	@ResponseBody
	public Grid<Map<String,Object>> getStepList(HttpServletRequest oRequest){
		String jobName = StrUtil.toStr(oRequest.getParameter("jobName"), ""); 
		int start = StrUtil.toInt(oRequest.getParameter("start"), 0); 
		int offset = StrUtil.toInt(oRequest.getParameter("limit"), 100);
		
		List<DynaBean> stepLogList = transLogService.getStepList(jobName, start, offset);
		long total = transLogService.getStepList(jobName, 0, 0).size();
		
		Grid<Map<String, Object>> oGrid = new Grid<Map<String,Object>>();
		oGrid.setData(Tools.dynaBeanToMap(stepLogList));
		oGrid.setTotal(total);
		oGrid.setStart(start);
		return oGrid;
	}
}
