package co.paulburke.android.itemtouchhelperdemo;

/**
 * Created by Kazander on 5/10/2017.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Toast.makeText(arg0, "Alarm received!", Toast.LENGTH_LONG).show();

        NotificationManager notificationManager = (NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Intent notificationIntent = new Intent(arg0, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(arg0, 0,
                notificationIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(arg0)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Alarm is going off!")
                .setContentText("Alarm is going off in FiTime!")
                .setSound(soundUri) //This sets the sound to play
                .setContentIntent(intent);

        //Display notification
        notificationManager.notify(0, mBuilder.build());

    }
}
