package org.dmp.module.login.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;
//import org.dmp.core.db.hbase.IDao;
import org.dmp.core.db.hibernate.IDao;
import org.dmp.core.util.StrUtil;
import org.dmp.core.util.Tools;
import org.dmp.module.admin.security.service.BgUserService;
import org.dmp.module.common.form.FormResponse;
import org.dmp.pojo.admin.security.BgUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LoginController {
	
	//@Resource(name = "MUPHbase")
	//protected IDao<DynaBean> m_oDB;

	private final static Logger logger = LoggerFactory.getLogger(LoginController.class);
		@Resource(name = "BgUserService")
		private BgUserService m_oBgUserService;
	
	@RequestMapping(value = "/module/login/login", method = RequestMethod.GET)
	public String getLogin(HttpServletRequest oRequest){
		//List<DynaBean> list = m_oDB.select("test");
		//System.out.println(list.size());
		return "/login/login";
	}
	
	@RequestMapping(value = "/module/login/islogin", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse login(HttpServletRequest oRequest){
		String sAccount = StrUtil.toStr(oRequest.getParameter("sAccount"), "");
		String sPassword = StrUtil
				.toStr(oRequest.getParameter("sPassword"), "");
		FormResponse oFormResponse = new FormResponse();

		BgUser oBgUser = m_oBgUserService.getBgUser(sAccount, sPassword);
		if (oBgUser != null)
		{
			oRequest.getSession().setAttribute("GlobalSessionBgUserId",
					oBgUser.getBgUserId());
		}
		oFormResponse
				.setSuccess(oBgUser != null ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(oBgUser != null ? "后台用户登录成功"
				: "后台用户登录失败<br/>请确认您的帐号或密码是否正确");
		
        logger.info("client ip address: " + Tools.getRequestIp(oRequest));
        return oFormResponse;
	}
	
	@RequestMapping(value = "/module/bglogin/logout", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse logout(HttpServletRequest oRequest)
	{
		FormResponse oFormResponse = new FormResponse();
		Object oBgUserId = oRequest.getSession().getAttribute(
				"GlobalSessionBgUserId");
		if (oBgUserId != null)
		{
			oRequest.getSession().removeAttribute("GlobalSessionBgUserId");
		}

		oFormResponse.setSuccess(Boolean.TRUE);
		oFormResponse.setMsg("后台用户退出成功");

		return oFormResponse;
	}
	@ResponseBody
	public BgUser getBgUser(String name, String pwd) {

		return m_oBgUserService.getBgUser(name, pwd);
	}
}
