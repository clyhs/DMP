package org.dmp.module.admin.security.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.dmp.core.util.AlgorithmUitl;
import org.dmp.core.util.StrUtil;
import org.dmp.module.admin.security.service.BgUserService;
import org.dmp.module.common.form.FormResponse;
import org.dmp.module.common.grid.Grid;
import org.dmp.pojo.admin.security.BgUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 后台用户管理控制层
 */
@Controller
public class BgUserController
{
	@Resource(name = "BgUserService")
	private BgUserService m_oBgUserService;
	
	/**
	 * 获取后台用户管理主入口
	 * @param oRequest
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/security/index", method = RequestMethod.GET)
	public String getBgUserIndex(HttpServletRequest oRequest)
	{
		return "/admin/security/index";
	}
	
	/**
	 * 获取后台用户列表
	 * @param oRequest
	 * @return Grid<BgUser>
	 */
	@RequestMapping(value = "/module/admin/security/getBgUserList", method = RequestMethod.POST)
	@ResponseBody
	public Grid<BgUser> getBgUserList(HttpServletRequest oRequest)
	{
		//获取后台用户姓名和帐号
		Integer nBgRoleId = StrUtil.toInt(oRequest.getParameter("nBgRoleId"), 0);
		String sName = StrUtil.toStr(oRequest.getParameter("sName"), ""); 
		String sAccount = StrUtil.toStr(oRequest.getParameter("sAccount"), ""); 
		int nStart = StrUtil.toInt(oRequest.getParameter("start"), 0); 
		int nLimit = StrUtil.toInt(oRequest.getParameter("limit"), 100); 
		
		//获取后台用户列表
		List<BgUser> aBgUser = m_oBgUserService.getBgUserList(nBgRoleId, sName, sAccount, nStart, nLimit);
		Long nTotal = m_oBgUserService.getBgUserListTotal(nBgRoleId, sName, sAccount);
		
		Grid<BgUser> oGrid = new Grid<BgUser>();
		oGrid.setTotal(nTotal);
		oGrid.setStart(nStart);
		oGrid.setData(aBgUser);
		return oGrid;
	}
	
	/**
	 * 获取一个后台用户资料
	 * @param oRequest
	 * @return BgUser
	 */
	@RequestMapping(value = "/module/admin/security/getBgUser", method = RequestMethod.POST)
	@ResponseBody
	public BgUser getBgUser(HttpServletRequest oRequest)
	{
		Integer nBgUserId = StrUtil.toInt(oRequest.getParameter("nBgUserId"), 0);
		return m_oBgUserService.getBgUser(nBgUserId);
	}
	
	/**
	 * 增加一个后台用户
	 * @param oRequest
	 * @param oResponse
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/security/addBgUser", method = RequestMethod.POST)
	public String addBgUser(HttpServletRequest oRequest)
	{
		//接收参数
		String sAccount = StrUtil.toStr(oRequest.getParameter("sAccount"), "");
		String sPassword = StrUtil.toStr(oRequest.getParameter("sPassword"), "");
		String sName = StrUtil.toStr(oRequest.getParameter("sName"), "");
		Integer nBgDeptId = StrUtil.toInt(oRequest.getParameter("nBgDeptId"), 0);
		String sDeptName = StrUtil.toStr(oRequest.getParameter("sDeptName"), "");
		String sBgRoleId = StrUtil.toStr(oRequest.getParameter("sBgRoleId"), "");
		String sBgRole = StrUtil.toStr(oRequest.getParameter("sBgRole"), "");
		String sBgPostId = StrUtil.toStr(oRequest.getParameter("sBgPostId"), "");
		String sBgPost = StrUtil.toStr(oRequest.getParameter("sBgPost"), "");
		Integer nSex = StrUtil.toInt(oRequest.getParameter("nSex"), 3);
		String sQQ = StrUtil.toStr(oRequest.getParameter("sQQ"), "");
		String sEmail = StrUtil.toStr(oRequest.getParameter("sEmail"), "");
		String sMobile = StrUtil.toStr(oRequest.getParameter("sMobile"), "");
		String sTel = StrUtil.toStr(oRequest.getParameter("sTel"), "");
		String sFax = StrUtil.toStr(oRequest.getParameter("sFax"), "");
		String sZip = StrUtil.toStr(oRequest.getParameter("sZip"), "");
		String sAddress = StrUtil.toStr(oRequest.getParameter("sAddress"), "");
		String sBrief = StrUtil.toStr(oRequest.getParameter("sBrief"), "");
		
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		
		BgUser oBgUser = m_oBgUserService.getBgUser(sAccount);
		if(oBgUser == null)
		{
			//增加用户
			int nTotal = m_oBgUserService.addBgUser(sAccount, sPassword, sName, nBgDeptId, sDeptName, sBgRoleId, sBgRole, sBgPostId, sBgPost, nSex, sQQ, sEmail, sMobile, sTel, sFax, sZip, sAddress, sBrief);
			oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
			oFormResponse.setMsg(nTotal > 0 ? "后台用户增加成功" : "后台用户增加失败");			
		}else
		{
			oFormResponse.setSuccess(Boolean.FALSE);
			oFormResponse.setMsg("后台用户增加失败<br/>此帐号已存在，请换帐号再试");
		}
		
		oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}
	
	/**
	 * 修改一个后台用户
	 * @param oRequest
	 * @param oResponse
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/security/editBgUser", method = RequestMethod.POST)
	public String editBgUser(HttpServletRequest oRequest)
	{
		Integer nBgUserId = StrUtil.toInt(oRequest.getParameter("nBgUserId"), 0);
		String sPassword = StrUtil.toStr(oRequest.getParameter("sPassword"), "");
		String sName = StrUtil.toStr(oRequest.getParameter("sName"), "");
		Integer nBgDeptId = StrUtil.toInt(oRequest.getParameter("nBgDeptId"), 0);
		String sDeptName = StrUtil.toStr(oRequest.getParameter("sDeptName"), "");
		String sBgRoleId = StrUtil.toStr(oRequest.getParameter("sBgRoleId"), "");
		String sBgRole = StrUtil.toStr(oRequest.getParameter("sBgRole"), "");
		String sBgPostId = StrUtil.toStr(oRequest.getParameter("sBgPostId"), "");
		String sBgPost = StrUtil.toStr(oRequest.getParameter("sBgPost"), "");
		Integer nSex = StrUtil.toInt(oRequest.getParameter("nSex"), 3);
		String sQQ = StrUtil.toStr(oRequest.getParameter("sQQ"), "");
		String sEmail = StrUtil.toStr(oRequest.getParameter("sEmail"), "");
		String sMobile = StrUtil.toStr(oRequest.getParameter("sMobile"), "");
		String sTel = StrUtil.toStr(oRequest.getParameter("sTel"), "");
		String sFax = StrUtil.toStr(oRequest.getParameter("sFax"), "");
		String sZip = StrUtil.toStr(oRequest.getParameter("sZip"), "");
		String sAddress = StrUtil.toStr(oRequest.getParameter("sAddress"), "");
		String sBrief = StrUtil.toStr(oRequest.getParameter("sBrief"), "");
		
		int nTotal = m_oBgUserService.editBgUser(nBgUserId, sPassword, sName, nBgDeptId, sDeptName, sBgRoleId, sBgRole, sBgPostId, sBgPost, nSex, sQQ, sEmail, sMobile, sTel, sFax, sZip, sAddress, sBrief);
		
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "后台用户修改成功" : "后台用户修改失败");
		oRequest.setAttribute("sResponse", oFormResponse);
		
		return "/admin/common/form_response";
	}	
	
	/**
	 * 删除后台用户
	 * @param sBgUserIdList
	 * @return FormResponse
	 */
	@RequestMapping(value = "/module/admin/security/delBgUser", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse delBgUser(String sBgUserIdList)
	{
		int nTotal = m_oBgUserService.delBgUserList(sBgUserIdList);
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "后台用户删除成功" : "后台用户删除失败");
		
		return oFormResponse;
	}	
		
	/**登录用户资料********************************************************************/
	/**
	 * 登录用户修改用户资料入口
	 * @param oRequest
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/security/bg_user_file", method = RequestMethod.GET)
	public String getBgUserFileIndex(HttpServletRequest oRequest)
	{
		return "/admin/security/bg_user_file";
	}
	
	/**
	 * 后台用户修改用户密码入口
	 * @param oRequest
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/security/bg_user_pw", method = RequestMethod.GET)
	public String getBgUserPWIndex(HttpServletRequest oRequest)
	{
		return "/admin/security/bg_user_pw";
	}
	
	/**
	 * 获取一个后台登录用户资料
	 * @param oRequest
	 * @return BgUser
	 */
	@RequestMapping(value = "/module/admin/security/getMyBgUser", method = RequestMethod.POST)
	@ResponseBody
	public BgUser getMyBgUser(HttpServletRequest oRequest)
	{
		//从Session中获取
		Integer nBgUserId = (Integer)(oRequest.getSession().getAttribute("GlobalSessionBgUserId"));
		return m_oBgUserService.getBgUser(nBgUserId);
	}
	
	/**
	 * 修改一个后台登录用户资料
	 * @param oRequest
	 * @param oResponse
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/security/editMyBgUserFile", method = RequestMethod.POST)
	public String editMyBgUserFile(HttpServletRequest oRequest)
	{
		//从Session中获取
		Integer nBgUserId = (Integer)(oRequest.getSession().getAttribute("GlobalSessionBgUserId"));
		
		String sPassword = "**********";
		String sName = StrUtil.toStr(oRequest.getParameter("sName"), "");
		Integer nBgDeptId = StrUtil.toInt(oRequest.getParameter("nBgDeptId"), 0);
		String sDeptName = StrUtil.toStr(oRequest.getParameter("sDeptName"), "");
		String sBgRoleId = StrUtil.toStr(oRequest.getParameter("sBgRoleId"), "");
		String sBgRole = StrUtil.toStr(oRequest.getParameter("sBgRole"), "");
		String sBgPostId = StrUtil.toStr(oRequest.getParameter("sBgPostId"), "");
		String sBgPost = StrUtil.toStr(oRequest.getParameter("sBgPost"), "");
		Integer nSex = StrUtil.toInt(oRequest.getParameter("nSex"), 3);
		String sQQ = StrUtil.toStr(oRequest.getParameter("sQQ"), "");
		String sEmail = StrUtil.toStr(oRequest.getParameter("sEmail"), "");
		String sMobile = StrUtil.toStr(oRequest.getParameter("sMobile"), "");
		String sTel = StrUtil.toStr(oRequest.getParameter("sTel"), "");
		String sFax = StrUtil.toStr(oRequest.getParameter("sFax"), "");
		String sZip = StrUtil.toStr(oRequest.getParameter("sZip"), "");
		String sAddress = StrUtil.toStr(oRequest.getParameter("sAddress"), "");
		String sBrief = StrUtil.toStr(oRequest.getParameter("sBrief"), "");
		
		int nTotal = m_oBgUserService.editBgUser(nBgUserId, sPassword, sName, nBgDeptId, sDeptName, sBgRoleId, sBgRole, sBgPostId, sBgPost, nSex, sQQ, sEmail, sMobile, sTel, sFax, sZip, sAddress, sBrief);
		
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "后台用户资料修改成功" : "后台用户资料修改失败");
		oRequest.setAttribute("sResponse", oFormResponse);
		
		return "/admin/common/form_response";
	}	
	
	/**
	 * 修改一个后台登录用户密码
	 * @param oRequest
	 * @param oResponse
	 * @return String
	 */
	@RequestMapping(value = "/module/admin/security/editMyBgUserPW", method = RequestMethod.POST)
	public String editMyBgUserPW(HttpServletRequest oRequest)
	{
		//从Session中获取
		Integer nBgUserId = (Integer)(oRequest.getSession().getAttribute("GlobalSessionBgUserId"));
		
		String sPassword1 = StrUtil.toStr(oRequest.getParameter("sPassword1"), "");
		String sPassword2 = StrUtil.toStr(oRequest.getParameter("sPassword2"), "");
		String sPassword3 = StrUtil.toStr(oRequest.getParameter("sPassword3"), "");
		
		FormResponse oFormResponse = new FormResponse();
		if(sPassword1.equals(""))
		{
			oFormResponse.setSuccess(Boolean.FALSE);
			oFormResponse.setMsg("用户密码为空，不能修改密码");
		}else
		{
			if(!sPassword2.equals("") && sPassword2.equals(sPassword3))
			{
				BgUser oBgUser = m_oBgUserService.getBgUser(nBgUserId);
				if(!oBgUser.getPassword().equals(AlgorithmUitl.md5(sPassword1)))
				{
					oFormResponse.setSuccess(Boolean.FALSE);
					oFormResponse.setMsg("用户密码不正确，不能修改密码");
				}else
				{
					int nTotal = m_oBgUserService.editMyBgUserPW(nBgUserId, sPassword2);
					oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
					oFormResponse.setMsg(nTotal > 0 ? "后台用户密码修改成功" : "后台用户密码修改失败");
				}
			}else
			{
				oFormResponse.setSuccess(Boolean.FALSE);
				oFormResponse.setMsg("用户新密码与确认密码不匹配，不能修改密码");
			}
		}
		
		oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}	
}
