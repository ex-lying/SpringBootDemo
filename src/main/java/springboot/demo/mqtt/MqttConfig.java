package springboot.demo.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import tool.util.LogUtil;
import tool.util.SSLUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @className: MqttConfig
 * @author: Lying
 * @description: TODO
 * @date: 2022/10/18 上午11:29
 */
@Configuration
@IntegrationComponentScan
public class MqttConfig {
    private static final LogUtil log = new LogUtil(MqttConfig.class);
    @Value("${spring.mqtt.url}")
    private String url;
    @Value("${spring.mqtt.username}")
    private String username;
    @Value("${spring.mqtt.password}")
    private String password;
    @Value("${spring.mqtt.client_id}")
    private String clientId;
    @Value("${spring.mqtt.ca_file}")
    private String caCerts;
    @Value("${spring.mqtt.cert_file}")
    private String certFile;
    @Value("${spring.mqtt.key_file}")
    private String keyfile;
    @Value("${spring.mqtt.tls_version}")
    private String tlsVersion;
    @Value("${spring.mqtt.default_topic}")
    private String defaultTopic;

    @Autowired
    private DefaultTopicInboundMessageHandler defaultTopicInboundMessageHandler;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() throws Exception {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();

        //再次重连接时，还是原来的session，就可以收到离线消息
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setServerURIs(new String[]{url});
        mqttConnectOptions.setKeepAliveInterval(20);
        mqttConnectOptions.setMaxInflight(1000);
        mqttConnectOptions.setConnectionTimeout(100);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);

        try {
            mqttConnectOptions.setSocketFactory(SSLUtil.getUnidirectionalSocketFactory(caCerts));
        } catch (Exception e) {
            log.info("证书读取失败");
        }

        mqttConnectOptions.setHttpsHostnameVerificationEnabled(false);

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions);

        return factory;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                clientId, mqttClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultRetained(false);
        messageHandler.setAsyncEvents(false);
        // Exactly Once
        messageHandler.setDefaultQos(0);
        messageHandler.setDefaultTopic(defaultTopic);

        return messageHandler;
    }

    @Bean
    public MessageChannel defaultTopicInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducerSupport defaultTopicInbound(MqttPahoClientFactory mqttClientFactory) throws MqttException {
        List<String> topics = new ArrayList<>();

        topics.add(defaultTopic);

        String[] topicStrs = new String[topics.size()];

        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        clientId + "_sub" + new Date().getTime(),
                        mqttClientFactory,
                        topics.toArray(topicStrs)
                );

        adapter.setCompletionTimeout(30000);
        adapter.setQos(0);
        adapter.setOutputChannel(defaultTopicInboundChannel());
        adapter.setConverter(new DefaultPahoMessageConverter());

        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "qiaotongInboundChannel")
    public MessageHandler handler() {
        return defaultTopicInboundMessageHandler;
    }
}
