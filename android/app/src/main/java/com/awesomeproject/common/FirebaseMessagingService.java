// package com.awesomeproject.common;

// import android.app.NotificationManager;
// import android.app.PendingIntent;
// import android.content.Context;
// import android.content.Intent;
// import android.media.RingtoneManager;
// import android.util.Log;

// import androidx.annotation.NonNull;
// import androidx.core.app.NotificationCompat;

// import com.google.firebase.messaging.RemoteMessage;
// import com.awesomeproject.MainActivity;
// import com.awesomeproject.R;

// public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
//     private static final String TAG = "FirebaseMessagingService";
//     private static final String CHANNEL_ID = "hc_server_channel";
//     private String msg, title;

//     @Override
//     public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//         title = remoteMessage.getNotification().getTitle();
//         msg = remoteMessage.getNotification().geatBody();

//         Intent intent = new Intent(this, MainActivity.class);
//         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//         PendingIntent contentIntent = PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class), PendingIntent.FLAG_IMMUTABLE);

//         NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
//                 .setContentTitle(title)
//                 .setAutoCancel(true)
//                 .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                 .setPriority(NotificationCompat.PRIORITY_HIGH)
//                 .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//                 .setVibrate(new long[]{1,1000});

//         NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//         int notificationId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
//         notificationManager.notify(notificationId,mBuilder.build());

//         mBuilder.setContentIntent(contentIntent);
//     }

//     @Override
//     public void onNewToken(@NonNull String token) {
//         Log.d(TAG, "Refreshed token: " + token);
//     }
// }
