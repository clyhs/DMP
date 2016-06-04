package org.dmp.module.admin.reportmgr.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.util.StrUtil;
import org.dmp.core.util.Tools;
import org.dmp.module.admin.reportmgr.service.ReportImportService;
import org.dmp.module.common.form.FormResponse;
import org.dmp.module.common.grid.Grid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



@Controller
public class ReportImportController{
	
	@Resource(name = "ReportImportService")
	private ReportImportService oReportImportService;
	
	/**
	 * 报表导入入口
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value="/module/admin/reportmgr/report_import",method = RequestMethod.GET)
	public String getReportImportIndex(HttpServletRequest oRequest){
		return "/admin/reportmgr/report_import";
	}

	/**
	 * 获取表名下拉框
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/getTableNameCombo")
	@ResponseBody
	public Grid<Map<String,Object>> getTableNameBean(HttpServletRequest oRequest){
		List<DynaBean> listModel = oReportImportService.getTableNameBean();
		Grid<Map<String, Object>> oGrid = new Grid<Map<String, Object>>();
		oGrid.setData(Tools.dynaBeanToMap(listModel));
		return oGrid;
	}
	
	//修改,删除,查询共用的List
	List<Map<String,Object>> list = null;
	
	/**
	 * 预览-excel表的内容专成list
	 * @param oRequest
	 * @param oFile
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/getExcelToList", method = RequestMethod.POST)
	public String getExcelToList(HttpServletRequest oRequest,@RequestParam("filePath") MultipartFile oFile)
	{
		 list = oReportImportService.excelToMap(oFile);
		 String headers = oReportImportService.getTableHeader(oFile);
		 FormResponse oFormResponse = new FormResponse();
		 oFormResponse.setSuccess(Boolean.TRUE);
		 oFormResponse.setMsg(headers);
		 oRequest.setAttribute("sResponse", oFormResponse);
		 return "/admin/common/form_response";
	}
	
	/**
	 * excel表的内容在GridPanel展示
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/getExcelList", method = RequestMethod.POST)
	@ResponseBody
	public Grid getExcelList(HttpServletRequest oRequest)
	{
		int nStart = StrUtil.toInt(oRequest.getParameter("start"), 0); 
		String loadData = StrUtil.toStr(oRequest.getParameter("loadData"), "");
		List<Map<String,Object>> listData =  null;
		Long total = (long)0;
		
		if(!loadData.equals("loadData")){
			list = null;
		}
		if(list!=null){
			total = (long)list.size();
			if(total-nStart>=100){
				listData =list.subList(nStart, nStart+100);
			}else{
				listData = list.subList(nStart,list.size());
			}
		}
		Grid oGrid = new Grid();
		oGrid.setTotal(total);
		oGrid.setData(listData);
		oGrid.setStart(nStart);
		listData = null;
		return oGrid;
	}
	
	/**
	 * 导入excel表预览的内容到数据库
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/importExcel",method = RequestMethod.POST)
	public String doImportExcel(HttpServletRequest oRequest) {
		String tableName  = StrUtil.toStr(oRequest.getParameter("tableName"),"");
		FormResponse res = oReportImportService.doImportExcel(tableName,list);
		if(res.getSuccess()){
			list = null;
		}
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(res.getSuccess());
		oFormResponse.setMsg(res.getMsg());
		oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}
	
	/**
	 * 删除excel表预览的内容
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/delExcel", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse delExcel(HttpServletRequest oRequest)
	{
		String ids = StrUtil.toStr(oRequest.getParameter("ids"),"");
		list = oReportImportService.delExcel(list,ids);
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(Boolean.TRUE);
		oFormResponse.setMsg("删除成功!");
		return  oFormResponse;
	}
	
	/**
	 * 修改excel表预览的内容
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/editExcel", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse editExcel(HttpServletRequest oRequest)
	{
		String id = StrUtil.toStr(oRequest.getParameter("id"),"");
		String field = StrUtil.toStr(oRequest.getParameter("field"),"");
		String changeValue = StrUtil.toStr(oRequest.getParameter("changeValue"),"");
	    list = oReportImportService.editExcel(list,id,field,changeValue);
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(Boolean.TRUE);
		oFormResponse.setMsg("修改成功!");
		return  oFormResponse;
	}
	

	/**
	 * excel模版下载
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/reportmgr/getExcelModel")
	public String getExcelModel(HttpServletRequest oRequest,HttpServletResponse oResponse){
		String  fileName = oRequest.getParameter("fileName")+".xls";
		String msg = oReportImportService.getExcelModel(oRequest,oResponse,fileName);
		FormResponse oFormResponse = new FormResponse();
		if(!msg.equals("")){
			oFormResponse.setSuccess(false);
		}else{
			oFormResponse.setSuccess(true);
		}
		oFormResponse.setMsg(msg);
        oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}
}
