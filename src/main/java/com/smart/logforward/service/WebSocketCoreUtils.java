package com.smart.logforward.service;

import com.alibaba.fastjson.JSON;
import com.smart.logforward.model.LogMessage;
import com.smart.logforward.utils.SocketManager;
import com.smart.logforward.utils.SpringBootBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理socket 转发数据
 */
public class WebSocketCoreUtils {

    private static SimpMessagingTemplate template = SpringBootBeanUtil.getBean(SimpMessagingTemplate.class);

    public static void sendUser(String data) {

        // TODO 区分用户
        LogMessage logMessage = parse(data);
        //String user = logMessage.getUser();
        String token = "1234";

        WebSocketSession webSocketSession = SocketManager.get(token);
        if (webSocketSession != null && logMessage != null) {
            /**
             * 主要防止broken pipe
             */
            template.convertAndSend("/queue/" + token ,logMessage);
        }
    }

    private static LogMessage parse(String data){

        if(data.indexOf("beat!") > -1){
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        LogMessage logMessage = JSON.parseObject(data,LogMessage.class);
        return logMessage;
    }
}
