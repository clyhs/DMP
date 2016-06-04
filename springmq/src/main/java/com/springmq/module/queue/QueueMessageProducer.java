package com.springmq.module.queue;

import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;

import com.springmq.common.ObjectMessageConverter;
import com.springmq.module.bean.MessageInfo;

public class QueueMessageProducer {
	
	private JmsTemplate jmsTemplate;
	
	private Destination queueDestination;
	
	private ObjectMessageConverter objectMessageConverter;
	
	
	public void sendMessage(MessageInfo messageInfo){
		jmsTemplate.setMessageConverter(objectMessageConverter);
		jmsTemplate.setPubSubDomain(false);
		jmsTemplate.convertAndSend(queueDestination,messageInfo);
	}
	

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Destination getQueueDestination() {
		return queueDestination;
	}

	public void setQueueDestination(Destination queueDestination) {
		this.queueDestination = queueDestination;
	}

	public ObjectMessageConverter getObjectMessageConverter() {
		return objectMessageConverter;
	}

	public void setObjectMessageConverter(
			ObjectMessageConverter objectMessageConverter) {
		this.objectMessageConverter = objectMessageConverter;
	}
	
	

}
