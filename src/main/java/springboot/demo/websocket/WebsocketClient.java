package springboot.demo.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import tool.util.LogUtil;

import java.net.URI;

/**
 * @className: WebsocketClient
 * @author: Lying
 * @description: TODO
 * @date: 2022/12/21 下午1:56
 */
public class WebsocketClient extends WebSocketClient {
    private static final LogUtil log = new LogUtil(WebsocketClient.class);

    public WebsocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("[websocket] 握手成功");
    }

    @Override
    public void onMessage(String message) {
        log.info("[websocket] 收到消息="+ message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info(String.format("[websocket] 退出连接: code=%s reason=%s remote=%s"
                ,code, reason, remote));
    }

    @Override
    public void onError(Exception e) {
        log.info("[websocket] 连接错误="+e.getMessage());
    }
}
