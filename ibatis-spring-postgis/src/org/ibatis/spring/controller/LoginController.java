package org.ibatis.spring.controller;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ibatis.spring.beans.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

@Controller
public class LoginController extends SimpleFormController {

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		// TODO Auto-generated method stub
		User user = (User)command;
		System.out.println(user.getUsername());
		

	
		
		return new ModelAndView("test");
	}

	
}
