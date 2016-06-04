package org.ibatis.spring.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class MVController extends MultiActionController {
	
	public MVController(){}

	
	public ModelAndView method01(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("method01 controller");
		
		Map model =new HashMap(); 
		model.put("city", "123");
		
		return new ModelAndView("test",model);
	}
	
	

}
