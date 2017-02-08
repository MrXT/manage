package com.project.common.mq;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;

/**
 * Copyright (c) 2015, 北京卡拉卡尔科技股份有限公司 All rights reserved.
 *
 * @Description: 消息发送服务
 * @since
 * @author xiaolong.li@karakal.com.cn (李小龙)   
 * @date 2015年12月15日 下午4:15:51 
 * @version 1.0
 */
//@Component("msgProducer")
public class MsgProducer {

	private static Log log = LogFactory.getLog("rocketmq");
	
	@Resource(name = "producerWrapper")
	private ProducerWrapper producer;
	
	@Value("${mq.msg.topic}")
    private String topic;
	/**
	 * @Description: 发送单条消息
	 * @param mqMessage
	 * @return    
	 * @throws 
	 * @date 2015年12月16日 下午9:23:20 
	 * @version 1.0
	 */
	public int sendMsg(MqMessage mqMessage) {
		String body = JSON.toJSONString(mqMessage.getMessageBody());
		try {
    		Message msg = new Message();
            msg.setTopic(topic);
            msg.setTags(mqMessage.getTags());

            MessageBody messageBody = producer.buildDefaultMessageBody();
            
            messageBody.setBody(body);
            msg.setBody(JSONObject.toJSONString(messageBody).getBytes());
            SendResult sr = producer.send(msg);
            if (!SendStatus.SEND_OK.equals(sr.getSendStatus())) {
            	log.error("发送消息失败， sendStatus：" + sr.getSendStatus() + ", SendResult:" + JSON.toJSONString(sr) + ", tags:" + mqMessage.getTags() + ", msgBody:" + body);
            } else {
            	log.info("发送消息成功, SendResult:" + JSON.toJSONString(sr) + ", tags:" + mqMessage.getTags() + ", msgBody:" + body);
            }
    	} catch (Throwable e) {
    		log.error("发送消息异常, tags:" + mqMessage.getTags() + ", msgBody:" + body, e);
        }
		
		return 1;
	}	
	
	/**
	 * @Description: 发送多条消息 
	 * @param msgs
	 * @return    
	 * @throws 
	 * @author xiaolong.li@karakal.com.cn (李小龙)   
	 * @date 2015年12月16日 下午9:23:42 
	 * @version 1.0
	 */
	public int sendMsgs(List<MqMessage> msgs) {
		for (MqMessage m : msgs) {
			sendMsg(m);
		}
		return msgs.size();
	}
}
