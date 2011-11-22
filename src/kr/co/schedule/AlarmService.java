package kr.co.schedule;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class AlarmService extends Service {
	private String[] days = {"일", "월", "화", "수", "목", "금", "토"};
	private Calendar calendar = null;
	private long alarmTime;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		//calendar.add(Calendar.SECOND, 10);

		int whenDay = calendar.get(Calendar.DAY_OF_WEEK);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		// 브로드케스트 리시버에 보낼 팬딩인텐트
		PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(),
				0, new Intent(getBaseContext(), AlarmReceiver.class), 0);
		// 표준시간을 기준으로 하는 명시적인 시간에 intent를 발생, 장치를 깨움
		MyDBHelper mydb = new MyDBHelper(getBaseContext());
		SQLiteDatabase db = mydb.getReadableDatabase();
		Cursor cursor = null;
		
		cursor = db.query(MyDBHelper.DATABASE_TABLE, null, null, null, null, null, "order_num asc");
		String day, alarmTime;
		if( cursor.moveToFirst() ){	// cursor에 row가 1개 이상 있으면 
			do{
				Log.i("service", cursor.getString(cursor.getColumnIndex("s_time") ));
				day = cursor.getString(cursor.getColumnIndex("day"));
				alarmTime = cursor.getString(cursor.getColumnIndex("s_time"));
				Log.i("service", "day-->"+  day);
				Log.i("service", "whenDay-->"+  days[whenDay-1]);
				if( /* day.trim() days[whenDay-1] && */ checkAlarmHour(alarmTime) ){
					setAlarmTime(alarmTime);	
					Log.i("service", "now setting!");
					am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
					break;
				}
				
				
				
			}while( cursor.moveToNext() );	// 다음 커서가 있으면 내용을 가져온다.
		}			
		
		//am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
		Log.i("service", "onstartCommand");
		return 0;
	}
	
	private void setAlarmTime(String alaramTime) {
		// TODO Auto-generated method stub
		int alarmHour = Integer.valueOf(alaramTime.substring(0, 2));
		int alarmMin = Integer.valueOf(alaramTime.substring(3));
		calendar.set(Calendar.HOUR_OF_DAY, alarmHour);	// 알람시간 시 설정
		calendar.set(Calendar.MINUTE, alarmMin);	// 알람시간 분 설정
		Log.i("service", calendar.get(Calendar.HOUR_OF_DAY) +":"+ calendar.get(Calendar.MINUTE));
	}

	private boolean checkAlarmHour(String alaramTime){
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);	// 현재 시각 가져오기
		int alarmHour = Integer.valueOf(alaramTime.substring(0, 2));
		Log.i("service", alarmHour +"zzz" + currentHour);
		if(alarmHour > currentHour){
			return true;
		}else if(currentHour == alarmHour){	// 현재시간과 알람시간이 같으면 분까지 체크
			int alarmMin = Integer.valueOf(alaramTime.substring(3));
			return checkAlarmMinute(alarmMin);
		}
		
		return false;
	}
	
	private boolean checkAlarmMinute(int alarmMin){
		int min = calendar.get(Calendar.MINUTE);	// 현재 분 가져오기
		if(alarmMin > min){	// 현재 분보다 크면 
			return true;
		}
		
		return false;
	}	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}	

	

}
