package org.dmp.module.admin.navtree.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.dmp.core.util.StrUtil;
import org.dmp.core.util.Tools;
import org.dmp.module.admin.navtree.service.NavTreeService;
import org.dmp.module.common.form.FormResponse;
import org.dmp.module.common.tree.Tree;
import org.dmp.pojo.admin.navtree.BgTree;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
public class NavTreeController
{
	@Resource(name = "NavTreeService")
	private NavTreeService m_oNavTreeService;
	
	/**
	 * 获取后台导航树列表
	 * 
	 * @param oRequest
	 * @return List<Tree>
	 */
	@RequestMapping(value = "/module/admin/navtree/getBgNavTree", method = RequestMethod.POST)
	@ResponseBody
	public List<Tree> getBgNavTree(HttpServletRequest oRequest)
	{
		int nBgTreeId = 1;
		if (oRequest.getParameter("node") != null)
		{
			List<String> aBgTreeId = Tools.getRegexResult(
					oRequest.getParameter("node"), "[a-zA-Z_]*([0-9]+)");
			System.out.println(aBgTreeId.size());
			if (aBgTreeId.size() > 0)
			{
				nBgTreeId = StrUtil.toInt(aBgTreeId.get(0), 1);
				if (nBgTreeId == 0) nBgTreeId = 1;
			}
		}
		return m_oNavTreeService.getBgNavTree(nBgTreeId, Boolean.FALSE);
	}
	
	/**
	 * 获取用户导航树
	 * @param oRequest
	 * @return List<Tree>
	 */
	@RequestMapping(value = "/module/admin/navtree/getMyBgNavTree", method = RequestMethod.POST)
	@ResponseBody
	public List<Tree> getMyBgNavTree(HttpServletRequest oRequest)
	{
		String nodeName = oRequest.getParameter("nodeName");
		String secondaryNodeName = oRequest.getParameter("secondaryNodeName");
		String nodeNameStr = StrUtil.getEncoding(nodeName);
		String secondaryNodeNameStr = StrUtil.getEncoding(secondaryNodeName);
			try {
				if(nodeNameStr.equals("ISO-8859-1") && secondaryNodeNameStr.equals("ISO-8859-1")){
					nodeName = new String(nodeName.getBytes("ISO-8859-1"), "UTF-8");
					secondaryNodeName = new String(secondaryNodeName.getBytes("ISO-8859-1"), "UTF-8");
				}
				if(nodeNameStr.equals("GBK") && secondaryNodeNameStr.equals("GBK")){
					nodeName = new String(nodeName.getBytes("GBK"), "UTF-8");
					secondaryNodeName = new String(secondaryNodeName.getBytes("GBK"), "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		
		
		//获取二级菜单的treeid
		Integer bgTreeId = m_oNavTreeService.getBgTreeId(nodeName);
		int nBgTreeId = bgTreeId;
		if (oRequest.getParameter("node") != null)
		{
			List<String> aBgTreeId = Tools.getRegexResult(
					oRequest.getParameter("node"), "[a-zA-Z_]*([0-9]+)");
			if (aBgTreeId.size() > 0)
			{
				nBgTreeId = StrUtil.toInt(aBgTreeId.get(0), 1);
				if (nBgTreeId == 0) nBgTreeId = bgTreeId;
			}
		}
		//Integer nBgUserId = (Integer)(oRequest.getSession().getAttribute("GlobalSessionBgUserId"));
		Integer nBgUserId = 0;
		return m_oNavTreeService.getMyBgNavTree(nBgUserId, nBgTreeId,secondaryNodeName);
	}
	
	/**
	 * 后台导航树管理
	 * 
	 * @param oRequest
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/navtree/index", method = RequestMethod.GET)
	public String getNavTreeIndex(HttpServletRequest oRequest)
	{
		return "/admin/navtree/index";
	}
	
	/**
	 * 移到树结点
	 * 
	 * @param nDestBgTreeId
	 * @param nSrcBgTreeId
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "/module/admin/navtree/editMoveBgNavTree", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse editMoveBgNavTree(int nDestBgTreeId,
			int nSrcBgTreeId)
	{
		int nTotal = m_oNavTreeService.editMoveBgNavTree(nDestBgTreeId,
				nSrcBgTreeId);
		
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "导航结点移动成功" : "导航结点移动失败");
		return oFormResponse;
	}
	
	/**
	 * 获取结点信息
	 * 
	 * @param nBgTreeId
	 * @return BgTree
	 */
	@RequestMapping(value = "/module/admin/navtree/getBgNavTreeNode", method = RequestMethod.POST)
	@ResponseBody
	public BgTree getBgNavTreeNode(int nBgTreeId)
	{
		return m_oNavTreeService.getTreeNode(nBgTreeId);
	}
	
	/**
	 * 增加一个导航结点
	 * 
	 * @param nPId
	 * @param sName
	 * @param sUrl
	 * @param sRemark
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/navtree/addBgNavTreeNode", method = RequestMethod.POST)
	public String addBgNavTreeNode(HttpServletRequest oRequest, int nPId,
			 String sName, String sUrl, String sRemark, String pageRight)
	{
		
		int nPKId = m_oNavTreeService.addBgNavTreeNode(nPId, sName, sUrl,sRemark);
		//m_oNavTreeService.addRightCode(isProvince, pageRight);
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nPKId > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nPKId > 0 ? "导航结点增加成功" : "导航结点增加失败");
		oFormResponse.setParam("sName", sName);
		oFormResponse.setParam("nBgTreeId", nPKId);
		oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}
	
	
	/**
	 * 修改一个导航结点
	 * 
	 * @param nBgTreeId
	 * @param sName
	 * @param sUrl
	 * @param sRemark
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/navtree/editBgNavTreeNode", method = RequestMethod.POST)
	public String editBgNavTreeNode(HttpServletRequest oRequest, int nBgTreeId, String sName,
			String sUrl, String sRemark,  String pageRight)
	{
		int nTotal = m_oNavTreeService.editBgNavTreeNode(nBgTreeId, sName,
				sUrl, sRemark);
		//m_oNavTreeService.editRightCode(nBgTreeId, isProvince, pageRight);
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "导航结点修改成功" : "导航结点修改失败");
		oFormResponse.setParam("sName", sName);
		oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}
	
	/**
	 * 删除一个导航结点
	 * 
	 * @param nBgTreeId
	 * @return FormResponse
	 */
	@RequestMapping(value = "/module/admin/navtree/delBgNavTreeNode", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse delBgNavTreeNode(int nBgTreeId)
	{
		int nTotal = m_oNavTreeService.delTreeNode(nBgTreeId);
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "导航结点删除成功" : "导航结点删除失败");
		return oFormResponse;
	}
	
	
    /**
     * 根据reportId获取树信息
     * @param oRequest
     * @return
     */
	@RequestMapping(value = "/module/admin/navtree/getTreeByName", method = RequestMethod.POST)
	@ResponseBody
	public BgTree getTreeByName(HttpServletRequest oRequest)
	{
		String reportId = oRequest.getParameter("reportId"); 
		return m_oNavTreeService.getTreeByName(reportId);
	}
}
