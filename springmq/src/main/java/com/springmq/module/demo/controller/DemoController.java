package com.springmq.module.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmq.module.common.controller.CommonController;

@Controller
public class DemoController extends CommonController {
	
	@RequestMapping(value = "/module/demo/hello", method = { RequestMethod.GET })
	public String hello(){
		return "demo/hello";
	}

}
