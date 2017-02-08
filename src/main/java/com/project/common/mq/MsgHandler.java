package com.project.common.mq;



import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.project.common.util.LogUtils;


/**
 * @Description: MQ接收消息处理Handler
 * @since
 * @date 2015年12月15日 下午3:10:56
 * @version 1.0
 */
//@Component
public class MsgHandler extends AbstractMsgHandler {

    /**
     * 这里处理消息（业务逻辑处理）
     * @see com.project.common.mq.AbstractMsgHandler#handler(com.alibaba.rocketmq.common.message.Message)
     */
    @Override
    public ConsumeConcurrentlyStatus handler(Message message) {
        String msgId = null;
        if (message instanceof MessageExt) {
            MessageExt ext = (MessageExt) message;
            msgId = ext.getMsgId();
        }
        String topic = message.getTopic();
        String tags = message.getTags();
        if(tags.indexOf("catalog") != -1){
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        String msgbody = new String(message.getBody());
        try {
            MessageBody mb = JSONObject.parseObject(msgbody, MessageBody.class);
            JSONObject body = JSONObject.parseObject(mb.getBody());
            @SuppressWarnings("unused")
            Long dataId = body.getLong("id");
            Integer status = body.getInteger("status");
            if (status == null) {
                LogUtils.WARN("status为空，msgId:" + msgId + ", topic:" + topic + ", tags:" + tags + ", body:" + msgbody);
            } else{
                if (tags.indexOf("artist") != -1) {
                    LogUtils.INFO("收到消息, msgId:" + msgId + ", topic:" + topic + ", tags:" + tags + ", body:" + msgbody);
//                    esService.sync("artist", dataId);
                }
                if (tags.indexOf("album") != -1) {
//                    LogUtil.INFO("收到消息, msgId:" + msgId + ", topic:" + topic + ", tags:" + tags + ", body:" + msgbody);
//                    esService.sync("album", dataId);
                }
                if (tags.indexOf("song") != -1) {
//                    LogUtil.INFO("收到消息, msgId:" + msgId + ", topic:" + topic + ", tags:" + tags + ", body:" + msgbody);
//                    esService.sync("song", dataId);
                }
            }
        } catch (Throwable e) {
            LogUtils.ERROR("处理消息异常, msgId:" + msgId + ", topic:" + topic + ", tags:" + tags + ", body:" + msgbody);
            LogUtils.LOGEXCEPTION(e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
