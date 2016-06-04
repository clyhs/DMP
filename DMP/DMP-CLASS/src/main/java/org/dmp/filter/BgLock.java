package org.dmp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BgLock implements Filter, ApplicationContextAware {
	private static BgLock bl = new BgLock();

	private static ApplicationContext cxt;

	Logger logger = LoggerFactory.getLogger(BgLock.class);
	private static int loginCount = 0;

	public ApplicationContext getApplicationContext() {
		return cxt;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		BgLock.cxt = applicationContext;
	}

	public void init(FilterConfig config) {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest oReq = (HttpServletRequest) request;
		HttpServletResponse oRep = (HttpServletResponse) response;

		// 不为sso单点登录时的配置
		if (oReq.getSession().getAttribute("GlobalSessionBgUserId") != null) {
			chain.doFilter(request, response);
		} else if (oReq.getHeader("X-Requested-With") != null
				&& oReq.getHeader("X-Requested-With").equalsIgnoreCase(
						"XMLHttpRequest")) {
			oRep.addHeader("sessionstatus", "timeout");
		} else {
			// System.out.println(oReq.getRequestURI()+"----------oReq.getRequestURI()");
			if ((oReq.getRequestURI().indexOf("/quartz/uploadfile") != -1)) {
				chain.doFilter(request, response);
			} else {
				logger.info("登录的次数：" + (++loginCount));
				oRep.sendRedirect(oReq.getContextPath()+ "/module/login/login.html");
			}
		}

	}

	public void destroy() {

	}

}
