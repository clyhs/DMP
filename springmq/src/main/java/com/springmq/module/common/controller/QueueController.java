package com.springmq.module.common.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmq.module.bean.MessageInfo;
import com.springmq.module.queue.QueueMessageProducer;

@Controller
public class QueueController extends CommonController {
	
	@Resource(name="queueMessageProducer")
	private QueueMessageProducer queueMessageProducer;

	@RequestMapping(value = "/module/queue.do", method = { RequestMethod.GET })
	@ResponseBody
	public String queue(){
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setContent("消息内容");
		messageInfo.setReceiver("clyhs");
		messageInfo.setTitle("queue消息测试");
		queueMessageProducer.sendMessage(messageInfo);
		return "success";
	}
}
