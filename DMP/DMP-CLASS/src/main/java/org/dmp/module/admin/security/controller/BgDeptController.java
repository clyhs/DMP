package org.dmp.module.admin.security.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.dmp.core.util.StrUtil;
import org.dmp.core.util.Tools;
import org.dmp.module.admin.security.service.BgDeptService;
import org.dmp.module.common.form.FormResponse;
import org.dmp.module.common.tree.Tree;
import org.dmp.pojo.admin.security.BgDept;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 后台部门管理控制层
 * 
 */
@Controller
public class BgDeptController
{
	@Resource(name = "BgDeptService")
	private BgDeptService m_oBgDeptService;
	
	/**
	 * 获取后台部门列表
	 * 
	 * @param oRequest
	 * @return List<Tree>
	 */
	@RequestMapping(value = "/module/admin/security/getBgDeptList", method = RequestMethod.POST)
	@ResponseBody
	public List<Tree> getBgDeptList(HttpServletRequest oRequest)
	{
		int nBgDeptId = 1;
		if (oRequest.getParameter("node") != null)
		{
			List<String> aBgDeptId = Tools.getRegexResult(
					oRequest.getParameter("node"), "[a-zA-Z_]*([0-9]+)");
			if (aBgDeptId.size() > 0)
			{
				nBgDeptId = StrUtil.toInt(aBgDeptId.get(0), 1);
				if (nBgDeptId == 0) nBgDeptId = 1;
			}
		}
		return m_oBgDeptService.getBgDept(nBgDeptId, Boolean.FALSE);
	}
	
	/**
	 * 后台部门管理
	 * 
	 * @param oRequest
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/security/bg_dept", method = RequestMethod.GET)
	public String getBgDeptIndex(HttpServletRequest oRequest)
	{
		return "/admin/security/bg_dept";
	}
	
	/**
	 * 移到部门
	 * 
	 * @param nDestBgDeptId
	 * @param nSrcBgDeptId
	 * @return FormResponse
	 */
	@RequestMapping(value = "/module/admin/security/editMoveBgDept", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse editMoveBgDept(int nDestBgDeptId,
			int nSrcBgDeptId)
	{
		int nTotal = m_oBgDeptService.editMoveBgDept(nDestBgDeptId,
				nSrcBgDeptId);
		
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "后台部门移动成功" : "后台部门移动失败");
		return oFormResponse;
	}
	
	/**
	 * 获取部门信息
	 * 
	 * @param nBgDeptId
	 * @return BgDept
	 */
	@RequestMapping(value = "/module/admin/security/getBgDept", method = RequestMethod.POST)
	@ResponseBody
	public BgDept getBgDept(int nBgDeptId)
	{
		return m_oBgDeptService.getBgDept(nBgDeptId);
	}
	
	/**
	 * 增加一个后台部门
	 * 
	 * @param nPId
	 * @param sName
	 * @param sUrl
	 * @param sRemark
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/security/addBgDept", method = RequestMethod.POST)
	public String addBgDept(HttpServletRequest oRequest, int nPId,
			String sName, String sRemark)
	{
		System.out.println("nPid"+nPId);
		int nPKId = m_oBgDeptService.addBgDept(nPId, sName,
				sRemark);
		
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nPKId > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nPKId > 0 ? "后台部门增加成功" : "后台部门增加失败");
		oFormResponse.setParam("sName", sName);
		oFormResponse.setParam("nBgDeptId", nPKId);
		oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}
	
	/**
	 * 修改一个后台部门
	 * 
	 * @param nBgDeptId
	 * @param sName
	 * @param sUrl
	 * @param sRemark
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/security/editBgDept", method = RequestMethod.POST)
	public String editBgDept(HttpServletRequest oRequest, int nBgDeptId, String sName, String sRemark)
	{
		int nTotal = m_oBgDeptService.editBgDept(nBgDeptId, sName, sRemark);
		
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "后台部门修改成功" : "后台部门修改失败");
		oFormResponse.setParam("sName", sName);
		oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}
	
	/**
	 * 删除一个后台部门
	 * 
	 * @param nBgDeptId
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "/module/admin/security/delBgDept", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse delBgDept(int nBgDeptId)
	{
		int nTotal = m_oBgDeptService.delBgDept(nBgDeptId);
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "后台部门删除成功" : "后台部门删除失败");
		return oFormResponse;
	}
}
