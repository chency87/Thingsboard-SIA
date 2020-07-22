package org.thingsboard.client.tools;

import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;

@Slf4j
public class MqttTcpClient {


    private static final String MQTT_URL = "tcp://localhost:1883";

    private static final String CLIENT_ID = "MQTT_TCP_JAVA_CLIENT";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "public";

    public static void main(String[] args) {

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setPassword(PASSWORD.toCharArray());
            options.setUserName(USERNAME);
            MqttAsyncClient client = new MqttAsyncClient(MQTT_URL, CLIENT_ID);
            client.connect(options);
            Thread.sleep(3000);
            MqttMessage message = new MqttMessage();
            message.setPayload("{\"key1\":\"value1\", \"key2\":true, \"key3\": 3.0, \"key4\": 4}".getBytes());
            client.publish("home/garden/fountain", message);
            client.disconnect();
            log.info("Disconnected");
            System.exit(0);
        } catch (Exception e) {
            log.error("Unexpected exception occurred in MqttSslClient", e);
        }
    }
}
