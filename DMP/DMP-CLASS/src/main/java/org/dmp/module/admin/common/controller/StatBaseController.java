package org.dmp.module.admin.common.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.util.StrUtil;
import org.dmp.core.util.Tools;
import org.dmp.module.admin.common.excel.ExportingExcel;
import org.dmp.module.admin.common.service.StatBaseSevice;
import org.dmp.module.admin.navtree.service.NavTreeService;
import org.dmp.module.common.form.FormResponse;
import org.dmp.module.common.grid.DynamicGrid;
import org.dmp.module.common.grid.Grid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class StatBaseController {
	
	@Resource(name = "NavTreeService")
	private NavTreeService m_oNavTreeService;

	@Resource(name="StatBaseSevice")
	private StatBaseSevice statBaseService;
	
	
	@RequestMapping(value = "/module/admin/common/dynamicreportindex")
	public String schedulerIndex(HttpServletRequest oRequest)
	{
		return "/admin/common/dynamicreportindex";
	}
	/**
	 * 构造Toolbar
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/module/admin/common/getToolbarData", method = RequestMethod.POST)
	@ResponseBody
	public List getToolbarData(HttpServletRequest request){
		String reportId = request.getParameter("reportId");
		int treeId = StrUtil.toInt(request.getParameter("treeId"), 0);
		//int bgUserId = (Integer)(request.getSession().getAttribute("GlobalSessionBgUserId"));
		int bgUserId = 0;
		List list = statBaseService.getToolbarData(reportId,treeId,bgUserId); 
		return list;
	}
	
	/**
	 * 构造报表展示内容
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/module/admin/common/getStatReportData", method = RequestMethod.POST)
	@ResponseBody
	public DynamicGrid getStatReportData(HttpServletRequest request){
		String reportId = request.getParameter("reportId");
		int start = StrUtil.toInt(request.getParameter("start"), 0); 
		int limit = StrUtil.toInt(request.getParameter("limit"), 100);
		HashMap paraMap = getParamMap(request);
		DynamicGrid grid = statBaseService.getDynamicGrid(reportId, paraMap, start, limit);
		return grid;
	}
	
	//获取param
    protected HashMap<String, Object> getParamMap(HttpServletRequest request)
    {
    	Enumeration<String> enume = request.getParameterNames();
    	String key="";
    	String value="";
    	HashMap paraMap =  new HashMap<String,String>();
    	while(enume.hasMoreElements()){
    		key = enume.nextElement();
    		value = StrUtil.toQueryStr(request.getParameter(key),"%");
    		paraMap.put(key, value);
    	}
    	return paraMap;
	}
    
    /**
     * 报表数据导出
     * @param oRequest
     * @return
     */
    @RequestMapping(value = "/module/admin/common/doDataExport", method = RequestMethod.POST)
    public ExportingExcel doDataExport(HttpServletRequest oRequest)
    {
    	String reportId = oRequest.getParameter("reportId");
    	HashMap<String, Object> paramMap = getParamMap(oRequest);   //获取参数的map集合
    	
		List<List> rowList = new LinkedList<List>();
		rowList.add(statBaseService.getReportAttr(reportId));
		List<DynaBean> targetList = statBaseService.getDataList(reportId, paramMap);
		Tools.dynaBeanToList(rowList, targetList);
		ExportingExcel excel = new ExportingExcel();
		excel.setExcelData(rowList);
		String fileName =  StrUtil.transitionEncode(oRequest.getParameter("name"));	
		excel.setFileName(fileName + StrUtil.preTimeStr(paramMap));
		return excel;
    }
    
    /**
     * 生成图表数据
     * @param oRequest
     * @return
     */
    @RequestMapping(value = "/module/admin/common/getReportChart", method = RequestMethod.POST)
   	@ResponseBody
    public Grid<Map<String, Object>>getReportChart(HttpServletRequest oRequest){
    	String reportId = oRequest.getParameter("reportId");
		List<DynaBean> chartList = statBaseService.getChartDataList(reportId,getParamMap(oRequest));
		Grid<Map<String, Object>> grid = new Grid<Map<String, Object>>();
		grid.setData(Tools.dynaBeanToMap(chartList));
		return grid;
    }
    
    /**
     * 报表数据合计
     * @param oRequest
     * @return
     */
    @RequestMapping(value = "/module/admin/common/getReportDataTotal", method = RequestMethod.POST)
   	@ResponseBody
    public FormResponse getReportDataTotal(HttpServletRequest oRequest) {
    	String reportId = oRequest.getParameter("reportId");
    	if(reportId != null && reportId.length() > 0) {
    	  String sql = statBaseService.getSumSQL(reportId);
    	  List<DynaBean> listModel = statBaseService.getTotalList(getParamMap(oRequest),sql);
    	  String str = Tools.getJsonString(listModel);
          FormResponse oFormResponse = new FormResponse();
  		  oFormResponse.setSuccess(Boolean.TRUE);
  		  oFormResponse.setMsg(str);
  		return oFormResponse;
    	} else{
           return null;
        }
    }
    /**
     * 柱状图获取XML数据
     * @param oRequest
     * @return
     */
    @RequestMapping(value = "/module/admin/common/getChartXML", method = RequestMethod.POST)
   	@ResponseBody
   	public FormResponse getChartXML(HttpServletRequest oRequest){
    	String reportId = oRequest.getParameter("reportId");
    	if(reportId != null && reportId.length() > 0) {
    		List<DynaBean> listModel = statBaseService.getChartDataList(reportId,getParamMap(oRequest));
    		String str = Tools.getXMLString(listModel);
    		FormResponse oFormResponse = new FormResponse();
      		oFormResponse.setSuccess(Boolean.TRUE);
      		oFormResponse.setMsg(str);
      		return oFormResponse;
    	}else{
    		return null;
    	}
    }
 
/***********************门户访问及会员业务统计报表构造（区分全国省份维度）  shu.y*************************************************/
    /**
	 * 构造报表展示内容
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/module/admin/common/getStatReportDataForPro", method = RequestMethod.POST)
	@ResponseBody
	public DynamicGrid getStatReportDataForPro(HttpServletRequest request){
		String reportId = request.getParameter("reportId");
		int start = StrUtil.toInt(request.getParameter("start"), 0); 
		int limit = StrUtil.toInt(request.getParameter("limit"), 100);
		HashMap paraMap = getParamMap(request);
		DynamicGrid grid = statBaseService.getDynamicGridForReport(reportId, paraMap, start, limit);
		return grid;
	}
	
    
    /**
     * 报表数据导出
     * @param oRequest
     * @return
     */
    @RequestMapping(value = "/module/admin/common/doDataExportForPro", method = RequestMethod.POST)
    public ExportingExcel doDataExportForPro(HttpServletRequest oRequest)
    {
    	String reportId = oRequest.getParameter("reportId");
     	HashMap<String, Object> paramMap = getParamMap(oRequest);   //获取参数的map集合
  
		List<List> rowList = new LinkedList<List>();
		rowList.add(statBaseService.getReportAttr(reportId));
		List<DynaBean> targetList = statBaseService.getDataListForReport(reportId,paramMap);
		Tools.dynaBeanToList(rowList, targetList);
		ExportingExcel excel = new ExportingExcel();
		excel.setExcelData(rowList);
		String fileName =  StrUtil.transitionEncode(oRequest.getParameter("name"));	
		excel.setFileName(fileName + StrUtil.preTimeStr(paramMap));
		return excel;
    }
    
    /**
     * 生成图表数据
     * @param oRequest
     * @return
     */
    @RequestMapping(value = "/module/admin/common/getReportChartForPro", method = RequestMethod.POST)
   	@ResponseBody
    public Grid<Map<String, Object>>getReportChartForPro(HttpServletRequest oRequest){
    	String reportId = oRequest.getParameter("reportId");
		List<DynaBean> chartList = statBaseService.getChartDataListForReport(reportId,getParamMap(oRequest));
		Grid<Map<String, Object>> grid = new Grid<Map<String, Object>>();
		grid.setData(Tools.dynaBeanToMap(chartList));
		return grid;
    }
    
    /**
     * 报表数据合计
     * @param oRequest
     * @return
     */
    @RequestMapping(value = "/module/admin/common/getReportDataTotalForPro", method = RequestMethod.POST)
   	@ResponseBody
    public FormResponse getReportDataTotalForPro(HttpServletRequest oRequest) {
    	String reportId = oRequest.getParameter("reportId");
    	if(reportId != null && reportId.length() > 0) {
    	  String sql = statBaseService.getSumSQL(reportId);
    	  List<DynaBean> listModel = statBaseService.getTotalListForReport(getParamMap(oRequest),sql);
    	  String str = Tools.getJsonString(listModel);
          FormResponse oFormResponse = new FormResponse();
  		  oFormResponse.setSuccess(Boolean.TRUE);
  		  oFormResponse.setMsg(str);
  		return oFormResponse;
    	} else{
           return null;
        }
    }
}
