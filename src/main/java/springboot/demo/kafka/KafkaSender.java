package springboot.demo.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author liaojie
 * @since 2022/10/15 9:37
 */
@Component
public class KafkaSender {
    @Resource
    KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String key, String msg) {
        kafkaTemplate.send(topic, key, msg);
    }
}
