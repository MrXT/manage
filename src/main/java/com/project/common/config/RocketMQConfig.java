package com.project.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.project.common.mq.AbstractMsgHandler;
import com.project.common.mq.ProducerWrapper;
import com.project.common.mq.PushConsumerWrapper;
import com.project.common.util.ListUtils;


/**
 * mq生成与消费配置
 * ClassName: RocketMQConfig <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月10日
 * @version 1.0
 */
//@Configuration
public class RocketMQConfig {
	
	@Value("${mq.consumerGroup}")
	private String consumerGroup;
	
	@Value("${mq.namesrvAddr}")
	private String namesrvAddr;
	
	@Value("${mq.consumeThreadMin}")
	private Integer consumeThreadMin;
	
	@Value("${mq.consumeThreadMax}")
	private Integer consumeThreadMax;
	
	@Value("${mq.topic}")
	private String topic;

	@Value("${mq.application}")
	private String application;

	@Value("${mq.application}")
	private String producerGroup;
	
	@Autowired
	private AbstractMsgHandler msgHandler;
	/**
	 * 配置消费，必须实现AbstractMsgHandler,去掉注释即可打开
	 * @return
	 */
	@Bean(initMethod="init", destroyMethod="shutdown")
	public PushConsumerWrapper pushConsumerWrapper() {
		PushConsumerWrapper wrapper = new PushConsumerWrapper();
		wrapper.setConsumerGroup(consumerGroup);
		wrapper.setNamesrvAddr(namesrvAddr);
		wrapper.setConsumeThreadMin(consumeThreadMin);
		wrapper.setConsumeThreadMax(consumeThreadMax);
		wrapper.setSubs(ListUtils.arrayToList(topic));
		wrapper.setMsgHandler(msgHandler);
		return wrapper;
	}
	/**
	 * mq消息产生
	 * @return
	 */
	@Bean(initMethod = "init",destroyMethod = "shutdown")
	public ProducerWrapper producerWrapper(){
		ProducerWrapper producerWrapper = new ProducerWrapper();
		producerWrapper.setApplication(application);
		producerWrapper.setNamesrvAddr(namesrvAddr);
		producerWrapper.setProducerGroup(producerGroup);
		producerWrapper.setIdleSec(60);
		return producerWrapper;
	}

}
