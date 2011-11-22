package kr.co.schedule;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        
        if(action.equals("android.intent.action.BOOT_COMPLETED")){
            ComponentName comp = new ComponentName(context.getPackageName(), MyScheduleActivity.class.getName());
            ComponentName service = context.startService(new Intent().setComponent(comp));
            
            if (null == service){
                // something really wrong here
                Log.e("aaa", "Could not start service " + comp.toString());
            } else {
              Log.e("aaa", "Received unexpected intent " + intent.toString());   
            }            
        }        
    }
}