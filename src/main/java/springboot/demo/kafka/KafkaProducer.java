package springboot.demo.kafka;

import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tool.util.LogUtil;

import java.util.List;

/**
 * @author tianshl
 * @since 2017/9/1 下午05:03
 */
@SuppressWarnings(value = "unused")
@Component
public class KafkaProducer {
    private final static LogUtil thclLog = new LogUtil();
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 向topic中发送消息
     */
    public void send(String topic, String msg) {
        try {
            kafkaTemplate.send(topic, msg);
        } catch (TimeoutException e) {
            e.printStackTrace();
            thclLog.error(e.getMessage());
            return;
        }
    }


    /**
     * 向topic中发送消息
     */
    public void send(String topic, String key, String msg) {
        try {
            kafkaTemplate.send(topic, key, msg);
        } catch (TimeoutException e) {
            e.printStackTrace();
            thclLog.error(e.getMessage());
            return;
        }
    }


    /**
     * 向topic中发送消息
     */
    public void send(String topic, String key, String msg, String logKey) {
        try {
            kafkaTemplate.send(topic, key, msg);
        } catch (TimeoutException e) {
            e.printStackTrace();
            thclLog.error(e.getMessage(), logKey);
            return;
        }
    }

    /**
     * 向topic中发送消息
     */
    public void send(String topic, List<String> msgs) {
        msgs.forEach(msg -> kafkaTemplate.send(topic, msg));
    }

}
