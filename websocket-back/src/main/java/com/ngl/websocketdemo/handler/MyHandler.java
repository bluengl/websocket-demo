package com.ngl.websocketdemo.handler;

import org.springframework.web.socket.TextMessage;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author NGL18
 */
public class MyHandler extends TextWebSocketHandler {

    private String[] userIds;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, String> map = JSONObject.parseObject(payload, HashMap.class);
        System.out.println("=====接受到的数据" + map);
        while (true) {
            double random = Math.random();
            session.sendMessage(new TextMessage("服务器返回收到的信息," + random));
            if (random > 0.5) {
                break;
            }
        }
    }
}
