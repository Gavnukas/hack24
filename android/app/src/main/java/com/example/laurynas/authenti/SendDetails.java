package com.example.laurynas.authenti;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.util.HttpAuthorizer;

import org.java_websocket.client.WebSocketClient;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendDetails extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.laurynas.authenti.action.FOO";
    private static final String ACTION_BAZ = "com.example.laurynas.authenti.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.authenti.intservice.extra.USER";
    private static final String EXTRA_PARAM2 = "com.authenti.intservice.extra.PASS";
    private final Context context = this;

    String derp;
    private Pusher pusher;
    private WebSocketClient clientSocket;


    public SendDetails() {
        super("SendDetails");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {


            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else {
                Log.d("Action:", "Invalid");
            }
            //            else if (ACTION_BAZ.equals(action)) {
            //                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
            //                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
            //                handleActionBaz(param1, param2);
            //            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        HttpAuthorizer authorizer = new HttpAuthorizer("");


        PusherOptions options = new PusherOptions();
        options.setCluster("eu");
        options.setAuthorizer(authorizer);
        int app_id = 189186;
        String key = "1dd6040d071e13880ca2",
                secret = "0c86b344a6b5cb9452ed";

        pusher = new Pusher(key, options);


        PrivateChannel channel = pusher.subscribePrivate("Client-NoAuth");
        channel.bind("private-authenticate", new PrivateChannelEventListener() {
            @Override
            public void onAuthenticationFailure(String s, Exception e) {

            }

            @Override
            public void onSubscriptionSucceeded(String s) {

            }

            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(context);
                Intent notificationIntent = new Intent(context, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

                //set
                builder.setContentIntent(contentIntent);
                builder.setContentText(data);
                builder.setContentTitle(eventName);
                builder.setAutoCancel(true);
                builder.setDefaults(Notification.DEFAULT_ALL);

                Notification notification = builder.build();
                nm.notify((int) System.currentTimeMillis(), notification);
            }
        });

        org.apache.log4j.BasicConfigurator.configure();
        pusher.connect();


    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
