package org.dmp.module.admin.security.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.dmp.core.util.StrUtil;
import org.dmp.module.admin.security.service.BgPostService;
import org.dmp.module.common.form.FormResponse;
import org.dmp.module.common.grid.Grid;
import org.dmp.pojo.admin.security.BgPost;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 后台岗位管理控制层
 */
@Controller
public class BgPostController
{
	@Resource(name = "BgPostService")
	private BgPostService m_oBgPostService;
	
	/**
	 * 岗位的主入口
	 * @param oRequest
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/security/bg_post", method = RequestMethod.GET)
	public String getBgPostIndex(HttpServletRequest oRequest)
	{
		return "/admin/security/bg_post";
	}
	
	/**
	 * 获取后台岗位列表
	 * @param oRequest
	 * @return Grid<BgPost>
	 */
	@RequestMapping(value = "/module/admin/security/getBgPostList", method = RequestMethod.POST)
	@ResponseBody
	public Grid<BgPost> getBgPostList(HttpServletRequest oRequest)
	{
		String sName = StrUtil.toStr(oRequest.getParameter("sName"), ""); 
		String sDeptName = StrUtil.toStr(oRequest.getParameter("sDeptName"), ""); 
		int nStart = StrUtil.toInt(oRequest.getParameter("start"), 0); 
		int nLimit = StrUtil.toInt(oRequest.getParameter("limit"), 100); 
		
		List<BgPost> aBgPost = m_oBgPostService.getBgPostList(sName, sDeptName, nStart, nLimit);
		Long nTotal = m_oBgPostService.getBgPostListTotal(sName, sDeptName);
		
		Grid<BgPost> oGrid = new Grid<BgPost>();
		oGrid.setTotal(nTotal);
		oGrid.setStart(nStart);
		oGrid.setData(aBgPost);
		return oGrid;
	}
	
	/**
	 * 获取一个后台岗位资料
	 * @param oRequest
	 * @return BgPost
	 */
	@RequestMapping(value = "/module/admin/security/getBgPost", method = RequestMethod.POST)
	@ResponseBody
	public BgPost getBgPost(HttpServletRequest oRequest)
	{
		Integer nBgPostId = StrUtil.toInt(oRequest.getParameter("nBgPostId"), 0);
		return m_oBgPostService.getBgPost(nBgPostId);
	}
	
	/**
	 * 增加一个后台岗位
	 * @param oRequest
	 * @param oResponse void
	 */
	@RequestMapping(value = "/module/admin/security/addBgPost", method = RequestMethod.POST)
	public String addBgPost(HttpServletRequest oRequest)
	{
		String sName = StrUtil.toStr(oRequest.getParameter("sName"), "");
		Integer nBgDeptId = StrUtil.toInt(oRequest.getParameter("nBgDeptId"), 0);
		String sRemark = StrUtil.toStr(oRequest.getParameter("sRemark"), "");
		
		int nTotal = m_oBgPostService.addBgPost(sName, nBgDeptId, sRemark);
		
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "岗位增加成功" : "岗位增加失败");
		oRequest.setAttribute("sResponse", oFormResponse);
		
		return "/admin/common/form_response";
	}
	
	/**
	 * 修改一个后台岗位
	 * @param oRequest
	 * @param oResponse void
	 */
	@RequestMapping(value = "/module/admin/security/editBgPost", method = RequestMethod.POST)
	public String editBgPost(HttpServletRequest oRequest)
	{
		Integer nBgPostId = StrUtil.toInt(oRequest.getParameter("nBgPostId"), 0);
		String sName = StrUtil.toStr(oRequest.getParameter("sName"), "");
		Integer nBgDeptId = StrUtil.toInt(oRequest.getParameter("nBgDeptId"), 0);
		String sRemark = StrUtil.toStr(oRequest.getParameter("sRemark"), "");
		
		int nTotal = m_oBgPostService.editBgPost(nBgPostId, sName, nBgDeptId, sRemark);
		
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "岗位修改成功" : "岗位修改失败");
		oRequest.setAttribute("sResponse", oFormResponse);
		
		return "/admin/common/form_response";
	}	
	
	/**
	 * 删除后台岗位
	 * @param sBgPostIdList
	 * @return FormResponse
	 */
	@RequestMapping(value = "/module/admin/security/delBgPost", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse delBgPost(String sBgPostIdList)
	{
		int nTotal = m_oBgPostService.delBgPostList(sBgPostIdList);
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "岗位删除成功" : "岗位删除失败");
		
		return oFormResponse;
	}
		
}
