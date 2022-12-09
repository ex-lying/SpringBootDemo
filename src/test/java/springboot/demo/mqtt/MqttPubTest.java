package springboot.demo.mqtt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.demo.SpringBootDemoApplication;

/**
 * @className: MqttPubTest
 * @author: Lying
 * @description: TODO
 * @date: 2022/12/9 下午3:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootDemoApplication.class)
public class MqttPubTest {
    @Value("${spring.mqtt.default_topic}")
    private String defaultTopic;

    @Autowired
    MqttOutboundSend mqttOutboundSend;

    @Test
    public void pubDefaultTopicTest() {
        mqttOutboundSend.sendToTopic("hello", defaultTopic);
    }
}
