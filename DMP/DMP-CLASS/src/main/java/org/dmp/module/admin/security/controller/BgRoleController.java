package org.dmp.module.admin.security.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.util.JsonUtil;
import org.dmp.core.util.StrUtil;
import org.dmp.core.util.Tools;
import org.dmp.module.admin.navtree.service.NavTreeService;
import org.dmp.module.admin.security.service.BgRoleService;
import org.dmp.module.common.form.FormResponse;
import org.dmp.module.common.grid.Grid;
import org.dmp.module.common.tree.Tree;
import org.dmp.pojo.admin.security.BgRRT;
import org.dmp.pojo.admin.security.BgRole;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 后台角色管理控制层
 */
@Controller
public class BgRoleController
{
	@Resource(name = "BgRoleService")
	private BgRoleService m_oBgRoleService;
	
	@Resource(name = "NavTreeService")
	private NavTreeService m_oNavTreeService;
	
	/**
	 * 获取角色列表
	 * @param oRequest
	 * @return Grid<BgRole>
	 */
	@RequestMapping(value = "/module/admin/security/getBgRoleList", method = RequestMethod.POST)
	@ResponseBody
	public Grid<BgRole> getBgRoleList(HttpServletRequest oRequest)
	{
		String sName = StrUtil.toStr(oRequest.getParameter("sName"), ""); 
		int nStart = StrUtil.toInt(oRequest.getParameter("start"), 0); 
		int nLimit = StrUtil.toInt(oRequest.getParameter("limit"), 100); 
		
		List<BgRole> aBgRole = m_oBgRoleService.getBgRoleList(sName, nStart, nLimit);
		Long nTotal = m_oBgRoleService.getBgRoleListTotal(sName);
		
		Grid<BgRole> oGrid = new Grid<BgRole>();
		oGrid.setTotal(nTotal);
		oGrid.setStart(nStart);
		oGrid.setData(aBgRole);
		return oGrid;
	}
	
	/**
	 * 获取后台角色树
	 * @param oRequest
	 * @return List<Tree>
	 */
	@RequestMapping(value = "/module/admin/security/getBgRoleTree", method = RequestMethod.POST)
	@ResponseBody
	public List<Tree> getBgRoleTree(HttpServletRequest oRequest)
	{
		return m_oBgRoleService.getBgRoleTree(1);
	}
	
	/**
	 * 获取一个后台角色资料
	 * @param oRequest
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "/module/admin/security/getBgRole", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getBgRole(HttpServletRequest oRequest)
	{
		Map<String, Object> aReturnMap = new HashMap<String, Object>();
		Integer nBgRoleId = StrUtil.toInt(oRequest.getParameter("nBgRoleId"), 0);
		aReturnMap.put("sName", m_oBgRoleService.getBgRole(nBgRoleId).getName());
		aReturnMap.put("aNavTreeId", m_oBgRoleService.getBgRRTList(nBgRoleId));
		return aReturnMap;
	}
	
	/**
	 * 增加一个后台角色
	 * @param oRequest
	 * @param oResponse void
	 */
	@RequestMapping(value = "/module/admin/security/addBgRole", method = RequestMethod.POST)
	public String addBgRole(HttpServletRequest oRequest)
	{
		String sName = StrUtil.toStr(oRequest.getParameter("sName"), "");
		String sBgTreeIdList = StrUtil.toStr(oRequest.getParameter("sBgTreeIdList"), "0");
		String rightMap = StrUtil.toStr(oRequest.getParameter("sRightMap"), "0");
		String sRemark = StrUtil.toStr(oRequest.getParameter("sRemark"), "");
		JsonUtil jsonUtil = new JsonUtil();
		Map<Integer,String> map = (Map<Integer, String>) jsonUtil.decode(rightMap);
		int nPKId = m_oBgRoleService.addBgRole(sName, sBgTreeIdList, map, sRemark);
		
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nPKId > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nPKId > 0 ? "角色增加成功" : "角色名称重复");
		oFormResponse.setParam("nBgRoleId", nPKId);
		oRequest.setAttribute("sResponse", oFormResponse);
		
		return "/admin/common/form_response";
	}
	
	/**
	 * 修改一个后台角色
	 * @param oRequest
	 * @param oResponse void
	 */
	@RequestMapping(value = "/module/admin/security/editBgRole", method = RequestMethod.POST)
	public String editBgRole(HttpServletRequest oRequest)
	{
		Integer nBgRoleId = StrUtil.toInt(oRequest.getParameter("nBgRoleId"), 0);
		String sName = StrUtil.toStr(oRequest.getParameter("sName"), "");
		String sBgTreeIdList = StrUtil.toStr(oRequest.getParameter("sBgTreeIdList"), "0");
		String rightMap = StrUtil.toStr(oRequest.getParameter("sRightMap"), "0");
		String sRemark = StrUtil.toStr(oRequest.getParameter("sRemark"), "");
		JsonUtil jsonUtil = new JsonUtil();
		Map<Integer,String> map = (Map<Integer, String>) jsonUtil.decode(rightMap);
		int nTotal = m_oBgRoleService.editBgRole(nBgRoleId, sName, sBgTreeIdList, map, sRemark);
		
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "角色修改成功" : "角色名称重复");
		oRequest.setAttribute("sResponse", oFormResponse);
		
		return "/admin/common/form_response";
	}	
	
	/**
	 * 删除后台角色
	 * @param nBgRoleId
	 * @return FormResponse
	 */
	@RequestMapping(value = "/module/admin/security/delBgRole", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse delBgRole(int nBgRoleId)
	{
		FormResponse oFormResponse = new FormResponse();
		int nTotal = m_oBgRoleService.delBgRole(nBgRoleId);
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "后台角色删除成功" : "后台角色删除失败");
		return oFormResponse;
	}	
	
	
	/**
	 * 后台角色是否正在使用
	 * @param nBgRoleId
	 * @return FormResponse
	 */
	@RequestMapping(value = "/module/admin/security/checkRoleUse", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse checkRoleUse(int nBgRoleId)
	{
		FormResponse oFormResponse = new FormResponse();
		int nTotal = m_oBgRoleService.delBgRoleBefore(nBgRoleId);
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "后台角色没有使用" : "后台角色正在使用中，无法删除");
		return oFormResponse;
	}	
	
	/**
	 * 后台用户归角色
	 * @param sBgUserId
	 * @param nBgRoleId
	 * @return FormResponse
	 */
	@RequestMapping(value = "/module/admin/security/moveToRole", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse moveToRole(String sBgUserId, int nBgRoleId)
	{
		FormResponse oFormResponse = new FormResponse();
		int nTotal = m_oBgRoleService.addBgUserToRole(sBgUserId, nBgRoleId);
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "后台用户归角色成功" : "后台用户归角色失败");
		return oFormResponse;
	}	
	
	/**
	 * 获取角色的导航树结点
	 * @param oRequest
	 * @return List<Tree>
	 */
	@RequestMapping(value = "/module/admin/security/getBgNavTree", method = RequestMethod.POST)
	@ResponseBody
	public List<Tree> getBgNavTree(HttpServletRequest oRequest)
	{
		int nBgTreeId = 1;
		if (oRequest.getParameter("node") != null)
		{
			List<String> aBgTreeId = Tools.getRegexResult(
					oRequest.getParameter("node"), "[a-zA-Z_]*([0-9]+)");
			if (aBgTreeId.size() > 0)
			{
				nBgTreeId = StrUtil.toInt(aBgTreeId.get(0), 1);
				if (nBgTreeId == 0) nBgTreeId = 1;
			}
		}
		return m_oNavTreeService.getBgNavTree(nBgTreeId, Boolean.TRUE);
	}
	
	/**
	 * 角色管理入口
	 * @param oRequest
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/security/bg_role", method = RequestMethod.GET)
	public String getCrbtOpenAccountIndex(HttpServletRequest oRequest)
	{
		return "/admin/security/bg_role";
	}
	
	/**
	 * 获取角色管理界面权限选择项
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/security/getRoleCheckGroup", method = RequestMethod.POST)
	@ResponseBody
	public List<DynaBean> getRoleCheckGroup(HttpServletRequest oRequest)
	{ 
		int treeId = StrUtil.toInt(oRequest.getParameter("treeId"), 0);
		List<DynaBean> checkGroupList = m_oBgRoleService.getRoleCheckGroup(treeId);
		return checkGroupList;

	}
	
	/**
	 * 获取权限功能关联表中的信息
	 * @param treeId
	 * @return
	 */
	@RequestMapping(value = "/module/admin/security/getRolePermissionInfo", method = RequestMethod.POST)
	@ResponseBody
	public List<BgRRT> getRolePermissionInfo(HttpServletRequest oRequest){
		int treeId = StrUtil.toInt(oRequest.getParameter("treeId"), 0);
		int roleId = StrUtil.toInt(oRequest.getParameter("roleId"), 0);
		return m_oBgRoleService.getRolePermissionInfo(treeId, roleId);
	}
	
	/**
	 *  获取权限功能关联表中的信息
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/security/getRolePermissionInfo1", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getRolePermissionInfo1(HttpServletRequest oRequest){
		int treeId = StrUtil.toInt(oRequest.getParameter("treeId"), 0);
		int roleId = StrUtil.toInt(oRequest.getParameter("roleId"), 0);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> aTreeId = new ArrayList<Integer>();
		List<BgRRT> rrt = m_oBgRoleService.getRolePermissionInfo(treeId,roleId);
		if(rrt.size() > 0){
			String dataRight =rrt.get(0).getsDataRight();
			if(dataRight != null){
			    String[] Strs = dataRight.split(",");
			    for(int i = 0;i < Strs.length;i++){
			    	aTreeId.add(Integer.parseInt(Strs[i]));
			    }
				map.put("dataRight",aTreeId);
				return map;
			}
		}
		
		return null;
	}
	
	/**
	 *  判断是否需要进行权限分配
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/security/isOpenWindow", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse isOpenWindow(HttpServletRequest oRequest){
		int treeId = StrUtil.toInt(oRequest.getParameter("treeId"), 0);
		System.out.println(treeId+"");
		FormResponse oFormResponse = new FormResponse();
		int nTotal = m_oBgRoleService.getRightCode(treeId);
		System.out.println(nTotal+"");
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "可以进行权限分配" : "该功能不需要进行角色权限分配");
		return oFormResponse;
	}

}
