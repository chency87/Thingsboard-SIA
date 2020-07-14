package org.thingsboard.client.tools;

import com.google.common.io.Resources;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;

public class MqttSSLClientV2 {
//    private static final String MQTT_URL = "ssl://47.95.0.202:8883";
    private static final String MQTT_URL = "ssl://localhost:8883";

    private static final String clientId = "MQTT_SSL_JAVA_CLIENT";
//    private static final String keyStoreFile = "mqttclient.jks";
    private static final String KEY_STORE_FILE = "E:\\WorkSpace\\Thingsboard-SIA\\tools\\src\\main\\java\\org\\thingsboard\\client\\tools\\mqttclient.jks";
    private static final String JKS="JKS";
    private static final String TLS="TLS";
    private static final String CLIENT_KEYSTORE_PASSWORD = "password";
    private static final String CLIENT_KEY_PASSWORD = "password";

    public static void main(String[] args) {

        try {

            File ksFile = new File(KEY_STORE_FILE);
            File tsFile = new File(KEY_STORE_FILE);



            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            KeyStore trustStore = KeyStore.getInstance(JKS);
            trustStore.load(new FileInputStream(tsFile), CLIENT_KEYSTORE_PASSWORD.toCharArray());
            tmf.init(trustStore);
            KeyStore ks = KeyStore.getInstance(JKS);

            ks.load(new FileInputStream(ksFile), CLIENT_KEYSTORE_PASSWORD.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, CLIENT_KEY_PASSWORD.toCharArray());

            KeyManager[] km = kmf.getKeyManagers();
            TrustManager[] tm = tmf.getTrustManagers();
            SSLContext sslContext = SSLContext.getInstance(TLS);
            sslContext.init(km, tm, null);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setSocketFactory(sslContext.getSocketFactory());
            MqttAsyncClient client = new MqttAsyncClient(MQTT_URL, clientId);
            client.connect(options);
            Thread.sleep(3000);
            MqttMessage message = new MqttMessage();
            message.setPayload("{\"key1\":\"value1\", \"key2\":true, \"key3\": 3.0, \"key4\": 4}".getBytes());
            client.publish("v1/devices/me/telemetry", message);
            client.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
