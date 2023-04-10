package springboot.demo.websocket;

import tool.util.ExceptionUtil;
import tool.util.LogUtil;

import java.net.URI;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @className: WebsocketPool
 * @author: Lying
 * @description: TODO
 * @date: 2022/12/26 下午3:24
 */
public class WebsocketPool {
    private static final LogUtil log = new LogUtil(WebsocketPool.class);

    private static final ConcurrentHashMap<String, WebsocketClient> socketPool = new ConcurrentHashMap<>();

    public static void connectSocket(String key, URI serverUri) {
        WebsocketClient websocketClient = socketPool.get(key);

        if (websocketClient == null) {
            websocketClient = new WebsocketClient(serverUri);

            websocketClient.connect();

            socketPool.put(key, websocketClient);
        } else {
            if (websocketClient.isClosed()) {
                socketPool.remove(key);

                websocketClient = new WebsocketClient(serverUri);

                websocketClient.connect();
                socketPool.put(key, websocketClient);
            } else if (websocketClient.isOpen()) {
                sendheartBeat(key);
            }
        }
    }

    public static void sendheartBeat(String key) {
        WebsocketClient websocketClient = socketPool.get(key);
        String heartBeat = "{\"msgType\": \"THIRDPARTY_SERVER_HEARTBEAT_REQ\"}";
        websocketClient.send(heartBeat);
    }

    public static void closeSocket(String key) {
        WebsocketClient client = socketPool.get(key);

        if (client == null) {
            log.warn(String.format("SocketPool已经不存在该连接了，code：%s 关闭socket连接失败！", key));
            return;
        }

        try {
            client.close();
        } catch (Exception e) {
            log.error("关闭连接失败：" + ExceptionUtil.StackTracetoString(e));
        } finally {
            socketPool.remove(key);
        }
    }
}
