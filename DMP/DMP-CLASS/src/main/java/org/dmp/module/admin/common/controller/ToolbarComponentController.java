package org.dmp.module.admin.common.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.util.StrUtil;
import org.dmp.core.util.Tools;
import org.dmp.module.admin.common.service.ToolbarComponentService;
import org.dmp.module.common.combobox.ComboBox;
import org.dmp.module.common.grid.Grid;
import org.dmp.pojo.admin.common.PortalType;
import org.dmp.pojo.admin.common.YMType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
public class ToolbarComponentController {
	
	@Resource(name = "ToolbarComponentService")
	private ToolbarComponentService toolbarComponentService;
	/**
	 * 获取动态下拉框的值	
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/common/getComboBox")
	@ResponseBody
	public Grid<Map<String, Object>> getComboBox(HttpServletRequest oRequest){
		String reportId = StrUtil.toStr(oRequest.getParameter("reportId"),"");
		String componentId = StrUtil.toStr(oRequest.getParameter("componentId"),"");
		List<DynaBean> listModel = toolbarComponentService.getComboBoxBean(reportId,componentId);
		Grid<Map<String, Object>> oGrid = new Grid<Map<String, Object>>();
		oGrid.setData(Tools.dynaBeanToMap(listModel));
		return oGrid;
	}
	
	
	
	/**
	 * 可单选或多选的年月控件
	 * @param request
	 * @return ComboBox<Station>
	 */
	@RequestMapping(value = "/module/admin/common/getYMComboBox")
	@ResponseBody
	public ComboBox<YMType> getYMComboBox(HttpServletRequest request){
		int ymRange = StrUtil.toInt(request.getParameter("ymRange"), 12);
		List<YMType> ymTypeList = toolbarComponentService.getYMComboBoxList(ymRange);
		ComboBox<YMType> oComboBox = new ComboBox<YMType>();
		YMType[] ymType = new YMType[ymTypeList.size()];
		oComboBox.setArr(ymTypeList.toArray(ymType));
		return oComboBox;
	}
	
	
	
}
