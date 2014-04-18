package com.jessefarebro.mqtt;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import com.jessefarebro.mqtt.persist.SQLPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;


public class MqttService extends Service {
	public static final String DEBUG_TAG = "MqttService";

    public static final String EXTRA_OPTIONS = "_options";

	private AndroidMqttClient mClient;

    private MqttLog mLog;
	
	private AlarmManager mAlarmManager;
	private ConnectivityManager mConnectivityManager;

    public static enum ConnectionStatus {
        INITIAL,
        CONNECTING,
        CONNECTED,
        WAITING_FOR_INTERNET,
        DATA_DISABLED,
        UNKNOWN
    }

    private ConnectionStatus mConnectionStatus
            = ConnectionStatus.INITIAL;

    public static void startService(Context context, MqttOptions options) {
        final Intent intent = new Intent(context, MqttService.class);
        intent.putExtra(MqttService.EXTRA_OPTIONS, options);

        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mConnectionStatus = ConnectionStatus.INITIAL;

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        final String action = intent.getAction();

        switch(action) {
            case "":

            default:

        }

        final MqttOptions options = (MqttOptions) intent.getParcelableExtra(EXTRA_OPTIONS);

        if (options == null) {
            throw new NullPointerException(
                    "MqttOptions is null, did you use MqttService.startService ?");
        }


        mLog = new MqttLog(DEBUG_TAG, options.logLevel);

        return START_STICKY;
    }


    private MqttConnectOptions configureConnectOptions(MqttOptions options) {
        final MqttConnectOptions connectOptions = new MqttConnectOptions();

        if (options.username != null) {
            connectOptions.setUserName(options.username);
        } else if (options.password != null) {
            connectOptions.setPassword(options.password.toCharArray());
        }

        return connectOptions;
    }

    private void configureClient(MqttOptions options) {
        final MqttConnectOptions connectOpts =
                configureConnectOptions(options);

        try {
            mClient = new AndroidMqttClient(options.brokerURI,
                    "testClient", new SQLPersistence(this));
            mLog.log(MqttLog.LogLevel.FULL, "Client Created...");
        } catch (MqttException ex) {
            mLog.log(MqttLog.LogLevel.BASIC,
                    "Creating the AndroidMqttClient failed...\n" + ex.getMessage());
        }
    }

    @Override
    public IBinder onBind(Intent bind) {
        return null;
    }

    public static class Intents {
        public static final String RECEIVE = "com.jessefarebro.mqtt.RECEIVE";
    }
}
