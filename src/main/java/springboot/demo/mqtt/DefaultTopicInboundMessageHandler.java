package springboot.demo.mqtt;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import tool.util.LogUtil;

import java.util.Objects;

/**
 * @className: InboundMessageHandler
 * @author: Lying
 * @description: TODO
 * @date: 2022/12/9 下午1:53
 */
@SuppressWarnings("unused")
@Component
public class DefaultTopicInboundMessageHandler implements MessageHandler {
    private final static LogUtil log = new LogUtil(DefaultTopicInboundMessageHandler.class);

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = Objects.requireNonNull(message.getHeaders().get("mqtt_receivedTopic")).toString();
        String payload = message.getPayload().toString();

        log.info("topic:" + topic);
        log.info("payload:" + payload);
    }
}
