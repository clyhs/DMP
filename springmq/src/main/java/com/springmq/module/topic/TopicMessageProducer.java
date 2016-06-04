package com.springmq.module.topic;

import javax.jms.DeliveryMode;
import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;

import com.springmq.common.ObjectMessageConverter;
import com.springmq.module.bean.MessageInfo;

public class TopicMessageProducer {
	
	private JmsTemplate jmsTemplate;
	
	private Destination topicDestination;
	
	private ObjectMessageConverter objectMessageConverter;
	
	
	public void sendMessage(MessageInfo messageInfo){
		jmsTemplate.setMessageConverter(objectMessageConverter);
		jmsTemplate.setPubSubDomain(false);
		jmsTemplate.convertAndSend(topicDestination,messageInfo);
	}
	

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	

	public Destination getTopicDestination() {
		return topicDestination;
	}


	public void setTopicDestination(Destination topicDestination) {
		this.topicDestination = topicDestination;
	}


	public ObjectMessageConverter getObjectMessageConverter() {
		return objectMessageConverter;
	}

	public void setObjectMessageConverter(
			ObjectMessageConverter objectMessageConverter) {
		this.objectMessageConverter = objectMessageConverter;
	}
	
	

}
