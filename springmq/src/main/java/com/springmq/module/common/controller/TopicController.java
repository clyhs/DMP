package com.springmq.module.common.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmq.module.bean.MessageInfo;
import com.springmq.module.queue.QueueMessageProducer;
import com.springmq.module.topic.TopicMessageProducer;

@Controller
public class TopicController extends CommonController {
	
	@Resource(name="topicMessageProducer")
	private TopicMessageProducer topicMessageProducer;
	
	@RequestMapping(value = "/module/topic.do", method = { RequestMethod.GET })
	@ResponseBody
	public String topic(){
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setContent("消息内容");
		messageInfo.setReceiver("clyhs");
		messageInfo.setTitle("topic消息测试");
		topicMessageProducer.sendMessage(messageInfo);
		return "success";
	}

}
