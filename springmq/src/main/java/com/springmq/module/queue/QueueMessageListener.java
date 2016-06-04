package com.springmq.module.queue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConversionException;

import com.springmq.common.ObjectMessageConverter;
import com.springmq.module.bean.MessageInfo;

public class QueueMessageListener implements MessageListener {
	
	private static Logger log = LoggerFactory.getLogger(QueueMessageListener.class);
	
	private ObjectMessageConverter objectMessageConverter;

	

	public ObjectMessageConverter getObjectMessageConverter() {
		return objectMessageConverter;
	}



	public void setObjectMessageConverter(
			ObjectMessageConverter objectMessageConverter) {
		this.objectMessageConverter = objectMessageConverter;
	}



	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		
		
		
		if(message instanceof ObjectMessage){
			ObjectMessage  objectMessage = (ObjectMessage)message;
			log.info("QueueMessageListener.onMessage message:"+message);
			try {
				System.out.println(message.getJMSMessageID().toString());
				MessageInfo messageInfo = (MessageInfo) objectMessageConverter.fromMessage(message);
				System.out.println("*********************************");
				System.out.println("接收到消息title："+messageInfo.getTitle());
				System.out.println("接收到消息conntent："+messageInfo.getContent());
				System.out.println("接收到消息receiver："+messageInfo.getReceiver());
				System.out.println("接收到消息destination："+objectMessage.getJMSDestination());
				System.out.println("接收到消息type："+objectMessage.getJMSType());
				System.out.println("接收到消息messageId："+objectMessage.getJMSMessageID());
				System.out.println("*********************************");
				
			} catch (Exception e) {
				
			}
		}
		if(message instanceof TextMessage){
			TextMessage textMessage = (TextMessage) message;
			log.info("QueueMessageListener.onMessage message:"+message);
			try {
				System.out.println(message.getJMSMessageID().toString());
				MessageInfo messageInfo = (MessageInfo) objectMessageConverter.fromMessage(message);
				System.out.println("*********************************");
				System.out.println("接收到消息title："+messageInfo.getTitle());
				System.out.println("接收到消息conntent："+messageInfo.getContent());
				System.out.println("接收到消息receiver："+messageInfo.getReceiver());
				System.out.println("接收到消息destination："+textMessage.getJMSDestination());
				System.out.println("接收到消息type："+textMessage.getJMSType());
				System.out.println("接收到消息messageId："+textMessage.getJMSMessageID());
				System.out.println("*********************************");
				
			} catch (Exception e) {
				
			}
		}
		
		

	}

}
