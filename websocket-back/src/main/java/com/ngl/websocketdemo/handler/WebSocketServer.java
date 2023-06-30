package com.ngl.websocketdemo.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * @author NGL18
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{sid}")
public class WebSocketServer {

    private final static String DEF_SID_STR = "sid";
    private final static String DEF_TYPE_STR = "type";
    private final static String DEF_DATA_STR = "data";
    private final static String DEF_MESSAGE_STR = "message";
    private final static String DEF_USERMSG_STR = "usermsg";
    private final static String DEF_MSG_STR = "msg";
    private final static String DEF_ONLINE_STR = "online";
    private final static String DEF_SYSMSG_STR = "sysmsg";

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static final CopyOnWriteArraySet<WebSocketServer> WEB_SOCKET_SET = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收sid
     */
    private String sid = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocketServer that = (WebSocketServer) o;
        return Objects.equals(session, that.session) &&
                Objects.equals(sid, that.sid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, sid);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(DEF_SID_STR) String sid) {
        this.session = session;
        //加入set中
        WEB_SOCKET_SET.add(this);
        //在线数加1
        addOnlineCount();
        log.info("有新窗口开始监听:" + sid + ",当前在线人数为" + getOnlineCount());
        this.sid = sid;
        Map<String, Object> msgMap = new HashMap<>(100);
        msgMap.put(DEF_TYPE_STR, DEF_SYSMSG_STR);
        msgMap.put(DEF_DATA_STR, "连接成功");
        sendInfo(JSON.toJSONString(msgMap), sid);
        this.sendOnlineSum();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //从set中删除
        WEB_SOCKET_SET.remove(this);
        //在线数减1
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
        this.sendOnlineSum();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + sid + "的信息:" + message);
        //群发消息
        Map<String, String> map = (Map<String, String>) JSON.parse(message);
        Map<String, Object> msgMap = new HashMap<>(100);
        msgMap.put(DEF_TYPE_STR, DEF_USERMSG_STR);
        Map<String, String> userMap = new HashMap<>(100);
        userMap.put(DEF_SID_STR, sid);
        userMap.put(DEF_MESSAGE_STR, map.get(DEF_MSG_STR));
        msgMap.put(DEF_DATA_STR, userMap);
        if (map.get(DEF_SID_STR) != null && !"".equals(map.get(DEF_SID_STR))) {
            sendInfo(JSON.toJSONString(msgMap), map.get(DEF_SID_STR));
        }
    }

    /**
     * @param session session
     * @param error   error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("非正常关闭,发生错误!====>" + error.toString() + "当前在线人数为" + getOnlineCount());
        this.sendOnlineSum();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);
        for (WebSocketServer item : WEB_SOCKET_SET) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    log.info("推送消息到窗口" + item.sid);
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    private void sendOnlineSum() {
        List<String> list = new ArrayList<>();
        for (WebSocketServer item : WEB_SOCKET_SET) {
            list.add(item.sid);
        }
        Map<String, Object> msgMap = new HashMap<>(100);
        for (WebSocketServer item : WEB_SOCKET_SET) {
            msgMap.put(DEF_TYPE_STR, DEF_ONLINE_STR);
            msgMap.put(DEF_DATA_STR, JSON.toJSONString(list.stream().filter(sidi -> !sidi.equals(item.sid)).collect(Collectors.toList())));
            sendInfo(JSON.toJSONString(msgMap), item.sid);
        }

    }

}
