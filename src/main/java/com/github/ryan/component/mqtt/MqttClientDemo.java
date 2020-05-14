package com.github.ryan.component.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
@Slf4j
public class MqttClientDemo {

    public static void main(String[] args) {
        try {
            final String serverUrl = "tcp://127.0.0.1:1883";
            final String clientId = "-1201073065";
            final String topic = "BORGWARD";

            MemoryPersistence persistence = new MemoryPersistence();
            MqttClient sampleClient = new MqttClient(serverUrl, clientId, persistence);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(false);
            // 设置心跳时间为20s (client 会自动给 broker 发送 PINGREQ 消息)
            connectOptions.setKeepAliveInterval(20);
            // payload 是建立连接时 client 需要构造的核心内容，用于 broker 鉴权和连接初始化
            byte[] payload = "{\"imei\":\"P005000200016022\",\"appKey\":\"6737035105\",\"signature\":\"15ade73a9b0ccdbe13dc3f1990f2584d\",\"deviceType\":\"2\",\"os\":\"12321\",\"osVersion\":\"1\",\"brand\":\"xxx\"}".getBytes();
            connectOptions.setWill(topic, payload, 0, false);
            // 建立连接
            sampleClient.connect(connectOptions);

            // 设置回调
            sampleClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    // do something ...
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

                    log.info("==== Message arrived, topic = {}, mqttMessage = {} ===="
                            , topic, new ObjectMapper().writeValueAsString(mqttMessage));
                    // This method is invoked synchronously by the MQTT client.
                    // An acknowledgment is not sent back to the server until this method returns cleanly.
                    // 该方法正常返回后，client 会自动针对消息进行确认，回复 PUBACK 消息给 broker
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    // do something...
                }
            });

            // 主动断开连接
            // sampleClient.disconnect();
            // 释放资源
            // sampleClient.close();
        } catch (Exception e) {
            log.error("==== mqtt client call exception! ====", e);
        }
    }
}
