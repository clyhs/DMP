package org.dmp.module.admin.main.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.dmp.core.util.StrUtil;
import org.dmp.module.admin.main.service.MainService;
import org.dmp.pojo.admin.navtree.BgTree;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
public class MainController {
	
	@Resource(name = "MainService")
	private MainService mainService;
	
	@RequestMapping(value = "/module/admin/main/index", method = RequestMethod.GET)
	public String getIndex(){
		
		return "/admin/main/index";
	}

	/**
	 * 获取一级菜单
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/main/getMainTab", method = RequestMethod.POST)
	@ResponseBody
	public List<BgTree> getMainTab(HttpServletRequest oRequest)
	{
		//Integer bgUserId = (Integer)(oRequest.getSession().getAttribute("GlobalSessionBgUserId"));
		List<BgTree> tree = mainService.getMainTab();
		return tree;
	}
	
	/**
	 * 获取二级菜单
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/main/getSecondaryMenu", method = RequestMethod.POST)
	@ResponseBody
	public List<BgTree> getSecondaryMenu(HttpServletRequest oRequest)
	{
		String nodeName = oRequest.getParameter("nodeName");
		String nodeNameStr = StrUtil.getEncoding(nodeName);
		try {
			if(nodeNameStr.equals("ISO-8859-1")){
				nodeName = new String(nodeName.getBytes("ISO-8859-1"), "UTF-8");
			}
			if(nodeNameStr.equals("GBK")){
				nodeName = new String(nodeName.getBytes("GBK"), "UTF-8");
			}	
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
			
		
		//Integer bgUserId = (Integer)(oRequest.getSession().getAttribute("GlobalSessionBgUserId"));
		List<BgTree> tree = mainService.getSecondaryMenu(nodeName);
		return tree;
	}
	
}
