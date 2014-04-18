package com.example.mqtt;

import android.app.Activity;
import android.os.Bundle;
import com.jessefarebro.mqtt.MqttLog;
import com.jessefarebro.mqtt.MqttOptions;
import com.jessefarebro.mqtt.MqttService;

/**
 * Created by Jesse on 2/16/14.
 */
public class SimpleMqttActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_activity);

        final MqttOptions options = new MqttOptions.Builder()
                .setBroker("m2m.eclipse.org")
                .setPort(1883)
                .setKeepAliveInterval(25000)
                .setLogLevel(MqttLog.LogLevel.FULL)
                .build();

        MqttService.startService(this, options);
    }
}