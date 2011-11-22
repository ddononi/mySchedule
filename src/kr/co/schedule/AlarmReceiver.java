package kr.co.schedule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	private int YOURAPP_NOTIFICATION_ID = 1;
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	Toast.makeText(context, "wefwef", Toast.LENGTH_SHORT).show();
    	Log.i("service", "broadcast catch!!");
    	Vibrator m_clsVibrator = (Vibrator)context.getSystemService( Context.VIBRATOR_SERVICE );
    	m_clsVibrator.vibrate(500);
    	showNotification(context, R.drawable.alarm);
    }
    
    private void showNotification(Context context, int statusBarIconID) {
        Intent contentIntent = new Intent(context, MyScheduleActivity.class);
        PendingIntent theappIntent = 
                PendingIntent.getBroadcast(context, 0, contentIntent, 0);
        CharSequence from = "Alarm Manager";
        CharSequence message = "The Alarm was fired";

        Notification notif = 
        new Notification(statusBarIconID, null, System.currentTimeMillis());
        notif.setLatestEventInfo(context, from, message, theappIntent);
        
        
        NotificationManager nm = 
        (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(this.YOURAPP_NOTIFICATION_ID, notif);
    }
}
