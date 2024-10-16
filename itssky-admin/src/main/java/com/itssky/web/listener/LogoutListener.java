//package com.itssky.web.listener;
//
//import com.alibaba.fastjson2.JSONObject;
//import com.itssky.common.constant.Constants;
//import com.itssky.common.core.domain.model.LoginUser;
//import com.itssky.common.core.redis.RedisCache;
//import com.itssky.common.utils.MessageUtils;
//import com.itssky.framework.manager.AsyncManager;
//import com.itssky.framework.manager.factory.AsyncFactory;
//import com.itssky.framework.web.service.TokenService;
//import com.itssky.listener.AbstractLogoutListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.core.Message;
//
//import java.io.IOException;
//
//@Component
//public class LogoutListener extends AbstractLogoutListener {
//
//    @Autowired
//    RedisCache redisCache;
//    @Autowired
//    RabbitTemplate rabbitTemplate;
//    @Autowired
//    private TokenService tokenService;
//
//    private static final Logger logger = LoggerFactory.getLogger(LogoutListener.class);
//    @Override
//    public void receiveMessage(String msg, Channel channel, Message message) {
//
//        try {
//            JSONObject object = JSONObject.parseObject(msg);
//            String accessToken = object.getString("accessToken");
//
//            String totalTokenKey = com.itssky.utils.SecurityUtils.getTotalTokenKey(accessToken);
//            LoginUser loginUser = redisCache.getCacheObject(totalTokenKey);
//
//            // 删除用户缓存记录
//            tokenService.delLoginUser(accessToken);
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//
//            // 记录用户退出日志
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginUser.getUsername(), Constants.LOGOUT, MessageUtils.message("user.logout.success")));
//        } catch (IOException e) {
//            logger.error("消息{}消费失败", msg,e);
//            throw new RuntimeException(e);
//        }
//    }
//}
