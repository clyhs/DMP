package com.springmq.common;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import com.springmq.module.bean.MessageInfo;

public class ObjectMessageConverter implements MessageConverter {
	
	private static Logger log = LoggerFactory.getLogger(ObjectMessageConverter.class);

	public Object fromMessage(Message message) throws JMSException,
			MessageConversionException {
		// TODO Auto-generated method stub
		log.info("ObjectMessageConverter.fromMessage receiver message:"+message);
		
		if(message instanceof ObjectMessage){
			ObjectMessage oMsg = (ObjectMessage)message;
		
			if (oMsg instanceof ActiveMQObjectMessage) {
				ActiveMQObjectMessage aMsg = (ActiveMQObjectMessage)oMsg;
			    MessageInfo messageInfo = (MessageInfo) oMsg.getObject();
			    return messageInfo;
			}
		}
		
		Boolean res = message instanceof TextMessage;
		System.out.println(res);
		if(message instanceof TextMessage){
			TextMessage oMsg =  (TextMessage) message;
			res = oMsg instanceof ActiveMQTextMessage;
			System.out.println(res);
			if(oMsg instanceof ActiveMQTextMessage){
				
				ActiveMQTextMessage aMsg = (ActiveMQTextMessage) oMsg;
				
				System.out.println(aMsg.toString());
				
				MessageInfo messageInfo = new MessageInfo();
				//messageInfo.setTitle(aMsg.getText());
				messageInfo.setContent(aMsg.getText());
				messageInfo.setReceiver(aMsg.getReplyTo().getQualifiedName());
				return messageInfo;
			}
		}
		return null;
	}

	public Message toMessage(Object obj, Session session) throws JMSException,
			MessageConversionException {
		// TODO Auto-generated method stub
		log.info("ObjectMessageConverter.toMessage converter message:"+obj);
		if(obj instanceof MessageInfo){
			ActiveMQObjectMessage aMsg = (ActiveMQObjectMessage)session.createObjectMessage();
			aMsg.setObject((MessageInfo)obj);
			log.info("ObjectMessageConverter.toMessage");
			return aMsg;
		}else{
			
		}
		return null;
	}

}
